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
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sr.model.vo.SrAtributoAcordoVO;
import br.gov.jfrj.siga.vraptor.converter.ConvertableEntity;
import br.gov.jfrj.siga.vraptor.entity.HistoricoSuporteVraptor;

@Entity
@Table(name = "SR_ATRIBUTO_ACORDO", schema = Catalogs.SIGASR)
public class SrAtributoAcordo extends HistoricoSuporteVraptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = Catalogs.SIGASR +".SR_ATRIBUTO_ACORDO_SEQ", name = "SrAtributoAcordoSeq")
	@GeneratedValue(generator = "SrAtributoAcordoSeq")
	@Column(name = "ID_ATRIBUTO_ACORDO")
	private Long idAtributoAcordo;

	@ManyToOne()
	@JoinColumn(name = "ID_ACORDO")
	private SrAcordo acordo;

	@Enumerated
	private SrOperador operador;

	@Column(name = "VALOR")
	private Long valor;

	@ManyToOne
	@JoinColumn(name = "UNIDADE_MEDIDA")
	private CpUnidadeMedida unidadeMedida;

	@ManyToOne()
	@JoinColumn(name = "ID_ATRIBUTO")
	private SrAtributo atributo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private SrAtributoAcordo atributoAcordoInicial;

	@OneToMany(targetEntity = SrAtributoAcordo.class, mappedBy = "atributoAcordoInicial")
	//@OrderBy("hisDtIni desc")
	private	List<SrAtributoAcordo> meuAtributoAcordoHistoricoSet;

	public Long getId() {
		return this.getIdAtributoAcordo();
	}

	public void setId(Long id) {
		setIdAtributoAcordo(id);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<SrAtributoAcordo> getHistoricoAtributoAcordo() {
		if (getAtributoAcordoInicial() != null)
			return getAtributoAcordoInicial().getMeuAtributoAcordoHistoricoSet();
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
		if (getOperador() == SrOperador.IGUAL)
			return valorAtributo.compareTo(v) == 0;
		else if (getOperador() == SrOperador.MENOR)
			return valorAtributo.compareTo(v) > 0;
		else if (getOperador() == SrOperador.MENOR_OU_IGUAL)
			return valorAtributo.compareTo(v) >= 0;
		else if (getOperador() == SrOperador.MAIOR)
			return valorAtributo.compareTo(v) < 0;
		else if (getOperador() == SrOperador.MAIOR_OU_IGUAL)
			return valorAtributo.compareTo(v) <= 0;
		return false;
	}

	public SrValor getValorEUnidade() {
		return new SrValor(getValor(), getUnidadeMedida() != null ? getUnidadeMedida()
				.getIdUnidadeMedida().intValue() : null);
	}
	
	public Long getValorEmSegundos(){
		return getValorEUnidade().getValorEmSegundos();
	}
	
	public SrAtributoAcordoVO toVO() {
		return SrAtributoAcordoVO.createFrom(this);
	}

	public Long getValor() {
		return valor;
	}

	public void setValor(Long valor) {
		this.valor = valor;
	}

	public Long getIdAtributoAcordo() {
		return idAtributoAcordo;
	}

	public void setIdAtributoAcordo(Long idAtributoAcordo) {
		this.idAtributoAcordo = idAtributoAcordo;
	}

	public SrAcordo getAcordo() {
		return acordo;
	}

	public void setAcordo(SrAcordo acordo) {
		this.acordo = acordo;
	}

	public SrOperador getOperador() {
		return operador;
	}

	public void setOperador(SrOperador operador) {
		this.operador = operador;
	}

	public CpUnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(CpUnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public SrAtributo getAtributo() {
		return atributo;
	}

	public void setAtributo(SrAtributo atributo) {
		this.atributo = atributo;
	}

	public SrAtributoAcordo getAtributoAcordoInicial() {
		return atributoAcordoInicial;
	}

	public void setAtributoAcordoInicial(SrAtributoAcordo atributoAcordoInicial) {
		this.atributoAcordoInicial = atributoAcordoInicial;
	}

	public List<SrAtributoAcordo> getMeuAtributoAcordoHistoricoSet() {
		return meuAtributoAcordoHistoricoSet;
	}

	public void setMeuAtributoAcordoHistoricoSet(
			List<SrAtributoAcordo> meuAtributoAcordoHistoricoSet) {
		this.meuAtributoAcordoHistoricoSet = meuAtributoAcordoHistoricoSet;
	}
}
