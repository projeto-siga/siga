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
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoHCP;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;


/**
 * A class that represents a row in the CP_ARQUIVO table. You can customize the
 * behavior of this class by editing the class, {@link CpArquivo()}.
 */
@Entity
@Immutable
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "corporativo.cp_arquivo")
public class CpArquivo implements Serializable, PersistentAttributeInterceptable {

	private static final long serialVersionUID = 1L;
	
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
	private Integer tamanho = 0;

	@Transient
	protected byte[] cacheArquivo;
	
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
		long ini = System.currentTimeMillis();
		switch (getTipoArmazenamento()) {
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
		case HCP:
			gerarCaminho();
			ArmazenamentoHCP a = ArmazenamentoHCP.getInstance();
			a.salvar(this, this.getConteudo());
			break;
		default:
			break;
		}
		long fim = System.currentTimeMillis();
		log.info("### Tempo: " + (fim-ini) + " Tamanho: " + this.getConteudo().length);
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
		case HCP:
			if (getCaminho() != null) {
				CpArquivoExcluir excluir = new CpArquivoExcluir();
				excluir.setIdArqExc(getIdArq());
				excluir.setCaminho(getCaminho());
				ContextoPersistencia.em().persist(excluir);
			}
			break;
		default:
			break;
		}
	}

	public static CpArquivo forUpdate(CpArquivo old) {
		if (old != null) {
			if (old.getIdArq() != null) {
				CpArquivo arq = new CpArquivo();
				arq.setTipoArmazenamento(old.getTipoArmazenamento());
				arq.setConteudoTpArq(old.getConteudoTpArq());
				arq.setOrgaoUsuario(old.getOrgaoUsuario());

				arq.setConteudo(old.getConteudo());

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
		case TABELA:
			cacheArquivo = getArquivoBlob() != null ? getArquivoBlob().getConteudoBlobArq() : null;
			break;
		case HCP:
			ArmazenamentoHCP a = ArmazenamentoHCP.getInstance();
			cacheArquivo = a.recuperar(this);
			break;
		default:
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
			arq.setTamanho(conteudo.length);
			return arq;
		}
		return old;
	}

	private void gerarCaminho() {
		String extensao;

		TipoConteudo t = TipoConteudo.getByMimeType(getConteudoTpArq());
		if (t != null)
			extensao = t.getExtensao();
		else
			extensao = TipoConteudo.ZIP.getExtensao();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.AM_PM, Calendar.PM);
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

	public Integer getTamanho() {
		return tamanho;
	}

	private void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	private byte[] getCacheArquivo() {
		return cacheArquivo;
	}

	private void setCacheArquivo(byte[] cacheArquivo) {
		this.cacheArquivo = cacheArquivo;
	}

}