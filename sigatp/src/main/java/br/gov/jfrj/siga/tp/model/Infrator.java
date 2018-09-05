package br.gov.jfrj.siga.tp.model;

public enum Infrator {

	CONDUTOR("CONDUTOR"), PROPRIETARIO("PROPRIET\u00C1RIO"), PESSOA_FISICA("PESSOA F\u00CDSICA"), 
	PESSOA_FIS_JUR("PESSOA F\u00CDSICA/JUR\u00CDDICA"), TRANSPORTADOR("TRANSPORTADOR"), EXPEDIDOR("EXPEDIDOR");

	private String descricao;
	
	Infrator(String descricao){
		this.setDescricao(descricao);
	}

	private void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.name();
	}

}
