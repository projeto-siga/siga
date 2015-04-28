package br.gov.jfrj.siga.sr.model;


public enum SrGravidade {

	SEM_GRAVIDADE(1, "Sem gravidade.", "Sem gravidade"), NORMAL(2, "Gravidade normal.",
			"Pouco grave"), GRAVE(3, "Grave.", "Grave"), MUITO_GRAVE(4,
			"Muito grave.", "Muito grave"), EXTREMAMENTE_GRAVE(5,
			"Extremamente grave.", "Extremamente grave");

	public int nivelGravidade;

	public String descrGravidade;

	public String respostaEnunciado;

	SrGravidade(int nivel, String descricao) {
		this(nivel, descricao, descricao);
	}


	private SrGravidade(int nivel, String descrGravidade,
			String respostaEnunciado) {
		this.nivelGravidade = nivel;
		this.descrGravidade = descrGravidade;
		this.respostaEnunciado = respostaEnunciado;
	}

}
