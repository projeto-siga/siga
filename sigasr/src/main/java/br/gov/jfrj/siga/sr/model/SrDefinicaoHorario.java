package br.gov.jfrj.siga.sr.model;

import java.util.Calendar;
import java.util.Date;

import br.gov.jfrj.siga.sr.util.SrDataUtil;

public enum SrDefinicaoHorario{
	HORARIO_PADRAO(1, 11, 19, "Horário Padrão"), HORARIO_CENTRAL(2, 9, 21, "Horário da Central de Serviços"),
	HORARIO_SUPORTE_LOCAL(3, 10, 19, "Horário do Suporte Local"), HORARIO_HELP_DESK(4, 8, 20, "Horário do Help Desk da JFRJ"), 
	HORARIO_SUPORTE_LOCAL_ES(5, 9, 19, "Horário do Suporte Local da JFES");
	
	private int idHorario;
	private int horaInicial;
	private int horaFinal;
	private String descricao;
	
	private SrDefinicaoHorario(int idHorario, int horaInicial, int horaFinal,
			String descricao) {
		this.idHorario = idHorario;
		this.horaInicial = horaInicial;
		this.horaFinal = horaFinal;
		this.descricao = descricao;
	}

	public int getIdHorario() {
		return idHorario;
	}

	public void setIdHorario(int idHorario) {
		this.idHorario = idHorario;
	}

	public int getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(int horaInicial) {
		this.horaInicial = horaInicial;
	}

	public int getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(int horaFinal) {
		this.horaFinal = horaFinal;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}		
	
	public boolean terminaAntesDe(int hora, int minuto) {
		if (horaFinal == hora)
			return minuto > 0;
		return horaFinal < hora;
	}
	
	public boolean comecaDepoisDe(int tempo) {
		return horaInicial > tempo;
	}
	
	public boolean abrange(int hora, int minuto) {
		return !terminaAntesDe(hora, minuto) && !comecaDepoisDe(hora);
	}
	
	public boolean terminaAntesDe(Date dt) {
		return terminaAntesDe(SrDataUtil.getHora(dt), SrDataUtil.getMinuto(dt));
	}
	
	public boolean comecaDepoisDe(Date dt) {
		return comecaDepoisDe(SrDataUtil.getHora(dt));
	}
	
	public boolean abrange(Date dt) {
		return abrange(SrDataUtil.getHora(dt), SrDataUtil.getMinuto(dt));
	}
}