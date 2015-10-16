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
		if (isInfinito() || millisAdiante <= getDecorridoMillis())
			return new Date(getInicio().getTime() + millisAdiante);
		return null;
	}

	@Override
	public boolean isAtivo(Date dt) {
		// TODO Auto-generated method stub
		return true;
	}

}
