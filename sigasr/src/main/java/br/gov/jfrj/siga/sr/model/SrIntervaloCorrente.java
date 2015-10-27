package br.gov.jfrj.siga.sr.model;

import java.util.Date;

public abstract class SrIntervaloCorrente extends SrIntervalo {

	public SrIntervaloCorrente(){
		
	}
	
	public SrIntervaloCorrente(Date dtIni, Date dtFim, String descr) {
		super(dtIni, dtFim, descr);
	}
	
	public abstract Long getDecorridoMillis();
	
	public boolean isAtivo(){
		return isAtivo(new Date());
	}
	
	public abstract boolean isAtivo(Date dt);
	
	public Long getDecorridoEmSegundos(){
		return segundos(getDecorridoMillis());
	}
	
	public float getDecorridoEmHoras() {
		return horas(segundos(getDecorridoMillis()));
	}
	
	public abstract Date getDataContandoDoInicio(Long millisAdiante);
	
	public boolean estaEntre(float tempo, float tempoIni, float tempoFim) {
		if (tempo == 0f)
			return true;
		return tempo > tempoIni && tempo <= tempoFim;
	}
}
