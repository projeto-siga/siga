package br.gov.jfrj.siga.ex;

public enum ExConfiguracaoNivelAcesso {
	
	ORGAOS("Órgãos"),
	UNIDADES("Unidades"),
	CARGOS("Cargos"),
	PESSOAS("Pessoas");
	
	private String descricao;
	
	ExConfiguracaoNivelAcesso(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}	

}
