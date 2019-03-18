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
	public Long getDecorridoMillis(boolean isPrevisao) {
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
				decorrido += it.getDecorrido(isPrevisao);
			}
			it.setInicio(getDtComHorarioInicio(SrDataUtil.addDia(dtAtual, 1)));
			dtAtual = it.getInicio();
			it.setFim(getDtComHorarioFim(dtAtual));
		}
		return decorrido;
	}

	public Long getDecorrido(boolean isPrevisao) { 
		if (!isPrevisao && isMesmoDia(new Date()) && isFuturo())
			return 0l;
		return getFimOuAgora().getTime() - getInicio().getTime();
	}

	@Override
	public Date getDataContandoDoInicio(Long millisAdiante) {
		//Edson: chamar o mecanismo acima para fazer previsões
		if (isInfinito() || millisAdiante <= this.getDecorridoMillis(false)) {
			Date dtPrevista, dtFinal = null; 
			SrIntervaloCorrente iPrevisto = null;
			int somaDia = 0; Long millisRestante = 0l;
			do {
				dtPrevista = getDataFimPrevista(millisAdiante, somaDia);
				iPrevisto = getIntervaloComDataPrevista(dtPrevista);
				millisRestante = millisAdiante - iPrevisto.getDecorridoMillis(true);
				dtFinal = new Date(dtPrevista.getTime() + millisRestante);
				somaDia = ajustarData(millisRestante, somaDia);
			} while (!isPrevisaoCerta(millisRestante, dtFinal)); 	
			return dtFinal;
		}	
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
	
	private boolean isPrevisaoCerta(Long millis, Date dt) {
		if (isAtivo(dt)) {
			if (horas(segundos(millis)) > getCargaHoraria())
				return false;
			return true;
		}
		return false;
	}
	
	public SrDefinicaoHorario getHorario() {
		return horario;
	}

	public void setHorario(SrDefinicaoHorario horario) {
		this.horario = horario;
	}
	
	public void setHorario(DpLotacao lotaAtendente) {
		if (lotaAtendente.getIdInicial() == 20937) //central 2r
			setHorario(SrDefinicaoHorario.HORARIO_CENTRAL);
		else if (lotaAtendente.getIdInicial() == 19753) //help desk
			setHorario(SrDefinicaoHorario.HORARIO_HELP_DESK);
		else if (lotaAtendente.getSiglaLotacao().startsWith("STI-SL")) //suporte local
			setHorario(SrDefinicaoHorario.HORARIO_SUPORTE_LOCAL);
		else if (lotaAtendente.getSiglaLotacao().startsWith("NTI-SL")) //suporte local ES
			setHorario(SrDefinicaoHorario.HORARIO_SUPORTE_LOCAL_ES);
		else 	//demais lotacoes
			setHorario(SrDefinicaoHorario.HORARIO_PADRAO);
	}
	
	public Long getCargaHoraria() {
		return (long) (getHorario().getHoraFinal() - getHorario().getHoraInicial()); 
	}
	
	public Date getDataInicioParaPrevisao() {
		if (isAtivo(getInicio()))
			return getInicio();
		if (getHorario().comecaDepoisDe(getInicio()))
			return getDtComHorarioInicio(getInicio());
		return getDtComHorarioInicio(SrDataUtil.addDia(getInicio(), 1));
	}
	
	public Date getDataFimPrevista(Long millisAdiante, int somaDia) {
		return SrDataUtil.addDia(getDataInicioParaPrevisao(), dias(millisAdiante).intValue() + somaDia);		
	}

	public Long dias(Long millis) {
		return (long) (horas(segundos(millis))/getCargaHoraria());
	}
		
	public SrIntervaloCorrente getIntervaloComDataPrevista(Date dtPrevista) {
		SrIntervaloEmAtendimento iPrevisto = new SrIntervaloEmAtendimento(getInicio(), dtPrevista, null);
		iPrevisto.setHorario(getHorario());
		return iPrevisto;
	}
	
	private int ajustarData(Long millis, int ajuste) {
		if (horas(segundos(millis)) > getCargaHoraria())
			return ajuste += dias(millis).intValue();
		return ajuste += 1;
	}
}
