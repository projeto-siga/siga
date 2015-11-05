package br.gov.jfrj.siga.sr.model;

import java.util.Calendar;
import java.util.Date;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.sr.util.SrDataUtil;

public class SrIntervaloEmAtendimento extends SrIntervaloCorrente{

	private SrDefinicaoHorario horario; 
	
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
		Date dtAtual = getDtComHorarioInicio(getInicio());
		Date dtFim = getFimOuAgora();
		Long decorrido = 0l;
		SrIntervaloEmAtendimento it = new SrIntervaloEmAtendimento(dtAtual, getDtComHorarioFim(dtAtual), null);
		while (dtAtual.before(dtFim)) {
			if (it.isDiaUtil() && !it.terminouAntesDe(dtAtual)) {
				if (it.abrange(getInicio()))
					it.setInicio(getInicio());
				if (it.isMesmoDia(dtFim)) 
					it.setFim(dtFim);		
				if (it.comecouDepoisDe(dtFim))
					it.setFim(it.getInicio());				
				decorrido += it.getDecorrido();
			}
			dtAtual = SrDataUtil.addDia(dtAtual, 1);
			it.setInicio(getDtComHorarioInicio(dtAtual));
			it.setFim(getDtComHorarioFim(dtAtual));
		}
		return decorrido;
	}

	public Long getDecorrido() { 
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
	
	public Date getDtComHorarioFim(Date d) {
		return SrDataUtil.getDataComHorario(d, getHorario().getHoraFinal(), 0, 0, 0);
	}
	
	public Date getDtComHorarioInicio(Date d) {
		return SrDataUtil.getDataComHorario(d, getHorario().getHoraInicial(), 0, 0, 0);
	}
		
	@Override
	public boolean isAtivo(Date dt) {
		//return true;
		return super.isDiaUtil(dt) && getHorario().abrange(dt);
	}
	
	public boolean isDiaUtil() {
		return super.isDiaUtil(getInicio());
	}
	
	public boolean isMesmoDia(Date dt) {
		return super.isMesmoDia(getInicio(), dt);
	}
	
	public SrDefinicaoHorario getHorario() {
		return horario;
	}

	public void setHorario(SrDefinicaoHorario horario) {
		this.horario = horario;
	}
		
	public void setHorario(DpLotacao lotaAtendente) {
		if (lotaAtendente.getIdInicial() == 20937 || //central 2r
				lotaAtendente.getIdInicial() == 19753)  //help desk
			setHorario(SrDefinicaoHorario.HORARIO_CENTRAL); 
		else if (lotaAtendente.getSigla().contains("STI-SL")) //suporte local
			setHorario(SrDefinicaoHorario.HORARIO_SUPORTE_LOCAL);
		else	//demais lotacoes
			setHorario(SrDefinicaoHorario.HORARIO_PADRAO);
	}
}
