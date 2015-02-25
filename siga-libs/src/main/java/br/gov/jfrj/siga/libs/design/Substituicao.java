package br.gov.jfrj.siga.libs.design;

public class Substituicao {
	public final static int SUBSTITUICAO_PESSOA = 1;
	public final static int SUBSTITUICAO_LOTACAO = 2;

	int tipo;
	String nome;
	Long id;

	public Substituicao(int tipo, String nome, Long id) {
		super();
		this.tipo = tipo;
		this.nome = nome;
		this.id = id;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
