/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.cp;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;
import org.jboss.logging.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.arquivo.Armazenamento;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoFabrica;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoTemporalidadeEnum;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.ContextoPersistencia.AfterCommit;
import br.gov.jfrj.siga.model.enm.CpExtensoesDeArquivoEnum;

/**
 * A class that represents a row in the CP_ARQUIVO table. You can customize the
 * behavior of this class by editing the class, {@link CpArquivo()}.
 */
@SuppressWarnings("serial")
@NamedQueries({
        @NamedQuery(name = "consultarEstatisticasParaMigracaoDeArmazenamento", query = "select count(arq), sum(arq.tamanho) from CpArquivo arq where (arq.tipoArmazenamento = :origem or arq.tipoArmazenamento is null)"),
        @NamedQuery(name = "consultarReferenciasParaMigracaoDeArmazenamento", query = "select arq from CpArquivo arq where (arq.tipoArmazenamento = :origem or arq.tipoArmazenamento is null) and arq.caminho is null")
})
@Entity
@Immutable
@Cacheable(false)
//@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "corporativo.cp_arquivo")
public class CpArquivo implements Serializable, PersistentAttributeInterceptable {
    private final static org.jboss.logging.Logger log = Logger.getLogger(CpArquivo.class);

    @Id
    @SequenceGenerator(sequenceName = "CORPORATIVO.CP_ARQUIVO_SEQ", name = "CP_ARQUIVO_SEQ")
    @GeneratedValue(generator = "CP_ARQUIVO_SEQ")
    @Column(name = "ID_ARQ")
    private java.lang.Long idArq;

    @LazyToOne(LazyToOneOption.NO_PROXY)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private CpArquivoBlob arquivoBlob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ORGAO_USU")
    private CpOrgaoUsuario orgaoUsuario;

    @Column(name = "CONTEUDO_TP_ARQ", length = 128)
    private java.lang.String conteudoTpArq;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_ARMAZENAMENTO")
    private CpArquivoTipoArmazenamentoEnum tipoArmazenamento;

    @Column(name = "CAMINHO")
    private String caminho;

    @Column(name = "TAMANHO_ARQ")
    private Long tamanho = 0L;

    @Column(name = "HASH_SHA256")
    private String hashSha256;

    @Column(name = "NOME_ARQ")
    private String nomeArquivo;

    @Transient
    protected byte[] cacheArquivo;

    @Transient
    private static boolean isFormatoLivre;

    @Transient
    private PersistentAttributeInterceptor persistentAttributeInterceptor;

    @Transient
    private String nomeSugerido;

    @Transient
    private ArmazenamentoTemporalidadeEnum temporalidadeSugerida;

    private static boolean fGerarCaminhoParaTabela = false;

    @Override
    public PersistentAttributeInterceptor $$_hibernate_getInterceptor() {
        return persistentAttributeInterceptor;
    }

    @Override
    public void $$_hibernate_setInterceptor(PersistentAttributeInterceptor persistentAttributeInterceptor) {
        this.persistentAttributeInterceptor = persistentAttributeInterceptor;
    }
    
    static LoadingCache<TipoECaminho, byte[]> cacheArmazenamento = CacheBuilder.newBuilder()
            .weigher((TipoECaminho key, byte[] value) -> value == null ? 0 : value.length)
            .maximumWeight(10*1024*1024)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build(new CacheLoader<TipoECaminho, byte[]>() {
                public byte[] load(TipoECaminho source) throws Exception {
                    log.info("RECUPERANDO: " + source.caminho + " do armazenamento " + source.tipo);
                    Armazenamento a = ArmazenamentoFabrica.getInstance(source.tipo);
                    return a.recuperar(source.caminho);
                }
            });

    public CpArquivo() {
    }

    @PrePersist
    private void salvarArquivo() {
        if (isFormatoLivre())
            return;

        long ini = System.currentTimeMillis();
        byte[] ab = this.getConteudo();
        switch (getTipoArmazenamento()) {
            case BLOB:
                throw new RuntimeException("Armazenamento em BLOB não é mais suportado.");
            case TABELA:
//			EntityTransaction transaction = CpDao.getInstance().em().getTransaction();
//			 System.out.println("* " + (CpDao.getInstance().em().getTransaction() ==
//					 null || !CpDao.getInstance().em().getTransaction().isActive() ? "NÃO" : "") + " TRANSACIONAL" );
//		
                if (fGerarCaminhoParaTabela) {
                    Armazenamento a = ArmazenamentoFabrica.getInstance(CpArquivoTipoArmazenamentoEnum.S3);
                    TipoConteudo t = identificarTipoDeConteudo();
                    this.caminho = a.gerarCaminho(nomeSugerido, t, temporalidadeSugerida);
                    ArmazenamentoTemporalidadeEnum tempo = a.obterTemporalidadePorCaminho(getCaminho());
                    if (tempo == ArmazenamentoTemporalidadeEnum.TEMPORARIO)
                        log.info("TEMPORÁRIO:  " + getCaminho());
                    else
                        log.info("30 ANOS:     " + getCaminho());
                }
            
                if (this.arquivoBlob == null) {
                    this.arquivoBlob = new CpArquivoBlob();
                    this.arquivoBlob.setArquivo(this);
                    this.arquivoBlob.setConteudoBlobArq(ab);
                }
                break;
            default:
                if (this.tamanho > 0L) {
                    Armazenamento a = ArmazenamentoFabrica.getInstance(getTipoArmazenamento());
                    TipoConteudo t = identificarTipoDeConteudo();
                    this.caminho = a.gerarCaminho(nomeSugerido, t, temporalidadeSugerida);

                    a.salvar(getCaminho(), getConteudoTpArq(), this.getConteudo());
                    cacheArmazenamento.put(new TipoECaminho(getTipoArmazenamento(), getCaminho()), ab);

                    ArmazenamentoTemporalidadeEnum tempo = a.obterTemporalidadePorCaminho(getCaminho());
                    if (tempo == ArmazenamentoTemporalidadeEnum.TEMPORARIO)
                        log.info("TEMPORÁRIO:  " + getCaminho());
                    else
                        log.info("30 ANOS:     " + getCaminho());
                }
                break;
        }
        long fim = System.currentTimeMillis();
        log.debug("Tempo para persistir o arquivo: " + (fim - ini) + " Tamanho: "
                + (ab != null ? ab.length : "nulo"));
    }

    public TipoConteudo identificarTipoDeConteudo() {
        TipoConteudo t = TipoConteudo.getByMimeType(getConteudoTpArq());
        if (t == null)
            t = TipoConteudo.ZIP;
        return t;
    }

    @PostPersist
    private void atualizarIdArqBlob() {
        if (this.arquivoBlob != null) {
            this.arquivoBlob.setIdArqBlob(this.getIdArq());
            ;
        }
    }

    @PreRemove
    private void removerArquivo() {
        switch (getTipoArmazenamento()) {
            case BLOB:
                throw new RuntimeException("Armazenamento em BLOB não é mais suportado.");
            case TABELA:
                if (fGerarCaminhoParaTabela) {
                    Armazenamento a = ArmazenamentoFabrica.getInstance(CpArquivoTipoArmazenamentoEnum.S3);
                    ArmazenamentoTemporalidadeEnum tempo = a.obterTemporalidadePorCaminho(getCaminho());
                    if (tempo == ArmazenamentoTemporalidadeEnum.TEMPORARIO)
                        log.info("EXCLUINDO:   " + getCaminho());
                    else
                        log.info("MANTENDO:    " + getCaminho());
                }
                break;
            default:
                final Armazenamento a = ArmazenamentoFabrica.getInstance(getTipoArmazenamento());
                ArmazenamentoTemporalidadeEnum tempo = a.obterTemporalidadePorCaminho(getCaminho());
                if (tempo == ArmazenamentoTemporalidadeEnum.TEMPORARIO) {
                    log.info("EXCLUINDO:   " + getCaminho());
                    ContextoPersistencia.addAfterCommit(new AfterCommit() {
                        @Override
                        public void run() {
                            a.apagar(getCaminho());
                        }
                    });
                } else
                    log.info("MANTENDO:    " + getCaminho());
                break;
        }
    }

    public static CpArquivo forUpdate(CpArquivo old) {
        return forUpdate(old, true);
    }

    public static CpArquivo forUpdate(CpArquivo old, boolean atualizarConteudo) {
        if (old != null) {
            if (old.getIdArq() != null) {
                CpArquivo arq = new CpArquivo();
                arq.tipoArmazenamento = old.getTipoArmazenamento();
                arq.conteudoTpArq = old.getConteudoTpArq();
                arq.orgaoUsuario = old.getOrgaoUsuario();
                if (atualizarConteudo)
                    arq.cacheArquivo = old.getConteudo();
                arq.hashSha256 = old.getHashSha256();
                arq.nomeArquivo = old.getNomeArquivo();

                ContextoPersistencia.em().remove(old);
                return arq;
            } else
                return old;
        } else
            return new CpArquivo();
    }

    static class TipoECaminho {
        public TipoECaminho(CpArquivoTipoArmazenamentoEnum tipo, String caminho) {
            super();
            this.tipo = tipo;
            this.caminho = caminho;
        }

        CpArquivoTipoArmazenamentoEnum tipo;
        String caminho;

        @Override
        public int hashCode() {
            return Objects.hash(caminho, tipo);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TipoECaminho other = (TipoECaminho) obj;
            return Objects.equals(caminho, other.caminho) && tipo == other.tipo;
        }
        
        public String toString() {
            return tipo.name() + "-" + caminho;
        }
        
        public static TipoECaminho fromString(String s) {
            String[] l = s.split("-", 2);
            return new TipoECaminho(CpArquivoTipoArmazenamentoEnum.valueOf(l[0]), l[1]);
        }
    }

    public byte[] getConteudo() {
        if (cacheArquivo != null)
            return cacheArquivo;
        if (this.idArq == null)
            return null;
        switch (getTipoArmazenamento()) {
            case BLOB:
                throw new RuntimeException("Armazenamento em BLOB não é mais suportado.");
            case TABELA:
                cacheArquivo = getArquivoBlob() != null ? getArquivoBlob().getConteudoBlobArq() : null;
                break;
            default:
                if (caminho == null)
                    return null;
                try {
                    log.debug("CARREGANDO:  " + getCaminho());
                    cacheArquivo = cacheArmazenamento.get(new TipoECaminho(getTipoArmazenamento(), getCaminho()));
                } catch (InvalidCacheLoadException e) {
                    if (e.getMessage().contains("CacheLoader returned null for key"))
                        return null;
                    throw new RuntimeException("Erro recuperando blob '" + getCaminho() + "' do armazenamento " + getTipoArmazenamento(), e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("Erro recuperando blob '" + getCaminho() + "' do armazenamento " + getTipoArmazenamento(), e);
                }
                break;
        }
        return cacheArquivo;
    }

    public static CpArquivo updateOrgaoUsuario(CpArquivo old, CpOrgaoUsuario orgaoUsuario) {
        if (old == null || old.getOrgaoUsuario() == null || !old.getOrgaoUsuario().equals(orgaoUsuario)) {
            CpArquivo arq = CpArquivo.forUpdate(old);
            arq.orgaoUsuario = orgaoUsuario;
            return arq;
        }
        return old;
    }

    public static CpArquivo updateConteudoTp(CpArquivo old, String conteudoTp) {
        if (old == null || !Texto.equals(old.getConteudoTpArq(), conteudoTp)) {
            CpArquivo arq = CpArquivo.forUpdate(old);
            arq.conteudoTpArq = conteudoTp;
            return arq;
        }
        return old;
    }

    public static CpArquivo updateConteudo(CpArquivo old, byte[] conteudo, String nome, ArmazenamentoTemporalidadeEnum temporalidade) {
        if (old == null || !Arrays.equals(old.getConteudo(), conteudo)) {
            CpArquivo arq = CpArquivo.forUpdate(old);
            arq.cacheArquivo = conteudo;
            arq.tamanho = Long.valueOf(conteudo.length);
            arq.setFormatoLivre(false);
            arq.nomeSugerido = nome;
            arq.temporalidadeSugerida = temporalidade;
            return arq;
        }
        return old;
    }

    public static CpArquivo updateFormatoLivre(CpArquivo old, CpOrgaoUsuario orgaoUsuario, String caminho,
            String nomeArquivo,
            Long tamanhoArquivo, CpArquivoTipoArmazenamentoEnum tipoArmazenamento, String hashSha256) {
        CpArquivo arq = CpArquivo.forUpdate(old, false);
        String extensao = CpExtensoesDeArquivoEnum.getTipoConteudo(FilenameUtils.getExtension(nomeArquivo));
        if (extensao == null)
            throw new AplicacaoException("Extensão de arquivo inválida: ." + FilenameUtils.getExtension(nomeArquivo));
        arq.orgaoUsuario = orgaoUsuario;
        arq.conteudoTpArq = extensao;
        arq.nomeArquivo = nomeArquivo;
        arq.caminho = caminho;
        arq.tamanho = tamanhoArquivo;
        arq.tipoArmazenamento = tipoArmazenamento;
        arq.setFormatoLivre(true);
        arq.hashSha256 = hashSha256;
        return arq;
    }

    public java.lang.Long getIdArq() {
        return idArq;
    }

    public CpOrgaoUsuario getOrgaoUsuario() {
        return orgaoUsuario;
    }

    public java.lang.String getConteudoTpArq() {
        return conteudoTpArq;
    }

    public CpArquivoBlob getArquivoBlob() {
        if (this.persistentAttributeInterceptor != null) {
            return (CpArquivoBlob) this.persistentAttributeInterceptor.readObject(
                    this, "arquivoBlob", this.arquivoBlob);
        }
        return this.arquivoBlob;
    }

    public void setArquivoBlob(CpArquivoBlob contaCorrente) {
        if (this.persistentAttributeInterceptor != null) {
            this.arquivoBlob = (CpArquivoBlob) persistentAttributeInterceptor.writeObject(
                    this, "arquivoBlob", this.arquivoBlob, contaCorrente);
        } else {
            this.arquivoBlob = contaCorrente;
        }
    }

    public CpArquivoTipoArmazenamentoEnum getTipoArmazenamento() {
        if (tipoArmazenamento == null)
            tipoArmazenamento = CpArquivoTipoArmazenamentoEnum.valueOf(Prop.get("/siga.armazenamento.arquivo.tipo"));
        return tipoArmazenamento;
    }

    public String getCaminho() {
        return caminho;
    }

    public Long getTamanho() {
        return tamanho;
    }

    private byte[] getCacheArquivo() {
        return cacheArquivo;
    }

    private static boolean isFormatoLivre() {
        return isFormatoLivre;
    }

    private void setFormatoLivre(boolean isFormatoLivre) {
        this.isFormatoLivre = isFormatoLivre;
    }

    public String getHashSha256() {
        return hashSha256;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

}
