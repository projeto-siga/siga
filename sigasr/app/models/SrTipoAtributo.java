package models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_TIPO_ATRIBUTO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrTipoAtributo extends HistoricoSuporte {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_TIPO_ATRIBUTO_SEQ", name = "srTipoAtributoSeq")
	@GeneratedValue(generator = "srTipoAtributoSeq")
	@Column(name = "ID_TIPO_ATRIBUTO")
	public Long idTipoAtributo;

	@Column(name = "NOME")
	public String nomeTipoAtributo;

	@Column(name = "DESCRICAO")
	public String descrTipoAtributo;
	
	@Enumerated
	public SrFormatoCampo formatoCampo;
	
	@Column(name = "DESCR_PRE_DEFINIDO")
	public String descrPreDefinido;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrTipoAtributo tipoAtributoInicial;

	@OneToMany(targetEntity = SrTipoAtributo.class, mappedBy = "tipoAtributoInicial")
	@OrderBy("hisDtIni desc")
	public List<SrTipoAtributo> meuTipoAtributoHistoricoSet;

	@Override
	public Long getId() {
		return idTipoAtributo;
	}

	@Override
	public void setId(Long id) {
		idTipoAtributo = id;
	}

	public static List<SrTipoAtributo> listar(boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();

		if (!mostrarDesativados) {
			queryBuilder.append(" hisDtFim is null");
		} else {
			queryBuilder.append("SELECT ta FROM SrTipoAtributo ta ");
			queryBuilder.append("WHERE ta.idTipoAtributo in (SELECT MAX(idTipoAtributo) FROM SrTipoAtributo GROUP BY hisIdIni) ");
		}
		return SrTipoAtributo.find(queryBuilder.toString()).fetch();
	}

	public List<SrTipoAtributo> getHistoricoTipoAtributo() {
		if (tipoAtributoInicial != null)
			return tipoAtributoInicial.meuTipoAtributoHistoricoSet;
		return null;
	}

	public SrTipoAtributo getAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrTipoAtributo> sols = getHistoricoTipoAtributo();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}
	
	public Set<String> getPreDefinidoSet() {
		Set<String> preDefinidos = new HashSet<String>();
		if (formatoCampo == SrFormatoCampo.VL_PRE_DEFINIDO){
			preDefinidos.addAll(Arrays.asList(descrPreDefinido.split(";"))); 
		}
		return preDefinidos;
	}

}
