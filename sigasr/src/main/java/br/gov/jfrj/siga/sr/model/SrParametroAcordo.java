package br.gov.jfrj.siga.sr.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
import br.gov.jfrj.siga.sr.model.vo.SrParametroAcordoVO;
import br.gov.jfrj.siga.sr.vraptor.AtributoController;

@Entity
@Table(name = "sr_atributo_acordo", schema = "sigasr")
public class SrParametroAcordo extends HistoricoSuporte {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR" + ".SR_ATRIBUTO_ACORDO_SEQ", name = "SrAtributoAcordoSeq")
	@GeneratedValue(generator = "SrAtributoAcordoSeq")
	@Column(name = "ID_ATRIBUTO_ACORDO")
	private Long idParametroAcordo;

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

	@Enumerated
	private SrParametro parametro;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private SrParametroAcordo parametroAcordoInicial;

	@OneToMany(targetEntity = SrParametroAcordo.class, mappedBy = "parametroAcordoInicial")
	@OrderBy("hisDtIni desc")
	private List<SrParametroAcordo> meuParametroAcordoHistoricoSet;

	@Override
	public Long getId() {
		return this.getIdParametroAcordo();
	}

	public void setId(Long id) {
		setIdParametroAcordo(id);
	}

	public String getDescricao() {
		return acordo.getNomeAcordo() + " - " + parametro.getDescricao() + " "
				+ operador.getNome() + " " + valor + " " + unidadeMedida.getPlural();
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public List<SrParametroAcordo> getHistoricoParametroAcordo() {
		if (getParametroAcordoInicial() != null)
			return getParametroAcordoInicial()
					.getMeuParametroAcordoHistoricoSet();
		return null;
	}

	public SrParametroAcordo getParametroAcordoAtual() {
		List<SrParametroAcordo> paramAcordos = getHistoricoParametroAcordo();
		if (paramAcordos == null)
			return null;
		return paramAcordos.get(0);
	}

	public SrSituacaoAcordo getSituacaoParaOValor(SrValor valor) {
		if (valor == null)
			return SrSituacaoAcordo.NAO_SE_APLICA;
		if (isNaFaixa(valor))
			return SrSituacaoAcordo.OK;
		return SrSituacaoAcordo.NAO_CUMPRIDO;
	}

	public boolean isNaFaixa(SrValor v) {
		SrValor valorParam = getValorEUnidade();
		if (getOperador() == SrOperador.IGUAL)
			return valorParam.compareTo(v) == 0;
		else if (getOperador() == SrOperador.MENOR)
			return valorParam.compareTo(v) > 0;
		else if (getOperador() == SrOperador.MENOR_OU_IGUAL)
			return valorParam.compareTo(v) >= 0;
		else if (getOperador() == SrOperador.MAIOR)
			return valorParam.compareTo(v) < 0;
		else if (getOperador() == SrOperador.MAIOR_OU_IGUAL)
			return valorParam.compareTo(v) <= 0;
		return false;
	}

	public SrValor getValorEUnidade() {
		return new SrValor(getValor(),
				getUnidadeMedida() != null ? getUnidadeMedida()
						.getIdUnidadeMedida().intValue() : null);
	}

	public Long getValorEmSegundos() {
		return getValorEUnidade().getValorEmSegundos();
	}
	
	public Long getValorEmMilissegundos() {
		return getValorEmSegundos() * 1000;
	}

	public SrParametroAcordoVO toVO() throws Exception {
		return SrParametroAcordoVO.createFrom(this);
	}

	public Long getValor() {
		return valor;
	}

	public void setValor(Long valor) {
		this.valor = valor;
	}

	public Long getIdParametroAcordo() {
		return idParametroAcordo;
	}

	public void setIdParametroAcordo(Long idParametroAcordo) {
		this.idParametroAcordo = idParametroAcordo;
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

	public SrParametro getParametro() {
		return parametro;
	}

	public void setParametro(SrParametro parametro) {
		this.parametro = parametro;
	}

	public SrParametroAcordo getParametroAcordoInicial() {
		return parametroAcordoInicial;
	}

	public void setParametroAcordoInicial(
			SrParametroAcordo parametroAcordoInicial) {
		this.parametroAcordoInicial = parametroAcordoInicial;
	}

	public List<SrParametroAcordo> getMeuParametroAcordoHistoricoSet() {
		return meuParametroAcordoHistoricoSet;
	}

	public void setMeuParametroAcordoHistoricoSet(
			List<SrParametroAcordo> meuAtributoAcordoHistoricoSet) {
		this.meuParametroAcordoHistoricoSet = meuAtributoAcordoHistoricoSet;
	}
}
