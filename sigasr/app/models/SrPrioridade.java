package models;

public enum SrPrioridade {

	IMEDIATO(5, "Imediata"), ALTO(4, "Alta"), MEDIO(3, "Média"), 
	BAIXO(2, "Baixa"), PLANEJADO(1, "Planejada");
	
	public int idPrioridade;
	public String descPrioridade;
	
	private SrPrioridade(int idPrioridade, String descPrioridade) {
		this.idPrioridade = idPrioridade;
		this.descPrioridade = descPrioridade;
	}
	
}
