package br.gov.jfrj.siga.sr.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;

public abstract class AbstractSrEtapa extends SrIntervaloCorrente implements SrParametroAcordoSolicitacao {

	//Edson: esta propriedade deveria estar em SrParametroAcordoSolicitacao
	protected SrParametroAcordo paramAcordo;
	
	private List<? extends SrIntervaloCorrente> intervalosCorrentes;
	
	public AbstractSrEtapa() {
		
	}
	
	//Edson: métodos que deveriam estar em SrParmetroAcordoSolicitaocao
	@Override
	public boolean isAcordoSatisfeito(){
		return paramAcordo.isSatisfeitoPeloValor(getValorRealizado());
	}
	
	@Override
	public SrSituacaoAcordo getSituacaoAcordo(){
		return paramAcordo.getSituacaoParaOValor(getValorRealizado());
	}

	@Override
	public SrParametroAcordo getParamAcordo() {
		return paramAcordo;
	}

	@Override
	public void setParamAcordo(SrParametroAcordo paramAcordo) {
		this.paramAcordo = paramAcordo;
	}
	//Edson: FIM métodos que deveriam estar em SrParmetroAcordoSolicitaocao	

	@Override
	public SrValor getValorRealizado() {
		return new SrValor(getDecorridoEmSegundos(), CpUnidadeMedida.SEGUNDO);
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
	public Date getDataContandoDoInicio(Long millisAdiante, boolean desconsiderarLimiteFim) {
		Iterator<? extends SrIntervaloCorrente> it = intervalosCorrentes.iterator();
		SrIntervaloCorrente i = null;
		while (it.hasNext() && millisAdiante > 0) {
			i = it.next();
			if (i.isFuturo())
				break;
			millisAdiante -= i.getDecorridoMillis();
		}
		return i.getDataContandoDoInicio(millisAdiante, desconsiderarLimiteFim);
	}

	private Long getRestanteMillis() {
		if (paramAcordo != null){
			return paramAcordo.getValorEmMilissegundos() - getDecorridoMillis();
		}
		return null;
	}

	public Long getRestanteEmSegundos() {
		return segundos(getRestanteMillis());
	}
	
	public Date getFimPrevisto() {
		//Edson: se esta etapa está terminada, perguntar qual o fim previsto significa
		//imaginar quando provavelmente finalizaria o último intervalo se esta etapa
		//não tivesse terminado. Isso é feito pelo parâmetro boolean abaixo
		if (paramAcordo != null)
			return getDataContandoDoInicio(paramAcordo.getValorEmMilissegundos(), !isInfinita());
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

}
