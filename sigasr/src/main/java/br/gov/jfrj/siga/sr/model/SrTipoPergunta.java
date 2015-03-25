package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "SR_TIPO_PERGUNTA", schema = "SIGASR")
public class SrTipoPergunta extends Objeto {

	final static public long TIPO_PERGUNTA_TEXTO_LIVRE = 1;

	final static public long TIPO_PERGUNTA_NOTA_1_A_5 = 2;
	
	public static ActiveRecord<SrTipoPergunta> AR = new ActiveRecord<>(SrTipoPergunta.class);
	
	@Id
	@Column(name = "ID_TIPO_PERGUNTA")
	private Long idTipoPergunta;

	@Column(name = "NOME_TIPO_PERGUNTA")
	private String nomeTipoPergunta;
	
	@Column(name = "DESCR_TIPO_PERGUNTA")
	private String descrTipoPergunta;

	public Long getIdTipoPergunta() {
		return idTipoPergunta;
	}

	public void setIdTipoPergunta(Long idTipoPergunta) {
		this.idTipoPergunta = idTipoPergunta;
	}

	public String getNomeTipoPergunta() {
		return nomeTipoPergunta;
	}

	public void setNomeTipoPergunta(String nomeTipoPergunta) {
		this.nomeTipoPergunta = nomeTipoPergunta;
	}

	public String getDescrTipoPergunta() {
		return descrTipoPergunta;
	}

	public void setDescrTipoPergunta(String descrTipoPergunta) {
		this.descrTipoPergunta = descrTipoPergunta;
	}

	public SrTipoPergunta() {

	}

	public Long getId() {
		return this.idTipoPergunta;
	}

	public void setId(Long id) {
		idTipoPergunta = id;
	}

}
