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
import java.util.UUID;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.arquivo.Armazenamento;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoFabrica;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.ContextoPersistencia.AfterCommit;
import br.gov.jfrj.siga.model.enm.CpExtensoesDeArquivoEnum;


/**
 * A class that represents a row in the CP_ARQUIVO table. You can customize the
 * behavior of this class by editing the class, {@link CpArquivo()}.
 */
@SuppressWarnings("serial")
@NamedQueries({
    @NamedQuery(name = "consultarEstatisticasParaMigracaoDeArmazenamento", query = "select count(*), sum(arq.tamanho) from CpArquivo arq where arq.tipoArmazenamento = :origem")
})
@Entity
@Immutable
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "corporativo.cp_arquivo")
public class CpArquivo implements Serializable, PersistentAttributeInterceptable {
    public static final ActiveRecord<DpPessoa> AR = new ActiveRecord<>(
            DpPessoa.class);
    
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
 
    @Override
    public PersistentAttributeInterceptor $$_hibernate_getInterceptor() {
        return persistentAttributeInterceptor;
    }
 
    @Override
    public void $$_hibernate_setInterceptor(PersistentAttributeInterceptor persistentAttributeInterceptor) {
        this.persistentAttributeInterceptor = persistentAttributeInterceptor;
    }

	/**
	 * Simple constructor of AbstractExDocumento instances.
	 */
	public CpArquivo() {
	}

	@PrePersist
	private void salvarArquivo() {
		if (isFormatoLivre())
			return;
		
		long ini = System.currentTimeMillis();
		switch (getTipoArmazenamento()) {
        case BLOB:
            throw new RuntimeException("Armazenamento em BLOB não é mais suportado.");
		case TABELA:
//			EntityTransaction transaction = CpDao.getInstance().em().getTransaction();
//			 System.out.println("* " + (CpDao.getInstance().em().getTransaction() ==
//					 null || !CpDao.getInstance().em().getTransaction().isActive() ? "NÃO" : "") + " TRANSACIONAL" );
//		
			if (this.arquivoBlob == null) {
				this.arquivoBlob = new CpArquivoBlob();
				this.arquivoBlob.setArquivo(this);
				this.arquivoBlob.setConteudoBlobArq(this.getConteudo());
			}
			break;
		default:
			gerarCaminho();
            Armazenamento a = ArmazenamentoFabrica.getInstance(getTipoArmazenamento());
			a.salvar(getIdArq(), getCaminho(), getConteudoTpArq(), this.getConteudo());
			break;
		}
		long fim = System.currentTimeMillis();
		log.debug("Tempo para persistir o arquivo: " + (fim-ini) + " Tamanho: " + (this.getConteudo() != null ? this.getConteudo().length : "nulo"));
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
		default:
            ContextoPersistencia.addAfterCommit(new AfterCommit() {
                @Override
                public void run() {
                    Armazenamento a = ArmazenamentoFabrica.getInstance(getTipoArmazenamento());
                    a.apagar(getIdArq(), getCaminho());
                }
            });
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
				arq.setTipoArmazenamento(old.getTipoArmazenamento());
				arq.setConteudoTpArq(old.getConteudoTpArq());
				arq.setOrgaoUsuario(old.getOrgaoUsuario());
				if (atualizarConteudo)
					arq.setConteudo(old.getConteudo());
				arq.setHashSha256(old.getHashSha256());
				arq.setNomeArquivo(old.getNomeArquivo());

				ContextoPersistencia.em().remove(old);
				return arq;
			} else
				return old;
		} else
			return new CpArquivo();
	}

	public byte[] getConteudo() {
		if (cacheArquivo != null)
			return cacheArquivo;
		switch (getTipoArmazenamento()) {
	    case BLOB:
	        throw new RuntimeException("Armazenamento em BLOB não é mais suportado.");
		case TABELA:
			cacheArquivo = getArquivoBlob() != null ? getArquivoBlob().getConteudoBlobArq() : null;
			break;
		default:
		    Armazenamento a = ArmazenamentoFabrica.getInstance(getTipoArmazenamento());
			cacheArquivo = a.recuperar(getIdArq(), getCaminho());
			break;
		}
		return cacheArquivo;
	}

	private void setConteudo(byte[] createBlob) {
		this.cacheArquivo = createBlob;
	}

	public static CpArquivo updateOrgaoUsuario(CpArquivo old, CpOrgaoUsuario orgaoUsuario) {
		if (old == null || old.getOrgaoUsuario() == null || !old.getOrgaoUsuario().equals(orgaoUsuario)) {
			CpArquivo arq = CpArquivo.forUpdate(old);
			arq.setOrgaoUsuario(orgaoUsuario);
			return arq;
		}
		return old;
	}

	public static CpArquivo updateConteudoTp(CpArquivo old, String conteudoTp) {
		if (old == null || !Texto.equals(old.getConteudoTpArq(), conteudoTp)) {
			CpArquivo arq = CpArquivo.forUpdate(old);
			arq.setConteudoTpArq(conteudoTp);
			return arq;
		}
		return old;
	}

	public static CpArquivo updateConteudo(CpArquivo old, byte[] conteudo) {
		if (old == null || !Arrays.equals(old.getConteudo(), conteudo)) {
			CpArquivo arq = CpArquivo.forUpdate(old);
			arq.setConteudo(conteudo);
			arq.setTamanho(Long.valueOf(conteudo.length));
			arq.setFormatoLivre(false);
			return arq;
		}
		return old;
	}

	public static CpArquivo updateFormatoLivre(CpArquivo old, CpOrgaoUsuario orgaoUsuario, String caminho, String nomeArquivo, 
			Long tamanhoArquivo, CpArquivoTipoArmazenamentoEnum tipoArmazenamento, String hashSha256) {
		CpArquivo arq = CpArquivo.forUpdate(old, false);
		String extensao = CpExtensoesDeArquivoEnum.getTipoConteudo(FilenameUtils.getExtension(nomeArquivo));
		if (extensao == null)
			throw new AplicacaoException("Extensão de arquivo inválida: ." + FilenameUtils.getExtension(nomeArquivo));
		arq.setOrgaoUsuario(orgaoUsuario);
		arq.setConteudoTpArq(extensao);
		arq.setNomeArquivo(nomeArquivo);
		arq.setCaminho(caminho);
		arq.setTamanho(tamanhoArquivo);
		arq.setTipoArmazenamento(tipoArmazenamento);
		arq.setFormatoLivre(true);
		arq.setHashSha256(hashSha256);
		return arq;
	}

	private void gerarCaminho() {
		String extensao;

		TipoConteudo t = TipoConteudo.getByMimeType(getConteudoTpArq());
		if (t != null)
			extensao = t.getExtensao();
		else
			extensao = TipoConteudo.ZIP.getExtensao();

		Calendar c = Calendar.getInstance();
		this.caminho = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DATE) + "/"
				+ c.get(Calendar.HOUR_OF_DAY) + "/" + c.get(Calendar.MINUTE) + "/" + UUID.randomUUID().toString() + "."
				+ extensao;
	}

	public java.lang.Long getIdArq() {
		return idArq;
	}

	public void setIdArq(java.lang.Long idArq) {
		this.idArq = idArq;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	private void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public java.lang.String getConteudoTpArq() {
		return conteudoTpArq;
	}

	private void setConteudoTpArq(java.lang.String conteudoTpArq) {
		this.conteudoTpArq = conteudoTpArq;
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

	private void setTipoArmazenamento(CpArquivoTipoArmazenamentoEnum tipoArmazenamento) {
		this.tipoArmazenamento = tipoArmazenamento;
	}

	public String getCaminho() {
		return caminho;
	}

	private void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public Long getTamanho() {
		return tamanho;
	}

	private void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}

	private byte[] getCacheArquivo() {
		return cacheArquivo;
	}

	private void setCacheArquivo(byte[] cacheArquivo) {
		this.cacheArquivo = cacheArquivo;
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

	public void setHashSha256(String hashSha256) {
		this.hashSha256 = hashSha256;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

}
