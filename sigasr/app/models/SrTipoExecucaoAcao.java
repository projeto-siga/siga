package models;

public enum SrTipoExecucaoAcao {
	
	ESCALONAMENTO(0, "Escalonamento"), EXECUCAO(1, "Execução"), TENTATIVA(2, "Tentativa");

	public long idTipoExecucaoAcao;
	public String descrTipoExecucaoAcao;
	
	SrTipoExecucaoAcao(int id, String descricao) {
		this.idTipoExecucaoAcao = id;
		this.descrTipoExecucaoAcao = descricao;
	}
}
