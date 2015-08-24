package br.gov.jfrj.siga.sr.model;

import java.util.Date;

public abstract class SrIntervaloCorrente extends SrIntervalo {

	public SrIntervaloCorrente(){
		
	}
	
	public SrIntervaloCorrente(Date dtIni, Date dtFim, String descr) {
		super(dtIni, dtFim, descr);
	}
	
	public abstract Long getDecorridoMillis();
	
	public abstract boolean isAtivo();
	
	public Long getDecorridoEmSegundos(){
		return segundos(getDecorridoMillis());
	}
	
	public abstract Date getDataContandoDoInicio(Long millisAdiante);
	
}
