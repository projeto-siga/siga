package br.gov.jfrj.siga.tp.util;

public enum PerguntaSimNao {
	
	SIM("Sim"), NAO("N\u00E3o");
	
	private String resposta;
	
	PerguntaSimNao(String resposta){
		this.setDescricao(resposta);
	}

	private void setDescricao(String descricao) {
		this.resposta = descricao;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
	
	public String getDescricao() {
		return resposta;
	}
	
}
