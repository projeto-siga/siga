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
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.search.annotations.DocumentId;

import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.BIE.ExBoletimDoc;

/**
 * A class that represents a row in the EX_DOCUMENTO table. You can customize
 * the behavior of this class by editing the class, {@link ExDocumento()}.
 */
@MappedSuperclass
public abstract class AbstractExDocumento extends ExArquivo implements
		Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private int hashValue = 0;

	@Id
	@DocumentId
	@SequenceGenerator(name = "EX_DOCUMENTO_SEQ")
	@GeneratedValue(generator = "EX_DOCUMENTO_SEQ")
	@Column(name = "ID_DOC")
	private java.lang.Long idDoc;

	@ManyToOne
	@JoinColumn(name = "ID_MOB_PAI")
	private ExMobil exMobilPai;

	@Column(name = "ANO_EMISSAO")
	private java.lang.Long anoEmissao;

	@Column(name = "NUM_EXPEDIENTE")
	private java.lang.Long numExpediente;

	@Column(name = "CONTEUDO_TP_DOC")
	private java.lang.String conteudoTpDoc;

	@Column(name = "DESCR_DOCUMENTO")
	private java.lang.String descrDocumento;

	@Column(name = "DSC_CLASS_DOC")
	private java.lang.String descrClassifNovo;

	@Column(name = "DT_DOC")
	private java.util.Date dtDoc;

	@Column(name = "DT_DOC_ORIGINAL")
	private java.util.Date dtDocOriginal;

	@Column(name = "DT_FECHAMENTO")
	private Date dtFinalizacao;

	@Column(name = "DT_REG_DOC")
	private java.util.Date dtRegDoc;

	@Column(name = "HIS_DT_ALT")
	private java.util.Date dtAltDoc;

	@Column(name = "NM_ARQ_DOC")
	private java.lang.String nmArqDoc;

	@Column(name = "NM_DESTINATARIO")
	private String nmDestinatario;

	@Column(name = "NM_ORGAO_DESTINATARIO")
	private String nmOrgaoExterno;

	@Column(name = "NM_SUBSCRITOR_EXT")
	private java.lang.String nmSubscritorExt;

	@Column(name = "NM_FUNCAO_SUBSCRITOR")
	private java.lang.String nmFuncaoSubscritor;

	@Column(name = "NUM_EXT_DOC")
	private java.lang.String numExtDoc;

	@Column(name = "NUM_ANTIGO_DOC")
	private java.lang.String numAntigoDoc;

	@Column(name = "OBS_ORGAO_DOC")
	private String obsOrgao;

	@Column(name = "FG_ELETRONICO")
	private String fgEletronico;

	@Column(name = "CONTEUDO_BLOB_DOC")
	private Blob conteudoBlobDoc;

	private Integer numSequencia;

	@Column(name = "DNM_DT_ACESSO")
	private Date dnmDtAcesso;

	@Column(name = "DNM_ACESSO")
	private String dnmAcesso;

	@ManyToOne
	@JoinColumn(name = "ID_SUBSCRITOR")
	private DpPessoa subscritor;

	@ManyToOne
	@JoinColumn(name = "ID_CADASTRANTE")
	private DpPessoa cadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_TITULAR")
	private DpPessoa titular;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE")
	private DpLotacao lotaCadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_TITULAR")
	private DpLotacao lotaTitular;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_DESTINATARIO")
	private DpLotacao lotaDestinatario;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_SUBSCRITOR")
	private DpLotacao lotaSubscritor;

	@ManyToOne
	@JoinColumn(name = "ID_DESTINATARIO")
	private DpPessoa destinatario;

	@ManyToOne
	@JoinColumn(name = "ID_CLASSIFICACAO")
	private ExClassificacao exClassificacao;

	@ManyToOne
	@JoinColumn(name = "ID_FORMA_DOC")
	private ExFormaDocumento exFormaDocumento;

	@ManyToOne
	@JoinColumn(name = "ID_MOD")
	private ExModelo exModelo;

	@ManyToOne
	@JoinColumn(name = "ID_TP_DOC")
	private ExTipoDocumento exTipoDocumento;

	@ManyToOne
	@JoinColumn(name = "ID_NIVEL_ACESSO")
	private ExNivelAcesso exNivelAcesso;

	@ManyToOne
	@JoinColumn(name = "DNM_ID_NIVEL_ACESSO")
	private ExNivelAcesso dnmExNivelAcesso;

	@ManyToOne
	@JoinColumn(name = "ID_ORGAO")
	private CpOrgao orgaoExterno;

	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario orgaoUsuario;

	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_DESTINATARIO")
	private CpOrgao orgaoExternoDestinatario;

	@OneToMany(mappedBy = "exDocumento")
	private java.util.SortedSet<ExMobil> exMobilSet = new TreeSet<ExMobil>();

	private java.util.Set<ExBoletimDoc> exBoletimDocSet;

	private ExDocumento exDocAnterior;

	@ManyToOne
	@JoinColumn(name = "ID_MOB_AUTUADO")
	private ExMobil exMobilAutuado;

	/**
	 * Simple constructor of AbstractExDocumento instances.
	 */
	public AbstractExDocumento() {
	}

	/**
	 * Constructor of AbstractExDocumento instances given a simple primary key.
	 * 
	 * @param idDoc
	 */
	public AbstractExDocumento(final java.lang.Long idDoc) {
		this.setIdDoc(idDoc);
	}

	/**
	 * Compara dois documentos por ID
	 * 
	 */
	@Override
	public boolean equals(final Object rhs) {
		if (rhs == null)
			return false;
		if (!(rhs instanceof ExDocumento))
			return false;
		final ExDocumento that = (ExDocumento) rhs;
		if (this.getIdDoc() == null || that.getIdDoc() == null)
			return false;
		return (this.getIdDoc().equals(that.getIdDoc()));
	}

	/**
	 * Retornao ano de emissão do documento, que compõe o código
	 */
	public java.lang.Long getAnoEmissao() {
		return this.anoEmissao;
	}

	/**
	 * Retorna o cadastrante do documento
	 */
	public DpPessoa getCadastrante() {
		return cadastrante;
	}

	/**
	 * COMPLETAR
	 */
	public Blob getConteudoBlobDoc() {
		return this.conteudoBlobDoc;
	}

	/**
	 * COMPLETAR
	 */
	public java.lang.String getConteudoTpDoc() {
		return this.conteudoTpDoc;
	}

	/**
	 * COMPLETAR
	 * 
	 * @return
	 */
	public java.lang.String getDescrClassifNovo() {
		return descrClassifNovo;
	}

	/**
	 * Retorna a descrição do documento
	 */
	public java.lang.String getDescrDocumento() {
		return this.descrDocumento;
	}

	/**
	 * Retorna a pessoa destinatária do documento
	 */
	public DpPessoa getDestinatario() {
		return destinatario;
	}

	/**
	 * Retorna a data do documento
	 */
	public java.util.Date getDtDoc() {
		return this.dtDoc;
	}

	public java.util.Date getDtDocOriginal() {
		return dtDocOriginal;
	}

	/**
	 * Retorna a data de finalização do documento
	 * 
	 * @return
	 */
	public Date getDtFinalizacao() {
		return dtFinalizacao;
	}

	/**
	 * Retorna a data de registro do documento
	 */
	public java.util.Date getDtRegDoc() {
		return this.dtRegDoc;
	}

	/**
	 * Retorna o objeto de relacionamento boletim x documentos relacionado a
	 * este documento
	 * 
	 * @return
	 */
	public java.util.Set<ExBoletimDoc> getExBoletimDocSet() {
		return exBoletimDocSet;
	}

	/**
	 * Retorna a classificação do documento
	 * 
	 * @return
	 */
	public ExClassificacao getExClassificacao() {
		return exClassificacao;
	}

	/**
	 * Retorna o documento usado como base para gerar este, por meio do comando
	 * Duplicar ou Refazer
	 * 
	 * @return
	 */
	public ExDocumento getExDocAnterior() {
		return exDocAnterior;
	}

	/**
	 * Retorna o tipo do documento (Ofício, Memorando, etc)
	 * 
	 * @return
	 */
	public ExFormaDocumento getExFormaDocumento() {
		return exFormaDocumento;
	}

	/**
	 * Retorna o móbil do qual o documento é filho
	 * 
	 * @return
	 */
	public ExMobil getExMobilPai() {
		return exMobilPai;
	}

	/**
	 * Retorna o conjunto de móbil's do documento
	 * 
	 * @return
	 */
	public java.util.SortedSet<ExMobil> getExMobilSet() {
		return exMobilSet;
	}

	/**
	 * Retorna o modelo do documento
	 * 
	 * @return
	 */
	public ExModelo getExModelo() {
		return exModelo;
	}

	/**
	 * Retorna o nível de acesso do documento
	 * 
	 * @return
	 */
	public ExNivelAcesso getExNivelAcesso() {
		return exNivelAcesso;
	}

	/**
	 * Retorna a origem do documento
	 */
	public ExTipoDocumento getExTipoDocumento() {
		return this.exTipoDocumento;
	}

	/**
	 * Retorna, no formato String (S/N) se o documento é eletrônico
	 * 
	 * @return
	 */
	public String getFgEletronico() {
		return fgEletronico;
	}

	/**
	 * Retorna o id do documento
	 */
	public java.lang.Long getIdDoc() {
		return idDoc;
	}

	/**
	 * Retorna a lotação cadastrante do documento
	 */
	public DpLotacao getLotaCadastrante() {
		return lotaCadastrante;
	}

	/**
	 * Retorna a lotação destinatária do documento
	 * */
	public DpLotacao getLotaDestinatario() {
		return lotaDestinatario;
	}

	/**
	 * Retorna a lotação subscritora do documento
	 * */
	public DpLotacao getLotaSubscritor() {
		return lotaSubscritor;
	}

	/**
	 * Retorna a lotação titular do documento
	 * 
	 * @return
	 */
	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	/**
	 * COMPLETAR Retorna o nome do arquivo do documento
	 */
	public java.lang.String getNmArqDoc() {
		return this.nmArqDoc;
	}

	/**
	 * @return Retorna o nome do destinatário não tabelado digitado.
	 */
	public String getNmDestinatario() {
		return nmDestinatario;
	}

	/**
	 * Retorna o valor completo digitado no campo Função;Lotação;Localidade
	 * 
	 * @return
	 */
	public java.lang.String getNmFuncaoSubscritor() {
		return nmFuncaoSubscritor;
	}

	/**
	 * COMPLETAR Retorna o nome do órgão externo
	 * 
	 * @return
	 */
	public String getNmOrgaoExterno() {
		return nmOrgaoExterno;
	}

	/**
	 * Retorna o nome do subscritor externo digitado
	 */
	public java.lang.String getNmSubscritorExt() {
		return this.nmSubscritorExt;
	}

	/**
	 * Retorna o número antigo digitado
	 * 
	 * @return
	 */
	public java.lang.String getNumAntigoDoc() {
		return numAntigoDoc;
	}

	/**
	 * Retorna o número do expediente (não o código completo)
	 */
	public java.lang.Long getNumExpediente() {
		return this.numExpediente;
	}

	/**
	 * Retorna o número externo digitado.
	 */
	public java.lang.String getNumExtDoc() {
		return this.numExtDoc;
	}

	/**
	 * Retorna o número de sequência do documento. Não é o número do expediente.
	 * No caso de ser um subprocesso, retorna o número que gera o código .01,
	 * .02, etc.
	 * 
	 * @return
	 */
	public Integer getNumSequencia() {
		return numSequencia;
	}

	/**
	 * retorna a observação sobre o órgão externo de origem do documento
	 * 
	 * @return
	 */
	public String getObsOrgao() {
		return obsOrgao;
	}

	/**
	 * Retorna o órgão externo de origem do documento
	 * 
	 * @return
	 */
	public CpOrgao getOrgaoExterno() {
		return orgaoExterno;
	}

	/**
	 * Retorna o órgão externo a que se destina o documento
	 * 
	 * @return
	 */
	public CpOrgao getOrgaoExternoDestinatario() {
		return orgaoExternoDestinatario;
	}

	/**
	 * @return Retorna o órgão usuário em que o documento foi produzido.
	 */
	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	/**
	 * @return Retorna o subscritor do documento.
	 */
	public DpPessoa getSubscritor() {
		return subscritor;
	}

	/**
	 * @return Retorna o titular do documento.
	 */
	public DpPessoa getTitular() {
		return titular;
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
		if (this.hashValue == 0) {
			int result = 17;
			final int idDocValue = this.getIdDoc() == null ? 0 : this
					.getIdDoc().hashCode();
			result = result * 37 + idDocValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	/**
	 * Set the value of the ANO_EMISSAO column.
	 * 
	 * @param anoEmissao
	 */
	public void setAnoEmissao(final java.lang.Long anoEmissao) {
		this.anoEmissao = anoEmissao;
	}

	/**
	 * @param cadastrante
	 *            Atribui a cadastrante o valor.
	 */
	public void setCadastrante(final DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
	}

	/**
	 * Set the value of the CONTEUDO_BLOB_DOC column.
	 * 
	 * @param conteudoBlobDoc
	 */
	public void setConteudoBlobDoc(Blob conteudoBlobDoc) {
		this.conteudoBlobDoc = conteudoBlobDoc;
	}

	/**
	 * Set the value of the CONTEUDO_TP_DOC column.
	 * 
	 * @param conteudoTpDoc
	 */
	public void setConteudoTpDoc(final java.lang.String conteudoTpDoc) {
		this.conteudoTpDoc = conteudoTpDoc;
	}

	/*
	 * public void setFgPessoal(final String fgPessoal) { this.fgPessoal =
	 * fgPessoal; }
	 */

	public void setDescrClassifNovo(java.lang.String descrClassifNovo) {
		this.descrClassifNovo = descrClassifNovo;
	}

	/**
	 * Set the value of the DESCR_DOCUMENTO column.
	 * 
	 * @param descrDocumento
	 */
	public void setDescrDocumento(final java.lang.String descrDocumento) {
		this.descrDocumento = descrDocumento;
	}

	/**
	 * @param destinatario
	 *            Atribui a destinatario o valor.
	 */
	public void setDestinatario(final DpPessoa destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * Set the value of the DT_DOC column.
	 * 
	 * @param dtDoc
	 */
	public void setDtDoc(final java.util.Date dtDoc) {
		this.dtDoc = dtDoc;
	}

	public void setDtDocOriginal(java.util.Date dtDocOriginal) {
		this.dtDocOriginal = dtDocOriginal;
	}

	public void setDtFinalizacao(final Date dtFinalizacao) {
		this.dtFinalizacao = dtFinalizacao;
	}

	/**
	 * Set the value of the DT_REG_DOC column.
	 * 
	 * @param dtRegDoc
	 */
	public void setDtRegDoc(final java.util.Date dtRegDoc) {
		this.dtRegDoc = dtRegDoc;
	}

	public void setExBoletimDocSet(java.util.Set<ExBoletimDoc> exBoletimDocSet) {
		this.exBoletimDocSet = exBoletimDocSet;
	}

	public void setExClassificacao(final ExClassificacao exClassificacao) {
		this.exClassificacao = exClassificacao;
	}

	public void setExDocAnterior(ExDocumento exDocAnterior) {
		this.exDocAnterior = exDocAnterior;
	}

	public void setExFormaDocumento(final ExFormaDocumento exFormaDocumento) {
		this.exFormaDocumento = exFormaDocumento;
	}

	public void setExMobilPai(ExMobil exMobilPai) {
		this.exMobilPai = exMobilPai;
	}

	public void setExMobilSet(java.util.SortedSet<ExMobil> exMobilSet) {
		this.exMobilSet = exMobilSet;
	}

	public void setExModelo(final ExModelo exModelo) {
		this.exModelo = exModelo;
	}

	public void setExNivelAcesso(ExNivelAcesso exNivelAcesso) {
		this.exNivelAcesso = exNivelAcesso;
	}

	/**
	 * Set the value of the ID_TP_DOC column.
	 * 
	 * @param exTipoDocumento
	 */
	public void setExTipoDocumento(final ExTipoDocumento exTipoDocumento) {
		this.exTipoDocumento = exTipoDocumento;
	}

	public void setFgEletronico(String fgEletronico) {
		this.fgEletronico = fgEletronico;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idDoc
	 */
	public void setIdDoc(final java.lang.Long idDoc) {
		this.hashValue = 0;
		this.idDoc = idDoc;
	}

	/**
	 * @param lotaCadastrante
	 *            Atribui a lotaCadastrante o valor.
	 */
	public void setLotaCadastrante(final DpLotacao lotaCadastrante) {
		this.lotaCadastrante = lotaCadastrante;
	}

	/**
	 * @param lotaDestinatario
	 *            Atribui a lotaDestinatario o valor.
	 */
	public void setLotaDestinatario(final DpLotacao lotaDestinatario) {
		this.lotaDestinatario = lotaDestinatario;
	}

	/**
	 * @param lotaSubscritor
	 *            Atribui a lotaSubscritor o valor.
	 */
	public void setLotaSubscritor(final DpLotacao lotaSubscritor) {
		this.lotaSubscritor = lotaSubscritor;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Set the value of the NM_ARQ_DOC column.
	 * 
	 * @param nmArqDoc
	 */
	public void setNmArqDoc(final java.lang.String nmArqDoc) {
		this.nmArqDoc = nmArqDoc;
	}

	/**
	 * @param nomeDestinatario
	 *            Atribui a nomeDestinatario o valor.
	 */
	public void setNmDestinatario(final String nomeDestinatario) {
		this.nmDestinatario = nomeDestinatario;
	}

	public void setNmFuncaoSubscritor(java.lang.String nmSubscritorFuncao) {
		this.nmFuncaoSubscritor = nmSubscritorFuncao;
	}

	public void setNmOrgaoExterno(String nmOrgaoExterno) {
		this.nmOrgaoExterno = nmOrgaoExterno;
	}

	/**
	 * Set the value of the NM_SUBSCRITOR_EXT column.
	 * 
	 * @param nmSubscritorExt
	 */
	public void setNmSubscritorExt(final java.lang.String nmSubscritorExt) {
		this.nmSubscritorExt = nmSubscritorExt;
	}

	public void setNumAntigoDoc(java.lang.String numAntigoDoc) {
		this.numAntigoDoc = numAntigoDoc;
	}

	/**
	 * Set the value of the NUM_EXPEDIENTE column.
	 * 
	 * @param numExpediente
	 */
	public void setNumExpediente(final java.lang.Long numExpediente) {
		this.numExpediente = numExpediente;
	}

	/**
	 * Set the value of the NUM_EXT_DOC column.
	 * 
	 * @param numExtDoc
	 */
	public void setNumExtDoc(final java.lang.String numExtDoc) {
		this.numExtDoc = numExtDoc;
	}

	public void setNumSequencia(Integer numSequencia) {
		this.numSequencia = numSequencia;
	}

	public void setObsOrgao(final String obsOrgao) {
		this.obsOrgao = obsOrgao;
	}

	public void setOrgaoExterno(final CpOrgao orgaoExterno) {
		this.orgaoExterno = orgaoExterno;
	}

	public void setOrgaoExternoDestinatario(final CpOrgao cpOrgao) {
		this.orgaoExternoDestinatario = cpOrgao;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	/**
	 * @param subscritor
	 *            Atribui a subscritor o valor.
	 */
	public void setSubscritor(final DpPessoa subscritor) {
		this.subscritor = subscritor;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public ExMobil getExMobilAutuado() {
		return exMobilAutuado;
	}

	public void setExMobilAutuado(ExMobil exMobilAutuado) {
		this.exMobilAutuado = exMobilAutuado;
	}

	public Date getDnmDtAcesso() {
		return dnmDtAcesso;
	}

	public void setDnmDtAcesso(Date dnmDtAcesso) {
		this.dnmDtAcesso = dnmDtAcesso;
	}

	public String getDnmAcesso() {
		return dnmAcesso;
	}

	public void setDnmAcesso(String dnmAcesso) {
		this.dnmAcesso = dnmAcesso;
	}

	public ExNivelAcesso getDnmExNivelAcesso() {
		return dnmExNivelAcesso;
	}

	public void setDnmExNivelAcesso(ExNivelAcesso dnmExNivelAcesso) {
		this.dnmExNivelAcesso = dnmExNivelAcesso;
	}

	public java.util.Date getDtAltDoc() {
		return dtAltDoc;
	}

	public void setDtAltDoc(java.util.Date dtAltDoc) {
		this.dtAltDoc = dtAltDoc;
	}
}