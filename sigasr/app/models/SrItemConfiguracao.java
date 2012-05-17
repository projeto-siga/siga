package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_ITEM_CONFIGURACAO")
public class SrItemConfiguracao extends GenericModel{

	@Id
	@GeneratedValue
	@Column(name = "ID_ITEM_CONFIGURACAO")
	public long idItemConfiguracao;

	@Column(name = "DESCR_ITEM_CONFIGURACAO")
	public String descrItemConfiguracao;

	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO_PAI")
	public SrItemConfiguracao itemConfiguracaoPai;

	public SrItemConfiguracao(String descricao) {
		this(descricao, null);
	}

	public SrItemConfiguracao(String descricao,
			SrItemConfiguracao itemConfiguracaoPai) {
		super();
		this.descrItemConfiguracao = descricao;
		this.itemConfiguracaoPai = itemConfiguracaoPai;
	}
	
	public String getDescrItemConfiguracao(){
		String s = "";
		SrItemConfiguracao pai = itemConfiguracaoPai;
		while (pai != null){
			s += "__";
			pai = pai.itemConfiguracaoPai;
		}
		return s + descrItemConfiguracao;
	}

}
