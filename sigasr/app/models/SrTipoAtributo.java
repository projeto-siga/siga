package models;

public enum SrTipoAtributo {

	TEXTO(1, "Texto"), 
	DATA(2, "Data"), 
	NUM_INTEIRO(3, "Número Inteiro"), 
	NUM_DECIMAL(4, "Número com duas casas decimais"), 
	HORA(5, "Hora"), 
	VL_PRE_DEFINIDO(6, "Valores pré-definidos"),
	TEXT_AREA(7, "TextArea");

	public int idTipoAtributo;

	public String descrTipoAtributo;

	private SrTipoAtributo(int idTipoAtributo, String descrTipoAtributo) {
		this.idTipoAtributo = idTipoAtributo;
		this.descrTipoAtributo = descrTipoAtributo;
	}
}
