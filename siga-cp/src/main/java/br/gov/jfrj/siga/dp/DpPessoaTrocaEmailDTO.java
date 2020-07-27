package br.gov.jfrj.siga.dp;

public class DpPessoaTrocaEmailDTO {

	private String nome;
	private String orgao;
	private String cpf;
	private String email;
	private String matricula;
	private String lotacao;

	public DpPessoaTrocaEmailDTO() {
	};

	public DpPessoaTrocaEmailDTO(String nome, String orgao, String cpf, String email, String matricula, String lotacao) {
		this.nome = nome;
		this.orgao = orgao;
		this.cpf = cpf;
		this.email = email;
		this.matricula = matricula;
		this.lotacao = lotacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getOrgao() {
		return orgao;
	}

	public void setOrgao(String orgao) {
		this.orgao = orgao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getLotacao() {
		return lotacao;
	}

	public void setLotacao(String lotacao) {
		this.lotacao = lotacao;
	}

}
