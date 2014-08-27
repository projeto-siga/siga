package models;


public enum SrGravidade {

	SEM_GRAVIDADE(1, "Sem gravidade."), NORMAL(2, "Gravidade normal.",
			"Pouco graves."), GRAVE(3, "Grave", "Graves"), MUITO_GRAVE(4,
			"Muito grave.", "Muito graves"), EXTREMAMENTE_GRAVE(5,
			"Extremamente grave.", "Extremamente graves");

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
