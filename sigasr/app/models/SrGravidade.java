package models;


public enum SrGravidade {

	SEM_GRAVIDADE(1, "Sem gravidade", ""), NORMAL(2, "Solicitante impossibilitado de realizar o trabalho",
			""), GRAVE(3, "Serviço fora do ar", ""), MUITO_GRAVE(4,
			"Evento em andamento (audiência, etc) ou solicitante prioritário", ""), EXTREMAMENTE_GRAVE(5,
			"Extremamente grave", "");

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
