package br.gov.jfrj.siga.sr.model;

public enum SrSituacaoAcordo {

	NAO_SE_APLICA(1, "Não se aplica"), OK(2, "Cumprido"), ALERTA(3, "Alerta"), NAO_CUMPRIDO(
			4, "Não Cumprido");

	private long idSituacaoAtributoAcordo;
	private String descrSituacaoAtributoAcordo;

	SrSituacaoAcordo(int id, String descricao) {
		this.setidSituacaoAtributoAcordo(id);
		this.setDescrSituacaoAtributoAcordo(descricao);
	}

	public long getidSituacaoAtributoAcordo() {
		return idSituacaoAtributoAcordo;
	}
	
	public long getId(){
		return getidSituacaoAtributoAcordo();
	}

	public void setidSituacaoAtributoAcordo(long idSituacaoAtributoAcordo) {
		this.idSituacaoAtributoAcordo = idSituacaoAtributoAcordo;
	}

	public String getDescrSituacaoAtributoAcordo() {
		return descrSituacaoAtributoAcordo;
	}

	public void setDescrSituacaoAtributoAcordo(
			String descrSituacaoAtributoAcordo) {
		this.descrSituacaoAtributoAcordo = descrSituacaoAtributoAcordo;
	}

	public boolean isSatisfatoria() {
		return (this == OK) ? true : false;
	}

}
