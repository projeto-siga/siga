package br.gov.jfrj.siga.tp.model;

public enum TipoRequisicao {

	NORMAL("NORMAL"), ESPECIAL("ESPECIAL") ;
	
	private String descricao;
	
	TipoRequisicao(String descricao){
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
