package br.gov.jfrj.siga.tp.model;

public enum NivelDeCombustivel {

	A("CHEIO"), B("7/8"), C("6/8"), D("5/8"), E("MEIO TANQUE"), F("3/8"), G("2/8"), H("1/8"), I("VAZIO") ;
	
	private String descricao;
	
	NivelDeCombustivel(String descricao){
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
