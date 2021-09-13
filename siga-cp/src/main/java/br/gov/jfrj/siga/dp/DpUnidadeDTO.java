package br.gov.jfrj.siga.dp;

public class DpUnidadeDTO {
	
	private Long id;
	private String nome;
	private String sigla;
	private String nomeOrgao;
		
	private Long[] idOrgaoSelecao;
	
	public DpUnidadeDTO() {}
	
	public DpUnidadeDTO(Long id, String nome, String sigla, String nomeOrgao) {
		this.id = id;
		this.nome = nome;
		this.sigla = sigla;
		this.nomeOrgao = nomeOrgao;
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
	
	public String getSigla() {
		return this.sigla;
	}
	
	public void setSIgla(String sigla) {
		this.sigla = sigla;
	}
	
	public String getNomeOrgao() {
		return this.nomeOrgao;
	}
	
	public void setNomeOrgao(String nomeOrgao) {
		this.nomeOrgao = nomeOrgao;
	}
		
	public Long[] getIdOrgaoSelecao() {
		return idOrgaoSelecao;
	}

	public void setIdOrgaoSelecao(Long[] idOrgaoSelecao) {
		this.idOrgaoSelecao = idOrgaoSelecao;
	}

}
