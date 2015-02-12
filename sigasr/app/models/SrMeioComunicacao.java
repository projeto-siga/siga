package models;

public enum SrMeioComunicacao {

	TELEFONE(1, "Telefone"), EMAIL(2, "Email"), CONTATO_DIRETO(3, "Contato Direto"),
	PANDION(4, "Mensageiro (Pandion, Lync, etc)"), CHAT(5, "Chat");

	public int idTipoContato;

	public String descrMeioComunicacao;

	private SrMeioComunicacao(int idTipoContato, String descrTipoContato) {
		this.idTipoContato = idTipoContato;
		this.descrMeioComunicacao = descrTipoContato;
	}

}
