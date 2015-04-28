package br.gov.jfrj.siga.sr.model;


public enum SrTendencia {

	NAO_PIORA(1, "Sem tendência de piorar.",
			"Não vai piorar ou pode ate melhorar"), PIORA_LONGO_PRAZO(2,
			"Piora em longo prazo.",
			"Vai piorar em longo prazo"), PIORA_MEDIO_PRAZO(3,
			"Piora em médio prazo.",
			"Vai piorar em médio prazo"), PIORA_CURTO_PRAZO(4,
			"Piora em curto prazo.",
			"Vai piorar em curto prazo"), PIORA_IMEDIATA(5,
			"Piora imediata.",
			"Vai piorar imediatamente");

	public int nivelTendencia;

	public String descrTendencia;

	public String respostaEnunciado;

	SrTendencia(int nivel, String descricao) {
		this(nivel, descricao, descricao);
	}

	private SrTendencia(int nivel, String descrTendencia,
			String respostaEnunciado) {
		this.nivelTendencia = nivel;
		this.descrTendencia = descrTendencia;
		this.respostaEnunciado = respostaEnunciado;
	}

}
