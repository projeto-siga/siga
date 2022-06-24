package br.gov.jfrj.siga.cp.model.enm;

public enum CpMarcadorFinalidadeGrupoEnum {
	SISTEMA("Sistema"),
	//
	LISTA("Lista"),
	//
	GERAL("Geral"),
	//
	LOCAL("Local"),
	//
	PASTA("Pasta");

	private final String nome;

	private CpMarcadorFinalidadeGrupoEnum(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}