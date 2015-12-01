package br.gov.jfrj.siga.sr.model;

import java.util.Calendar;
import java.util.Date;

public enum SrDefinicaoHorario{
	HORARIO_PADRAO(1, 11, 19, "Horário Padrão"), HORARIO_CENTRAL(2, 8, 20, "Horário da Central de Serviços"),
	HORARIO_SUPORTE_LOCAL(3, 10, 19, "Horário do Suporte Local");
	
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
	
	public boolean terminaAntesDe(int tempo) {
		return horaFinal <= tempo;
	}
	
	public boolean comecaDepoisDe(int tempo) {
		return horaInicial > tempo;
	}
	
	public boolean abrange(int tempo) {
		return !terminaAntesDe(tempo) && !comecaDepoisDe(tempo);
	}
	
	public boolean abrange(Date dt) {
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		int h = c.get(Calendar.HOUR_OF_DAY);
		return abrange(h);
	}
}