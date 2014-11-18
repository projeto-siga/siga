package models;


public enum SrFormatoCampo {

	TEXTO(1, "Texto"), DATA(2, "Data"), NUM_INTEIRO(3, "Número Inteiro"), NUM_DECIMAL(4,
			"Número com duas casas decimais"), HORA(5,
			"Hora"), VL_PRE_DEFINIDO(6, "Valores pré-definidos");

	public int idFormatoCampo;

	public String descrFormatoCampo;

	private SrFormatoCampo(int nivel, String descrFormato) {
		this.idFormatoCampo = nivel;
		this.descrFormatoCampo = descrFormato;
	}
}
