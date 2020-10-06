package br.gov.jfrj.siga.ex;

public enum ExConfiguracaoDestinatarios {
	ORGAOS("Órgãos"),
	UNIDADES("Unidades"),
	CARGOS("Cargos"),
	FUNCOES("Funções"),
	PESSOAS("Pessoas");
	
	private String descricao;
	
	ExConfiguracaoDestinatarios(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}	

}
