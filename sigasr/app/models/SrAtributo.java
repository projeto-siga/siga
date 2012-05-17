package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="SR_ATRIBUTO")
public class SrAtributo extends GenericModel {
	
	@Id
	@Column(name = "ID_ATRIBUTO")
	public long id;
	
	@ManyToOne
	@JoinColumn(name="ID_TIPO_ATRIBUTO")
	public SrTipoAtributo tipo;
	
	

}
