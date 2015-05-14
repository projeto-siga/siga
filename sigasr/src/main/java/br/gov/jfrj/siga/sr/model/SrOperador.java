package br.gov.jfrj.siga.sr.model;

public enum SrOperador {
	MENOR(1, "Menor que"), 
	MENOR_OU_IGUAL(2, "Menor ou Igual a"), 
	IGUAL(3, "Igual a"), 
	MAIOR(4, "Maior que"), 
	MAIOR_OU_IGUAL(5, "Maior ou igual a");

	private int idOperador;
	private String nome;

	SrOperador(int idOperador, String nome) {
		this.setIdOperador(idOperador);
		this.setNome(nome);
	}

	public int getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(int idOperador) {
		this.idOperador = idOperador;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
