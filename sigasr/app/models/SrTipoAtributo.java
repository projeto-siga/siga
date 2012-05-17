package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="SR_TIPO_ATRIBUTO")
public class SrTipoAtributo extends GenericModel {
	
	@Id
	@Column(name = "ID_TIPO_ATRIBUTO")
	public long id;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "SR_TIPO_ATRIB_ITEM_CONFIG",
	joinColumns = {
	@JoinColumn(name="ID_TIPO_ATRIBUTO") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="ID_ITEM_CONFIGURACAO")
	}
	)
	public Set<SrItemConfiguracao> itemConfiguracaoSet;
	
	@Column(name="NOME")
	public String nome;
	
	@Column(name="DESCRICAO")
	public String descricao;

}
