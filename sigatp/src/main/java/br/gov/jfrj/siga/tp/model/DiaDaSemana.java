package br.gov.jfrj.siga.tp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.gov.jfrj.siga.tp.util.Situacao;

public enum DiaDaSemana {
	
	DOMINGO	(0, "Domingo", "Domingo"),
	SEGUNDA	(1, "Segunda", "Segunda-feira"), 
	TERCA	(2, "Ter\u00E7a", "Ter\u00E7a-feira"), 
	QUARTA	(3, "Quarta", "Quarta-feira"), 
	QUINTA	(4, "Quinta", "Quinta-feira"), 
	SEXTA	(5, "Sexta", "Sexta-feira"), 
	SABADO	(6, "S\u00E1bado", "S\u00E1bado"); 

	
	private Integer ordem;
	private String nomeAbreviado;
	private String nomeCompleto;
	
	private DiaDaSemana(int ordem, String nomeAbreviado, String nomeCompleto) {
		this.ordem = ordem; 
		this.nomeAbreviado = nomeAbreviado;
		this.nomeCompleto = nomeCompleto;
	}
	
	public String getNomeAbreviado() {
		return nomeAbreviado;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}
	
	public Integer getOrdem() {
		return ordem;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
	public boolean isEquals(DiaDaSemana dia) {
		return this.getOrdem().equals(dia.getOrdem());
	}
	
	public static DiaDaSemana getDiaDaSemana(String data) throws ParseException {
		SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Calendar c = Calendar.getInstance();
		c.setTime(formatar.parse(data));
		int dia = c.get(Calendar.DAY_OF_WEEK);
		return DiaDaSemana.values()[dia-1];
	}
	
	public static DiaDaSemana getDiaDaSemana(Calendar data) throws ParseException {
		int dia = data.get(Calendar.DAY_OF_WEEK);
		return DiaDaSemana.values()[dia-1];
	}
	
	public DiaDaSemana[] getValues() {
		return DiaDaSemana.values();
	}
	
}
