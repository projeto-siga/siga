package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="SR_ATRIBUTO")
public class SrAtributo extends GenericModel {
	
	@Id
	@SequenceGenerator(sequenceName = "SR_ATRIBUTO_SEQ", name = "SR_ATRIBUTO_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SR_ATRIBUTO_SEQ")
	@Column(name = "ID_ATRIBUTO")
	public long id;
	
	@Column(name = "VALOR_ATRIBUTO")
	public String valorAtributo;
	
	@ManyToOne
	@JoinColumn(name="ID_TIPO_ATRIBUTO")
	public SrTipoAtributo tipoAtributo;
	
	@ManyToOne
	@JoinColumn(name="ID_SOLICITACAO")
	public SrSolicitacao solicitacao;
	
	public SrAtributo(){
		
	}
	
	public SrAtributo(SrTipoAtributo tipo, String valor, SrSolicitacao sol){
		this.tipoAtributo = tipo;
		this.valorAtributo = valor;
		this.solicitacao = sol;
	}

}
