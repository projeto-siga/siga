package br.gov.jfrj.siga.sr.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.base.util.Catalogs;

@Entity
@Table(name = "SR_TIPO_PERGUNTA", schema = Catalogs.SIGASR)
public class SrTipoPergunta extends GenericModel {

	final static public long TIPO_PERGUNTA_TEXTO_LIVRE = 1;

	final static public long TIPO_PERGUNTA_NOTA_1_A_5 = 2;
	
	@Id
	@Column(name = "ID_TIPO_PERGUNTA")
	public Long idTipoPergunta;

	@Column(name = "NOME_TIPO_PERGUNTA")
	public String nomeTipoPergunta;
	
	@Column(name = "DESCR_TIPO_PERGUNTA")
	public String descrTipoPergunta;

	public SrTipoPergunta() {

	}

	public Long getId() {
		return this.idTipoPergunta;
	}

	public void setId(Long id) {
		idTipoPergunta = id;
	}
	
	@SuppressWarnings("unchecked")
	public static List<SrTipoPergunta> buscarTodos() {
		return SrTipoPergunta.all().query.getResultList();
	}
}