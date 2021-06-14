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
/*
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.CpArquivoTipoArmazenamentoEnum;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

/**
 * A class that represents a row in the EX_MODELO table. You can customize the
 * behavior of this class by editing the class, {@link ExModelo()}.
 */
@MappedSuperclass
@NamedQueries({ @NamedQuery(name = "consultarModeloAtual", query = "select mod from ExModelo mod where mod.hisIdIni = :hisIdIni and mod.hisDtFim = null") })
public abstract class AbstractExModelo extends HistoricoAuditavelSuporte
		implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The composite primary key value. */
	@Id
	@SequenceGenerator(name = "EX_MODELO_SEQ", sequenceName = "siga.ex_modelo_id_mod_seq")
	@GeneratedValue(generator = "EX_MODELO_SEQ")
	@Column(name = "ID_MOD", unique = true, nullable = false)
	private Long idMod;

	@Transient
	protected byte[] cacheConteudoBlobMod;
	
	/** The value of the simple conteudoBlobMod property. */
	@Lob
	@Column(name = "CONTEUDO_BLOB_MOD")
	@Basic(fetch = FetchType.LAZY)
	private byte[] conteudoBlobMod;

	/** The value of the simple conteudoTpBlob property. */
	@Column(name = "CONTEUDO_TP_BLOB", length = 128)
	private String conteudoTpBlob;

	/** The value of the simple descMod property. */
	@Column(name = "DESC_MOD", length = 256)
	private String descMod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLASS_CRIACAO_VIA")
	private ExClassificacao exClassCriacaoVia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLASSIFICACAO")
	private ExClassificacao exClassificacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FORMA_DOC")
	private ExFormaDocumento exFormaDocumento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NIVEL_ACESSO")
	private ExNivelAcesso exNivelAcesso;

	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ARQ")
	private CpArquivo cpArquivo;

	/** The value of the exModeloTipologiaSet one-to-many association. */

	@Column(name = "NM_ARQ_MOD", length = 256)
	private String nmArqMod;

	/** The value of the simple nomeModelo property. */
	@Column(name = "NM_MOD", nullable = false, length = 128)
	private String nmMod;

	@Column(name = "NM_DIRETORIO", length = 128)
	private String nmDiretorio;

	@Column(name = "HIS_IDE", length = 128)
	private String uuid;
	
	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam de HistoricoSuporte.
	//
	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	@Override
	public Integer getHisAtivo() {
		this.hisAtivo = super.getHisAtivo();
		return this.hisAtivo;
	}
	
	@Override
	public void setHisAtivo(Integer hisAtivo) {
		super.setHisAtivo(hisAtivo);
		this.hisAtivo = getHisAtivo();
	}

	@Column(name = "MARCA_DAGUA", length = 13)
	private String marcaDagua;

	// private Set classificacaoSet;

	/**
	 * Simple constructor of AbstractExModelo instances.
	 */
	public AbstractExModelo() {
	}

	/**
	 * Constructor of AbstractExModelo instances given a simple primary key.
	 * 
	 * @param idMod
	 */
	public AbstractExModelo(final Long idMod) {
		this.setIdMod(idMod);
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 * @return boolean
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof ExModelo))
			return false;
		final ExModelo that = (ExModelo) rhs;
		if ((this.getIdMod() == null ? that.getIdMod() == null : this
				.getIdMod().equals(that.getIdMod()))) {
			if ((this.getDescMod() == null ? that.getDescMod() == null : this
					.getDescMod().equals(that.getDescMod())))
				return true;
		}
		return false;

	}

	/**
	 * Return the value of the DESC_MOD column.
	 * 
	 * @return String
	 */
	public String getDescMod() {
		return this.descMod;
	}

	public ExClassificacao getExClassCriacaoVia() {
		return exClassCriacaoVia;
	}

	public ExClassificacao getExClassificacao() {
		return exClassificacao;
	}

	/*    *//**
	 * @return Retorna o atributo tipologiaSet.
	 */
	/*
	 * public Set getClassificacaoSet() { return classificacaoSet; }
	 */

	public ExFormaDocumento getExFormaDocumento() {
		return exFormaDocumento;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return Long
	 */
	public Long getIdMod() {
		return idMod;
	}

	/*    *//**
	 * @param classificacaoSet
	 *            Atribui a classificacaoSet o valor.
	 */
	/*
	 * public void setClassificacaoSet(Set classificacaoSet) {
	 * this.classificacaoSet = classificacaoSet; }
	 */

	public String getNmArqMod() {
		return nmArqMod;
	}

	/**
	 * Return the value of the NM_MOD column.
	 * 
	 * @return String
	 */
	public String getNmMod() {
		return this.nmMod;
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdMod() == null ? 0 : this.getIdMod().hashCode();
		result = result * 37 + idValue;
		idValue = this.getDescMod() == null ? 0 : this.getDescMod().hashCode();
		return result * 37 + idValue;
	}

	/**
	 * Set the value of the DESC_MOD column.
	 * 
	 * @param descMod
	 */
	public void setDescMod(final String descMod) {
		this.descMod = descMod;
	}

	public void setExClassCriacaoVia(ExClassificacao exClassCriacaoVia) {
		this.exClassCriacaoVia = exClassCriacaoVia;
	}

	public void setExClassificacao(final ExClassificacao exClassificacao) {
		this.exClassificacao = exClassificacao;
	}

	public void setExFormaDocumento(final ExFormaDocumento exFormaDocumento) {
		this.exFormaDocumento = exFormaDocumento;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idMod
	 */
	public void setIdMod(final Long idMod) {
		this.idMod = idMod;
	}

	public void setNmArqMod(final String nmArqMod) {
		this.nmArqMod = nmArqMod;
	}

	/**
	 * Set the value of the NM_MOD column.
	 * 
	 * @param nmMod
	 */
	public void setNmMod(final String nmMod) {
		this.nmMod = nmMod.replaceAll("/", "&#47;");
	}

	public String getMarcaDagua() {
		return marcaDagua;
	}
	
	public void setMarcaDagua(String marcaDagua) {
		this.marcaDagua = marcaDagua;
	}

	public ExNivelAcesso getExNivelAcesso() {
		return exNivelAcesso;
	}

	public void setExNivelAcesso(ExNivelAcesso exNivelAcesso) {
		this.exNivelAcesso = exNivelAcesso;
	}

	public String getNmDiretorio() {
		return nmDiretorio;
	}

	public void setNmDiretorio(String nmDiretorio) {
		this.nmDiretorio = nmDiretorio;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean semelhante(Assemelhavel obj, int profundidade) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractExModelo other = (AbstractExModelo) obj;
		if (getConteudoBlobMod() == null) {
			if (other.getConteudoBlobMod() != null) {
				return false;
			}
		} else {
			if (other.getConteudoBlobMod() == null) {
				return false;
			}

			byte[] abthis = getConteudoBlobMod();
			byte[] abother = other.getConteudoBlobMod();

			String sthis = new String(abthis, StandardCharsets.UTF_8);
			String sother = new String(abother, StandardCharsets.UTF_8);

			sthis = sthis.replace("\r\n", "\n");
			sother = sother.replace("\r\n", "\n");

			if (!sthis.equals(sother)) {
				return false;
			}
		}
		if (getConteudoTpBlob() == null) {
			if (other.getConteudoTpBlob() != null) {
				return false;
			}
		} else if (!getConteudoTpBlob().equals(other.getConteudoTpBlob())) {
			return false;
		}
		if (descMod == null) {
			if (other.descMod != null) {
				return false;
			}
		} else if (!descMod.equals(other.descMod)) {
			return false;
		}
		if (exClassCriacaoVia == null) { 
			if (other.exClassCriacaoVia != null) {
				return false;
			}
		} else if (!exClassCriacaoVia.equals(other.exClassCriacaoVia)) {
			return false;
		}
		if (exClassificacao == null) {
			if (other.exClassificacao != null) {
				return false;
			}
		} else if (!exClassificacao.equals(other.exClassificacao)) {
			return false;
		}
		if (exFormaDocumento == null) {
			if (other.exFormaDocumento != null) {
				return false;
			}
		} else if (!exFormaDocumento.equals(other.exFormaDocumento)) {
			return false;
		}
		if (exNivelAcesso == null) {
			if (other.exNivelAcesso != null) {
				return false;
			}
		} else if (!exNivelAcesso.equals(other.exNivelAcesso)) {
			return false;
		}
		if (nmArqMod == null) {
			if (other.nmArqMod != null) {
				return false;
			}
		} else if (!nmArqMod.equals(other.nmArqMod)) {
			return false;
		}
		if (nmDiretorio == null) {
			if (other.nmDiretorio != null) {
				return false;
			}
		} else if (!nmDiretorio.equals(other.nmDiretorio)) {
			return false;
		}
		if (nmMod == null) {
			if (other.nmMod != null) {
				return false;
			}
		} else if (!nmMod.equals(other.nmMod)) {
			return false;
		}
		return true;
	}

	public CpArquivo getCpArquivo() {
		return cpArquivo;
	}

	public void setCpArquivo(CpArquivo cpArquivo) {
		this.cpArquivo = cpArquivo;
	}

	public String getConteudoTpBlob() {
		if (getCpArquivo() == null || getCpArquivo().getConteudoTpArq() == null)
			return conteudoTpBlob;
		return getCpArquivo().getConteudoTpArq();
	}

	public void setConteudoTpBlob(final String conteudoTpMod) {
		this.conteudoTpBlob = conteudoTpMod;
		if (conteudoBlobMod==null && !CpArquivoTipoArmazenamentoEnum.BLOB.equals(CpArquivoTipoArmazenamentoEnum.valueOf(Prop.get("/siga.armazenamento.arquivo.tipo")))) {
			cpArquivo = CpArquivo.updateConteudoTp(cpArquivo, conteudoTpMod);
	    }
	}

	public byte[] getConteudoBlobMod() {
		if(cacheConteudoBlobMod != null) {
			return cacheConteudoBlobMod;
		} else if (getCpArquivo() == null) {
			cacheConteudoBlobMod = conteudoBlobMod;
		} else {
			try {
				cacheConteudoBlobMod = getCpArquivo().getConteudo();
			} catch (Exception e) {
				throw new AplicacaoException(e.getMessage());
			}
		}
		return cacheConteudoBlobMod;
	}

	public void setConteudoBlobMod(byte[] createBlob) {
		cacheConteudoBlobMod = createBlob;
		if (this.cpArquivo==null && (this.conteudoBlobMod!=null || CpArquivoTipoArmazenamentoEnum.BLOB.equals(CpArquivoTipoArmazenamentoEnum.valueOf(Prop.get("/siga.armazenamento.arquivo.tipo"))))) {
			this.conteudoBlobMod = createBlob;
		} else if(cacheConteudoBlobMod != null){
			cpArquivo = CpArquivo.updateConteudo(cpArquivo, cacheConteudoBlobMod);
		}
	}
	
}
