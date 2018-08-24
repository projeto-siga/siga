package br.gov.jfrj.siga.tp.util;

import java.util.Calendar;

import br.gov.jfrj.siga.tp.model.DiaDaSemana;

public class PeriodoDeTrabalho {
	public Long id;
	public DiaDaSemana dia;
	public Calendar hora;
	
	public PeriodoDeTrabalho(Long id, DiaDaSemana dia, Calendar hora) {
		this.id = id;
		this.dia = dia;
		this.hora = hora;
	}
	
}
