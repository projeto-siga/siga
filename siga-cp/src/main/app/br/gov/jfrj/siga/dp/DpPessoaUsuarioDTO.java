package br.gov.jfrj.siga.dp;

public class DpPessoaUsuarioDTO {
	
	private Long id;
	private String nome;
	private String nomeLotacao;
	
	public DpPessoaUsuarioDTO(Long id, String nome, String nomeLotacao) {
		this.id = id;
		this.nome = nome;
		this.nomeLotacao = nomeLotacao;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNomeLotacao() {
		return this.nomeLotacao;
	}
	
	public void setNomeLotacao(String nomeLotacao) {
		this.nomeLotacao = nomeLotacao;
	}

}
