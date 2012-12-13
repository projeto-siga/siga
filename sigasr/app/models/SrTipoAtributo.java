package models;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
public class SrTipoAtributo extends ObjetoPlayComHistorico {
	
	@Id
	@GeneratedValue
	@Column(name = "ID_TIPO_ATRIBUTO")
	public Long idTipoAtributo;
	
	@Column(name="NOME")
	public String nomeTipoAtributo;
	
	@Column(name="DESCRICAO")
	public String descrTipoAtributo;

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return idTipoAtributo;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		idTipoAtributo = id;
	}
	
	public static List<SrTipoAtributo> listar() {
		return SrTipoAtributo.find("byHisDtFimIsNull").fetch();
	}

}
