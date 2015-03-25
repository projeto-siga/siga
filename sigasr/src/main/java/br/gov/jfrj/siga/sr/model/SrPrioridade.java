package br.gov.jfrj.siga.sr.model;

public enum SrPrioridade {

	IMEDIATO(5, "Imediata"), ALTO(4, "Alta"), MEDIO(3, "Média"), 
	BAIXO(2, "Baixa"), PLANEJADO(1, "Planejada");
	
	private int idPrioridade;
	private String descPrioridade;
	
	private SrPrioridade(int idPrioridade, String descPrioridade) {
		this.idPrioridade = idPrioridade;
		this.descPrioridade = descPrioridade;
	}

	/**
	 * @return the idPrioridade
	 */
	public int getIdPrioridade() {
		return idPrioridade;
	}

	/**
	 * @param idPrioridade the idPrioridade to set
	 */
	public void setIdPrioridade(int idPrioridade) {
		this.idPrioridade = idPrioridade;
	}

	/**
	 * @return the descPrioridade
	 */
	public String getDescPrioridade() {
		return descPrioridade;
	}

	/**
	 * @param descPrioridade the descPrioridade to set
	 */
	public void setDescPrioridade(String descPrioridade) {
		this.descPrioridade = descPrioridade;
	}
	
}
