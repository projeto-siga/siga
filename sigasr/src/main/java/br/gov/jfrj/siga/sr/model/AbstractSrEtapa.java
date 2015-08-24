package br.gov.jfrj.siga.sr.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;

public abstract class AbstractSrEtapa extends SrIntervaloCorrente implements SrParametroAcordoSolicitacao {

	protected SrParametroAcordo paramAcordo;
	
	private List<? extends SrIntervaloCorrente> intervalos;
	
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
		for (SrIntervaloCorrente i : intervalos){
			if (i.isFuturo())
				break;
			decorrido += i.getDecorridoMillis();
		}
		return decorrido;
	}
	
	public SrIntervaloCorrente getIntervaloCorrendoNaData(Date dt){
		for (SrIntervaloCorrente i : intervalos)
			if (i.abrange(dt))
				return i;
		return null;
	}
		
	@Override
	public boolean isAtivo() {
		return isAtivo(new Date());
	}

	public boolean isAtivo(Date dt) {
		SrIntervalo a = getIntervaloCorrendoNaData(dt);
		return a != null;
	}

	@Override
	public Date getDataContandoDoInicio(Long millisAdiante) {
		Date possivelDtFim = null;
		Iterator<? extends SrIntervaloCorrente> it = intervalos.iterator();
		while (it.hasNext() && millisAdiante > 0) {
			SrIntervaloCorrente i = it.next();
			possivelDtFim = i.getDataContandoDoInicio(millisAdiante);
			millisAdiante -= i.getDecorridoMillis();
		}
		return possivelDtFim;
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
		if (paramAcordo != null)
			return getDataContandoDoInicio(paramAcordo.getValorEmMilissegundos());
		return null;
	}
	
	public String getFimPrevistoString() {
		return toStr(getFimPrevisto());
	}

	public List<? extends SrIntervaloCorrente> getIntervalos() {
		return intervalos;
	}

	public void setIntervalos(List<? extends SrIntervaloCorrente> intervalos) {
		this.intervalos = intervalos;
	}

}
