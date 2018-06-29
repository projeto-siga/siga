package br.gov.jfrj.siga.tp.model;

public enum TiposDeServico {

	MANUTENCAOEXTERNA("MANUTEN\u00C7\u00C3O EXTERNA", true), 
	MANUTENCAOINTERNA("MANUTEN\u00C7\u00C3O INTERNA", false), 
	REVISAOSEMANAL("REVIS\u00C3O SEMANAL", false),
	CONSERVACAO("CONSERVA\u00C7\u00C3O", false), 
	VISTORIA("VISTORIA", true);
	
	private String descricao;
	private Boolean geraRequisicao;
	
	TiposDeServico(String descricao, Boolean geraRequisicao){
		this.setDescricao(descricao);
		this.setGeraRequisicao(geraRequisicao);
	}

	private void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	private void setGeraRequisicao(Boolean geraRequisicao) {
		this.geraRequisicao = geraRequisicao;
	}
	
	public Boolean getGeraRequisicao() {
		return this.geraRequisicao;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
}
