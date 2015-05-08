package br.gov.jfrj.siga.sr.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.util.Catalogs;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sr.model.vo.SrAtributoAcordoVO;

@Entity
@Table(name = "SR_ATRIBUTO_ACORDO", schema = Catalogs.SIGASR)
public class SrAtributoAcordo extends HistoricoSuporte {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ATRIBUTO_ACORDO_SEQ", name = "SrAtributoAcordoSeq")
	@GeneratedValue(generator = "SrAtributoAcordoSeq")
	@Column(name = "ID_ATRIBUTO_ACORDO")
	public Long idAtributoAcordo;

	@ManyToOne()
	@JoinColumn(name = "ID_ACORDO")
	public SrAcordo acordo;

	@Enumerated
	public SrOperador operador;

	@Column(name = "VALOR")
	public Long valor;

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
	//@OrderBy("hisDtIni desc")
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

	public boolean isNaFaixa(SrValor v) {
		SrValor valorAtributo = getValorEUnidade();
		if (operador == SrOperador.IGUAL)
			return valorAtributo.compareTo(v) == 0;
		else if (operador == SrOperador.MENOR)
			return valorAtributo.compareTo(v) > 0;
		else if (operador == SrOperador.MENOR_OU_IGUAL)
			return valorAtributo.compareTo(v) >= 0;
		else if (operador == SrOperador.MAIOR)
			return valorAtributo.compareTo(v) < 0;
		else if (operador == SrOperador.MAIOR_OU_IGUAL)
			return valorAtributo.compareTo(v) <= 0;
		return false;
	}

	public SrValor getValorEUnidade() {
		return new SrValor(valor, unidadeMedida != null ? unidadeMedida
				.getIdUnidadeMedida().intValue() : null);
	}
	
	public Long getValorEmSegundos(){
		return getValorEUnidade().getValorEmSegundos();
	}
	
	public SrAtributoAcordoVO toVO() {
		return SrAtributoAcordoVO.createFrom(this);
	}

}
