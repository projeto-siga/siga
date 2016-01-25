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
		Date dtAtual = getInicio();
		Date dtFim = isAtivo() ? getFimOuAgora() : getFimOuDtComHorarioFim();
		Long decorrido = 0l;
		SrIntervaloEmAtendimento it = new SrIntervaloEmAtendimento(getDtComHorarioInicio(dtAtual), getDtComHorarioFim(dtAtual), null);
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
			it.setInicio(getDtComHorarioInicio(SrDataUtil.addDia(dtAtual, 1)));
			dtAtual = it.getInicio();
			it.setFim(getDtComHorarioFim(dtAtual));
		}
		return decorrido;
	}

	public Long getDecorrido() { 
		if (isFuturo())
			return 0l;
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
	
	public Date getFimOuDtComHorarioFim() {
		return getFim() != null ? getFim() : getDtComHorarioFim(new Date());
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
		else if (lotaAtendente.getSigla().contains("STI-SL")
				|| lotaAtendente.getSigla().contains("NTI-SL")) //suporte local
			setHorario(SrDefinicaoHorario.HORARIO_SUPORTE_LOCAL);
		else	//demais lotacoes
			setHorario(SrDefinicaoHorario.HORARIO_PADRAO);
	}
}
