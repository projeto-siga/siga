package br.gov.jfrj.siga.sr.model;

import java.util.Date;

public class SrIntervaloEmAtendimento extends SrIntervaloCorrente{

	// private DefinicaoHorario h; 
	
	//Edson: este método seria chamado por quem estivesse
	//criando este objeto, ou seja, pelo SrEtapa
	//public setDefinicaoHorario(DefinicaoHorario h){
	//	
	//}
	
	public SrIntervaloEmAtendimento(){
		
	}
	
	public SrIntervaloEmAtendimento(Date dtIni, Date dtFim, String descr) {
		super(dtIni, dtFim, descr);
	}
	
	@Override
	public Long getDecorridoMillis() {
		//Edson: Varrer os horários e também os feriados e 
		//retornar quanto tempo efetivo se passou
		if (isFuturo())
			return null;
		return getFimOuAgora().getTime() - getInicio().getTime();
	}

	@Override
	public Date getDataContandoDoInicio(Long millisAdiante) {
		//Edson: chamar o mecanismo acima para fazer previsões
		if (isInfinita() || millisAdiante <= getDecorridoMillis())
			return new Date(getInicio().getTime() + millisAdiante);
		return null;
	}

	@Override
	public boolean isAtivo() {
		//Edson: chamar o mecanismo acima para verificar se há feriado
		//no momento atual ou se está fora do período de trabalho
		return true;
	}

}
