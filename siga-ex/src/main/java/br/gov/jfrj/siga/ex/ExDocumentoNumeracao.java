package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the EX_DOCUMENTO_NUMERACAO database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "siga.ex_documento_numeracao")
@NamedQueries({
	@NamedQuery(name="ExDocumentoNumeracao.findAll", query="SELECT e FROM ExDocumentoNumeracao e"),
	@NamedQuery(name="ExDocumentoNumeracao.findById", query="SELECT e.idDocumentoNumeracao FROM ExDocumentoNumeracao e where e.idDocumentoNumeracao = :id "),
	@NamedQuery(name="ExDocumentoNumeracao.obterDocumentoNumeracao", query="SELECT e "
			+ "FROM ExDocumentoNumeracao e "
			+ "where e.idOrgaoUsu = :idOrgaoUsu "
			+ "and e.idFormaDoc = :idFormaDoc "
			+ "and e.anoEmissao = :anoEmissao "
			+ "and e.flAtivo = :flAtivo"),
	@NamedQuery(name="ExDocumentoNumeracao.obterNumeroGerado", query="SELECT e.nrDocumento "
			+ "FROM ExDocumentoNumeracao e "
			+ "where e.idOrgaoUsu = :idOrgaoUsu "
			+ "and e.idFormaDoc = :idFormaDoc "
			+ "and e.anoEmissao = :anoEmissao "
			+ "and e.flAtivo = :flAtivo"),
	@NamedQuery(name="ExDocumentoNumeracao.incrementaNumeroDocumento", query="UPDATE ExDocumentoNumeracao e SET e.nrDocumento = e.nrDocumento + :increment WHERE e.idDocumentoNumeracao = :id"),
	@NamedQuery(name="ExDocumentoNumeracao.existeRangeDocumentoNumeracao", query="SELECT e.idDocumentoNumeracao FROM ExDocumentoNumeracao e "
			+ "where e.idOrgaoUsu = :idOrgaoUsu "
			+ "and e.idFormaDoc = :idFormaDoc "
			+ "Order by e.anoEmissao desc"),
	@NamedQuery(name="ExDocumentoNumeracao.mantemRangeNumeroDocumento", query="UPDATE ExDocumentoNumeracao e SET e.nrDocumento = e.nrDocumento + :increment, e.anoEmissao = :anoEmissao,  e.flAtivo = :flAtivo WHERE e.idDocumentoNumeracao = :id")

	
})

public class ExDocumentoNumeracao implements Serializable {

	@Id
	@SequenceGenerator(name="EX_DOCUMENTO_NUMERACAO_IDDOCUMENTONUMERACAO_GENERATOR", sequenceName="EX_DOCUMENTO_NUMERACAO_SEQ")
	@GeneratedValue(generator="EX_DOCUMENTO_NUMERACAO_IDDOCUMENTONUMERACAO_GENERATOR")
	@Column(name="ID_DOCUMENTO_NUMERACAO")
	private long idDocumentoNumeracao;

	@Column(name="ANO_EMISSAO")
	private Long anoEmissao;

	@Column(name="FL_ATIVO")
	private String flAtivo;

	@Column(name="ID_FORMA_DOC")
	private Long idFormaDoc;

	@Column(name="ID_ORGAO_USU")
	private Long idOrgaoUsu;

	@Column(name="NR_DOCUMENTO")
	private Long nrDocumento;

	@Column(name="NR_FINAL")
	private Long nrFinal;

	@Column(name="NR_INICIAL")
	private Long nrInicial;

	public ExDocumentoNumeracao() {
	}

	public long getIdDocumentoNumeracao() {
		return this.idDocumentoNumeracao;
	}

	public void setIdDocumentoNumeracao(long idDocumentoNumeracao) {
		this.idDocumentoNumeracao = idDocumentoNumeracao;
	}

	public Long getAnoEmissao() {
		return this.anoEmissao;
	}

	public void setAnoEmissao(Long anoEmissao) {
		this.anoEmissao = anoEmissao;
	}

	public String getFlAtivo() {
		return this.flAtivo;
	}

	public void setFlAtivo(String flAtivo) {
		this.flAtivo = flAtivo;
	}

	public Long getIdFormaDoc() {
		return this.idFormaDoc;
	}

	public void setIdFormaDoc(Long idFormaDoc) {
		this.idFormaDoc = idFormaDoc;
	}

	public Long getIdOrgaoUsu() {
		return this.idOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}

	public Long getNrDocumento() {
		return this.nrDocumento;
	}

	public void setNrDocumento(Long nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public Long getNrFinal() {
		return this.nrFinal;
	}

	public void setNrFinal(Long nrFinal) {
		this.nrFinal = nrFinal;
	}

	public Long getNrInicial() {
		return this.nrInicial;
	}

	public void setNrInicial(Long nrInicial) {
		this.nrInicial = nrInicial;
	}
	
	public void incrementaNumeroDocumento() {
		this.nrDocumento = nrDocumento + 1;
	}

}