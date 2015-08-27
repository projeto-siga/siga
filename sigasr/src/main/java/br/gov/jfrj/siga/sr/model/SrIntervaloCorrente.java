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
	
	public abstract Date getDataContandoDoInicio(Long millisAdiante);
	
}
