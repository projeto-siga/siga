package models;

public enum SrTipoAcao {

	INCIDENTE(0, "Incidente"), REQUISICAO(1, "Requisição");

	public long idTipoAcao;
	public String descrTipoAcao;

	SrTipoAcao(int id, String descricao) {
		this.idTipoAcao = id;
		this.descrTipoAcao = descricao;
	}
}
