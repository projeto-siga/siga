package models;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_ATRIBUTO_ACORDO", schema = "SIGASR")
public class SrAtributoAcordo extends HistoricoSuporte {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ATRIBUTO_ACORDO_SEQ", name = "SrAtributoAcordoSeq")
	@GeneratedValue(generator = "SrAtributoAcordoSeq")
	@Column(name = "ID_ATRIBUTO_ACORDO")
	public Long idAtributoAcordo;

	@ManyToOne()
	@JoinColumn(name = "ID_ACORDO")
	public SrAcordo acordo;

	@Column(name = "VALOR")
	public String valor;

	@ManyToOne
	@JoinColumn(name = "UNIDADE_MEDIDA")
	public CpUnidadeMedida unidadeMedida;

	@ManyToOne()
	@JoinColumn(name = "ID_ATRIBUTO")
	public SrAtributo atributo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrAtributoAcordo atributoAcordoInicial;

	@OneToMany(targetEntity = SrAtributoAcordo.class, mappedBy = "atributoAcordoInicial")
	@OrderBy("hisDtIni desc")
	public List<SrAtributoAcordo> meuAtributoAcordoHistoricoSet;

	public Long getId() {
		return this.idAtributoAcordo;
	}

	public void setId(Long id) {
		idAtributoAcordo = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<SrAtributoAcordo> getHistoricoAtributoAcordo() {
		if (atributoAcordoInicial != null)
			return atributoAcordoInicial.meuAtributoAcordoHistoricoSet;
		return null;
	}

	public SrAtributoAcordo getAtributoAcordoAtual() {
		List<SrAtributoAcordo> atributoAcordos = getHistoricoAtributoAcordo();
		if (atributoAcordos == null)
			return null;
		return atributoAcordos.get(0);
	}
	
	public Integer getValorEmSegundos(){
		if (valor == null || unidadeMedida == null)
			return null;
		Integer valor = Integer.valueOf(this.valor);
		switch (unidadeMedida.getIdUnidadeMedida().intValue()) {
		case CpUnidadeMedida.ANO:
			valor *= 946080000;
			break;
		case CpUnidadeMedida.MES:
			valor *= 2592000;
			break;
		case CpUnidadeMedida.DIA:
			valor *= 86400;
			break;
		case CpUnidadeMedida.HORA:
			valor *= 3600;
			break;
		case CpUnidadeMedida.MINUTO:
			valor *= 60;
			break;
		case CpUnidadeMedida.SEGUNDO:
			valor *= 1;
			break;
		}
		return valor;
	}

}
