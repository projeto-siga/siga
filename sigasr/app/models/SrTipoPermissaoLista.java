package models;

public enum SrTipoPermissaoLista {
	GESTAO(1, "Gestão"), PRIORIZACAO(2, "Priorização "), INCLUSAO(3, "Inclusão"), CONSULTA(4, "Consulta");

	public Long idTipoPermissaoLista;
	public String descrTipoPermissaoLista;
	
	SrTipoPermissaoLista(int id, String descricao) {
		this.idTipoPermissaoLista = Long.valueOf(id);
		this.descrTipoPermissaoLista = descricao;
	}
}
