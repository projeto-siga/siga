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
@Table(name="SR_SERVICO")
public class SrServico extends GenericModel{
	
	@Id
	@Column(name = "ID_SERVICO")
	public long id;
	
	@Column(name="DESCRICAO")
	public String descricao;
	
	@ManyToOne
	@JoinColumn(name="ID_SERVICO_PAI")
	public SrServico servicoPai;
	
	public SrServico(String descricao) {
		this(descricao, null);
	}



	public SrServico(String descricao, SrServico servicoPai) {
		super();
		this.descricao = descricao;
		this.servicoPai = servicoPai;
	}	

}
