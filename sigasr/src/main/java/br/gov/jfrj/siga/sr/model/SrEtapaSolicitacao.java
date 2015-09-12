package br.gov.jfrj.siga.sr.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.DpLotacao;

public class SrEtapaSolicitacao extends SrIntervaloCorrente implements SrParametroAcordoSolicitacao {

	//Edson: esta propriedade deveria estar em SrParametroAcordoSolicitacao
	private List<SrParametroAcordo> paramsAcordo; 
	
	private List<? extends SrIntervaloCorrente> intervalosCorrentes;
	
	private DpLotacao lotaResponsavel;
	
	private SrPrioridade prioridade;
	
	private SrItemConfiguracao item;
	
	private SrAcao acao;

	private SrParametro parametro;
	
	public SrEtapaSolicitacao(SrParametro etapa) {
		this.parametro = etapa;
		setDescricao(etapa.getTituloEtapa());
	}
	
	//Edson: métodos que deveriam estar em SrParmetroAcordoSolicitaocao
	@Override
	public boolean isAcordoSatisfeito(){
		SrSituacaoAcordo situ = getSituacaoAcordo();
		return situ == SrSituacaoAcordo.OK || situ == SrSituacaoAcordo.NAO_SE_APLICA;
	}
	
	@Override
	public SrSituacaoAcordo getSituacaoAcordo(){
		return getParamAcordo() != null ? getParamAcordo().getSituacaoParaOValor(getValorRealizado()) : SrSituacaoAcordo.NAO_SE_APLICA;
	}

	public SrParametroAcordo getParamAcordo() {
		if (paramsAcordo != null && paramsAcordo.size() > 0)
			return paramsAcordo.get(0);
		return null;
	}
	
	@Override
	public List<SrParametroAcordo> getParamsAcordo(){
		return paramsAcordo;
	}

	@Override
	public void setParamsAcordo(List<SrParametroAcordo> paramsAcordo) {
		this.paramsAcordo = paramsAcordo;
	}
	//Edson: FIM métodos que deveriam estar em SrParmetroAcordoSolicitaocao	
	
	@Override
	public SrValor getValorRealizado() {
		return new SrValor(getDecorridoEmSegundos(), CpUnidadeMedida.SEGUNDO);
	}
	
	public String getDescricaoComLotaResponsavel(){
		return getDescricao() + (lotaResponsavel != null ? " ("+ lotaResponsavel.getSiglaCompleta() +")" : "");
	}
	
	@Override
	public Long getDecorridoMillis(){
		long decorrido = 0L;
		for (SrIntervaloCorrente i : intervalosCorrentes){
			if (i.isFuturo())
				break;
			decorrido += i.getDecorridoMillis();
		}
		return decorrido;
	}
	
	public SrIntervaloCorrente getIntervaloCorrendoNaData(Date dt){
		for (SrIntervaloCorrente i : intervalosCorrentes)
			if (i.abrange(dt))
				return i;
		return null;
	}

	@Override
	public boolean isAtivo(Date dt) {
		SrIntervaloCorrente a = getIntervaloCorrendoNaData(dt);
		return a != null ? a.isAtivo() : false;
	}

	@Override
	public Date getDataContandoDoInicio(Long millisAdiante) {
		Iterator<? extends SrIntervaloCorrente> it = intervalosCorrentes.iterator();
		SrIntervaloCorrente i = null;
		while (it.hasNext()) {
			i = it.next();
			if (i.isFuturo())
				break;
			Date dt = i.getDataContandoDoInicio(millisAdiante);
			if (dt != null)
				return dt;
			millisAdiante -= i.getDecorridoMillis();
		}
		return null;
	}

	private Long getRestanteMillis() {
		if (getParamAcordo() != null){
			return getParamAcordo().getValorEmMilissegundos() - getDecorridoMillis();
		}
		return null;
	}

	public Long getRestanteEmSegundos() {
		return segundos(getRestanteMillis());
	}
	
	public Date getFimPrevisto() {
		if (getFim() == null && getParamAcordo() != null)
			return getDataContandoDoInicio(getParamAcordo().getValorEmMilissegundos());
		return null;
	}
	
	public String getFimPrevistoString() {
		return toStr(getFimPrevisto());
	}

	public List<? extends SrIntervaloCorrente> getIntervalosCorrentes() {
		return intervalosCorrentes;
	}

	public void setIntervalosCorrentes(List<? extends SrIntervaloCorrente> intervalos) {
		this.intervalosCorrentes = intervalos;
	}
	
	public DpLotacao getLotaResponsavel() {
		return lotaResponsavel;
	}

	public void setLotaResponsavel(DpLotacao lotaResponsavel) {
		this.lotaResponsavel = lotaResponsavel;
	}

	public SrParametro getParametro() {
		return parametro;
	}

	public void setParametro(SrParametro etapa) {
		this.parametro = etapa;
	}
	
	public SrPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(SrPrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public SrItemConfiguracao getItem() {
		return item;
	}

	public void setItem(SrItemConfiguracao item) {
		this.item = item;
	}

	public SrAcao getAcao() {
		return acao;
	}

	public void setAcao(SrAcao acao) {
		this.acao = acao;
	}

}
