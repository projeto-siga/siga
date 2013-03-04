package models;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;
import models.siga.PlayHistoricoSuporte;

@Entity
@Table(name="SR_TIPO_ATRIBUTO")
public class SrTipoAtributo extends HistoricoSuporte {
	
	@Id
	@SequenceGenerator(sequenceName = "SR_TIPO_ATRIBUTO_SEQ", name = "SR_TIPO_ATRIBUTO_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SR_TIPO_ATRIBUTO_SEQ")
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
	
	public List<SrTipoAtributo> getHistoricoTipoAtributo() {
		if (getHisIdIni() == null)
			return null;
		return find(
				"from SrTipoAtributo where hisIdIni = " + getHisIdIni()
						+ " order by idTipoAtributo desc").fetch();
	}

	public SrTipoAtributo getAtual() {
		List<SrTipoAtributo> sols = getHistoricoTipoAtributo();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

}
