package models;

public enum SrTipoTempo {
	
	HORAS(0, "Horas"), DIAS(1, "Dias");
	
	public long idTipoTempo;
	public String descTipoTempo;
	
	SrTipoTempo(int id, String descricao) {
		this.idTipoTempo = id;
		this.descTipoTempo = descricao;
	}
}
