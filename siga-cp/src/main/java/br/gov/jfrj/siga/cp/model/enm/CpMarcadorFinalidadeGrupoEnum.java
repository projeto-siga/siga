package br.gov.jfrj.siga.cp.model.enm;

public enum CpMarcadorFinalidadeGrupoEnum {
	SISTEMA("Sistema"),
	//
	TARJA("Tarja"),
	//
	ETIQUETA("Etiqueta"),
	//
	PASTA("Pasta"),
	//
	LISTA("Lista");

	private final String nome;

	private CpMarcadorFinalidadeGrupoEnum(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}