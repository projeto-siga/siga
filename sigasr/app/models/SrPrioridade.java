package models;

public enum SrPrioridade {

	IMEDIATO(5, "Imediato"), ALTO(4, "Alto"), MEDIO(3, "Médio"), 
	BAIXO(2, "Baixo"), PLANEJADO(1, "Planejado");
	
	public int idPrioridade;
	public String descPrioridade;
	
	private SrPrioridade(int idPrioridade, String descPrioridade) {
		this.idPrioridade = idPrioridade;
		this.descPrioridade = descPrioridade;
	}
	
}
