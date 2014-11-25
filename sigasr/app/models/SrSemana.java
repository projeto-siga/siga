package models;

public enum SrSemana {
	DOMINGO(1, "Domingo"), SEGUNDA(2, "Segunda-Feira"), TERCA(3, "Terça-Feira"), 
	QUARTA(4, "Quarta-Feira"), QUINTA(5, "Quinta-feira"), SEXTA(6, "Sexta-Feira"), SABADO(7, "Sábado");
	
	public int idDiaSemana;
	public String descrDiaSemana;
	
	private SrSemana(int idDiaSemana, String descrDiaSemana) {
		this.idDiaSemana = idDiaSemana;
		this.descrDiaSemana = descrDiaSemana;
	}
}
