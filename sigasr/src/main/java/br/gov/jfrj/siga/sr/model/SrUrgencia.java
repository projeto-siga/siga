package br.gov.jfrj.siga.sr.model;


public enum SrUrgencia {

	SEM_PRESSA(1, "Sem urgência.", "Sem pressa"), NORMAL(2,
			"Urgência normal.", "Quando for oportuno"), URGENCIA(
			3, "Urgente.", "Com urgência"), MUITA_URGENCIA(
			4, "Muito urgente.", "Com muita urgência"), AGIR_IMEDIATO(5,
			"Extremamente urgente.", "Imediatamente");

	public int nivelUrgencia;

	public String descrUrgencia;

	public String respostaEnunciado;

	private SrUrgencia(int nivelUrgencia, String descrUrgencia) {
		this(nivelUrgencia, descrUrgencia, descrUrgencia);
	}

	SrUrgencia(int nivel, String descricao, String respostaEnunciado) {
		this.nivelUrgencia = nivel;
		this.descrUrgencia = descricao;
		this.respostaEnunciado = respostaEnunciado;
	}

}
