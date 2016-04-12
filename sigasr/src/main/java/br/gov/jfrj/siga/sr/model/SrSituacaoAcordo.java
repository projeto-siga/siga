package br.gov.jfrj.siga.sr.model;

public enum SrSituacaoAcordo {

	NAO_SE_APLICA(1, "Não se aplica"), OK(2, "Cumprido"), ALERTA(3, "Alerta"), NAO_CUMPRIDO(
			4, "Não Cumprido");

	private long idSituacaoAcordo;
	private String descrSituacaoAcordo;

	SrSituacaoAcordo(int id, String descricao) {
		this.setidSituacaoAcordo(id);
		this.setDescrSituacaoAcordo(descricao);
	}

	public long getidSituacaoAcordo() {
		return idSituacaoAcordo;
	}
	
	public long getId(){
		return getidSituacaoAcordo();
	}

	public void setidSituacaoAcordo(long idSituacaoAcordo) {
		this.idSituacaoAcordo = idSituacaoAcordo;
	}

	public String getDescrSituacaoAcordo() {
		return descrSituacaoAcordo;
	}

	public void setDescrSituacaoAcordo(
			String descrSituacaoAcordo) {
		this.descrSituacaoAcordo = descrSituacaoAcordo;
	}

	public boolean isSatisfatoria() {
		return (this == OK) ? true : false;
	}

}
