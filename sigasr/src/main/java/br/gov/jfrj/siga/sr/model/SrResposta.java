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

import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.base.util.Catalogs;

@Entity
@Table(name = "SR_RESPOSTA", schema = Catalogs.SIGASR)
public class SrResposta extends GenericModel {

	@Id
	@SequenceGenerator(sequenceName = Catalogs.SIGASR +".SR_RESPOSTA_SEQ", name = "srRespostaSeq")
	@GeneratedValue(generator = "srRespostaSeq")
	@Column(name = "ID_RESPOSTA")
	public Long idResposta;

	@Column(name = "DESCR_RESPOSTA")
	public String descrResposta;

	
	@Column(name = "VALOR_RESPOSTA")
	@Enumerated()
	public SrGrauSatisfacao grauSatisfacao;

	@ManyToOne()
	@JoinColumn(name = "ID_PERGUNTA")
	public SrPergunta pergunta;
	
	@ManyToOne()
	@JoinColumn(name = "ID_MOVIMENTACAO")
	public SrMovimentacao movimentacao;	

	public SrResposta() {

	}

	public Long getId() {
		return this.idResposta;
	}

	public void setId(Long id) {
		idResposta = id;
	}
	
	public String getGrauSatisfacao() {
		return grauSatisfacao.descrGrauSatisfacao;
	}
	

}
