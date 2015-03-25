package br.gov.jfrj.siga.sr.model;

public enum SrTipoAcao {

	INCIDENTE(0, "Incidente"), REQUISICAO(1, "Requisição");

	private long idTipoAcao;
	private String descrTipoAcao;

	SrTipoAcao(int id, String descricao) {
		this.idTipoAcao = id;
		this.descrTipoAcao = descricao;
	}

	public long getIdTipoAcao() {
		return idTipoAcao;
	}

	public void setIdTipoAcao(long idTipoAcao) {
		this.idTipoAcao = idTipoAcao;
	}

	public String getDescrTipoAcao() {
		return descrTipoAcao;
	}

	public void setDescrTipoAcao(String descrTipoAcao) {
		this.descrTipoAcao = descrTipoAcao;
	}
}
