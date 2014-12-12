package models;

public enum SrOperador {
	MENOR("Menor que"), MENOR_OU_IGUAL("Menor ou Igual a"), IGUAL("Igual a"), MAIOR(
			"Maior que"), MAIOR_OU_IGUAL("Maior ou igual a");

	public String nome;

	SrOperador(String nome) {
		this.nome = nome;
	}
}
