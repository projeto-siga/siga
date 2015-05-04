package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="SR_ATRIBUTO_SOLICITACAO", schema="SIGASR")
public class SrAtributoSolicitacao extends GenericModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ATRIBUTO_SOLICITACAO_SEQ", name = "srAtributoSolicitacaoSeq")
	@GeneratedValue(generator = "srAtributoSolicitacaoSeq")
	@Column(name = "ID_ATRIBUTO_SOLICITACAO")
	public long id;
	
	@Column(name = "VALOR_ATRIBUTO_SOLICITACAO")
	public String valorAtributoSolicitacao;
	
	@ManyToOne
	@JoinColumn(name="ID_ATRIBUTO")
	public SrAtributo atributo;
	
	@ManyToOne
	@JoinColumn(name="ID_SOLICITACAO")
	public SrSolicitacao solicitacao;
	
	public SrAtributoSolicitacao(){
	}
	
	public SrAtributoSolicitacao(SrAtributo tipo, String valor, SrSolicitacao sol){
		this.atributo = tipo;
		this.valorAtributoSolicitacao = valor;
		this.solicitacao = sol;
	}
}
