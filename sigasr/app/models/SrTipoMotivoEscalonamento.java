package models;


public enum SrTipoMotivoEscalonamento {

	ERRO_ESCALONAMENTO(1, "Erro de escalonamento"),
	NOVO_ATENDENTE(2, "Escalonar solicitação para novo atendente");

	public int nivelTipoMotivoEscalonamento;
	public String descrTipoMotivoEscalonamento;

	SrTipoMotivoEscalonamento(int nivel, String descricao) {
		this.nivelTipoMotivoEscalonamento = nivel;
		this.descrTipoMotivoEscalonamento = descricao;
	}

}
