package br.gov.jfrj.siga.sr.model;

public enum SrMeioComunicacao {

	TELEFONE(1, "Telefone"), EMAIL(2, "Email"), CONTATO_DIRETO(3, "Contato Direto"),
	PANDION(4, "Pandion"), CHAT(5, "Chat");

	private int idTipoContato;

	private String descrMeioComunicacao;

	private SrMeioComunicacao(int idTipoContato, String descrTipoContato) {
		this.idTipoContato = idTipoContato;
		this.descrMeioComunicacao = descrTipoContato;
	}

	/**
	 * @return the idTipoContato
	 */
	public int getIdTipoContato() {
		return idTipoContato;
	}

	/**
	 * @param idTipoContato the idTipoContato to set
	 */
	public void setIdTipoContato(int idTipoContato) {
		this.idTipoContato = idTipoContato;
	}

	/**
	 * @return the descrMeioComunicacao
	 */
	public String getDescrMeioComunicacao() {
		return descrMeioComunicacao;
	}

	/**
	 * @param descrMeioComunicacao the descrMeioComunicacao to set
	 */
	public void setDescrMeioComunicacao(String descrMeioComunicacao) {
		this.descrMeioComunicacao = descrMeioComunicacao;
	}

}
