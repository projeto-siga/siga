package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "SR_RESPOSTA", schema = "SIGASR")
public class SrResposta extends Objeto {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_RESPOSTA_SEQ", name = "srRespostaSeq")
	@GeneratedValue(generator = "srRespostaSeq")
	@Column(name = "ID_RESPOSTA")
	private Long idResposta;

	@Column(name = "DESCR_RESPOSTA")
	private String descrResposta;

	
	@Column(name = "VALOR_RESPOSTA")
	@Enumerated()
	private SrGrauSatisfacao grauSatisfacao;

	@ManyToOne()
	@JoinColumn(name = "ID_PERGUNTA")
	private SrPergunta pergunta;
	
	@ManyToOne()
	@JoinColumn(name = "ID_MOVIMENTACAO")
	private SrMovimentacao movimentacao;	

	/**
	 * @return the idResposta
	 */
	public Long getIdResposta() {
		return idResposta;
	}

	/**
	 * @param idResposta the idResposta to set
	 */
	public void setIdResposta(Long idResposta) {
		this.idResposta = idResposta;
	}

	/**
	 * @return the descrResposta
	 */
	public String getDescrResposta() {
		return descrResposta;
	}

	/**
	 * @param descrResposta the descrResposta to set
	 */
	public void setDescrResposta(String descrResposta) {
		this.descrResposta = descrResposta;
	}

	/**
	 * @return the pergunta
	 */
	public SrPergunta getPergunta() {
		return pergunta;
	}

	/**
	 * @param pergunta the pergunta to set
	 */
	public void setPergunta(SrPergunta pergunta) {
		this.pergunta = pergunta;
	}

	/**
	 * @return the movimentacao
	 */
	public SrMovimentacao getMovimentacao() {
		return movimentacao;
	}

	/**
	 * @param movimentacao the movimentacao to set
	 */
	public void setMovimentacao(SrMovimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	/**
	 * @param grauSatisfacao the grauSatisfacao to set
	 */
	public void setGrauSatisfacao(SrGrauSatisfacao grauSatisfacao) {
		this.grauSatisfacao = grauSatisfacao;
	}

	public SrResposta() {

	}

	public Long getId() {
		return this.idResposta;
	}

	public void setId(Long id) {
		idResposta = id;
	}
	
	public String getGrauSatisfacao() {
		return grauSatisfacao.getDescrGrauSatisfacao();
	}
	

}
