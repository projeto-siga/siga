package br.gov.jfrj.siga.tp.model;

public enum TipoDeNotificacao {
	AUTUACAO("AUTUA\u00C7\u00C3O"), PENALIDADE("PENALIDADE");
	
	private String descricao;
	
	private TipoDeNotificacao(String descricao){
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
