package br.gov.jfrj.siga.tp.model;

public enum Gravidade {

	LEVE("LEVE",3), MEDIA("M\u00C9DIA",4), GRAVE("GRAVE",5), GRAVISSIMA("GRAV\u00CDSSIMA",7);
	
	private String descricao;
	private int pontuacao;
	
	Gravidade(String descricao, int pontuacao){
		this.setDescricao(descricao);
		this.setPontuacao(pontuacao);
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

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}
	
}
