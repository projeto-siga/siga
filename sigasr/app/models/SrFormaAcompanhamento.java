package models;


public enum SrFormaAcompanhamento {

	ANDAMENTO(1, "A cada andamento"), FECHAMENTO(2,
			"Quando o chamado for fechado"), NUNCA(3, "Não quero acompanhar");

	public long idFormaAcompanhamento;
	public String descrFormaAcompanhamento;

	SrFormaAcompanhamento(int id, String descricao) {
		this.idFormaAcompanhamento = id;
		this.descrFormaAcompanhamento = descricao;
	}

}
