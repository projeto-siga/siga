package br.gov.jfrj.siga.sr.model;

public enum SrOperador {
	MENOR("Menor que"), MENOR_OU_IGUAL("Menor ou Igual a"), IGUAL("Igual a"), MAIOR(
			"Maior que"), MAIOR_OU_IGUAL("Maior ou igual a");

	private String nome;

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	SrOperador(String nome) {
		this.nome = nome;
	}
}
