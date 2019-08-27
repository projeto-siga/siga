package br.gov.jfrj.siga.sr.model;

import java.util.Date;

import br.gov.jfrj.siga.sr.util.SrDataUtil;
import br.gov.jfrj.siga.sr.util.SrViewUtil;

public abstract class SrIntervaloCorrente extends SrIntervalo {

	public SrIntervaloCorrente(){
		
	}
	
	public SrIntervaloCorrente(Date dtIni, Date dtFim, String descr) {
		super(dtIni, dtFim, descr);
	}
	
	public abstract Long getDecorridoMillis(boolean isPrevisao);
	
	public boolean isAtivo(){
		return isAtivo(new Date());
	}
	
	public boolean isDiaUtil(Date data) {
		return !SrDataUtil.isFinalDeSemana(data) && !SrDataUtil.isFeriado(data);
	}
		
	public boolean isMesmoDia(Date data, Date oData) {
		return SrViewUtil.toDDMMYYYY(data).equals(SrViewUtil.toDDMMYYYY(oData));
	} 
	
	public boolean isZeradoEParado() {
		return (getDecorridoEmSegundos() == 0) && !isAtivo();
	}
	public abstract boolean isAtivo(Date dt);
	
	public Long getDecorridoEmSegundos(){
		return segundos(getDecorridoMillis(false));
	}
		
	public float getDecorridoEmHoras() {
		return horas(getDecorridoEmSegundos());
	}
	
	public abstract Date getDataContandoDoInicio(Long millisAdiante);
	
	public boolean estaEntre(float tempoIni, float tempoFim) {
		float tempo = getDecorridoEmHoras();
		if (tempo == 0f)
			return true;
		return tempo > tempoIni && tempo <= tempoFim;
	}
}
