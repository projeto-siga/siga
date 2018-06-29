package br.gov.jfrj.siga.tp.model;

public enum RamoDeAtividade{

	COMBUSTIVEL("COMBUST\u00CDVEL"), MANUTENCAO("MANUTEN\u00C7\u00C3O"), 
	VEICULOS("VE\u00CDCULOS"), PECAS("PE\u00C7AS"), PNEUS("PNEUS"),
	BATERIAS("BATERIAS"), MATERIALDELIMPEZA("MATERIAL DE LIMPEZA"),
	CONSERVACAO("CONSERVA\u00C7\u00C3O");
	
	private String descricao;
	
	RamoDeAtividade(String descricao){
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
