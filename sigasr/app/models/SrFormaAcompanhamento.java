package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="SR_FORMA_ACOMPANHAMEMNTO")
public class SrFormaAcompanhamento extends GenericModel {
	
	@Id
	@GeneratedValue
	@Column(name = "ID_FORMA_ACOMPANHAMENTO")
	public long id;
	
	@Column(name="DESCR_FORMA_ACOMPANHAMENTO")
	public String descrFormaAcompanhamento;

	public SrFormaAcompanhamento(String descricao) {
		super();
		this.descrFormaAcompanhamento = descricao;
	}

}
