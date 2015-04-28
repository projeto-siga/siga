package br.gov.jfrj.siga.sr.model;


public enum SrFormaAcompanhamento {

	ABERTURA_ANDAMENTO(1, "Na abertura e a cada andamento"), ABERTURA_FECHAMENTO(2,
			"Na abertura e no fechamento"), ABERTURA(3, "Somente na abertura");

	public long idFormaAcompanhamento;
	public String descrFormaAcompanhamento;

	SrFormaAcompanhamento(int id, String descricao) {
		this.idFormaAcompanhamento = id;
		this.descrFormaAcompanhamento = descricao;
	}

}
