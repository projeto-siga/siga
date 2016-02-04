package br.gov.jfrj.siga.sr.model;

public enum SrTipoExecucaoAcao {
	
	ESCALONAMENTO(0, "Escalonamento"), EXECUCAO(1, "ExecuÃ§Ã£o"), TENTATIVA(2, "Tentativa");

	private long idTipoExecucaoAcao;
	private String descrTipoExecucaoAcao;
	
	SrTipoExecucaoAcao(int id, String descricao) {
		this.idTipoExecucaoAcao = id;
		this.descrTipoExecucaoAcao = descricao;
	}

	public long getIdTipoExecucaoAcao() {
		return idTipoExecucaoAcao;
	}

	public void setIdTipoExecucaoAcao(long idTipoExecucaoAcao) {
		this.idTipoExecucaoAcao = idTipoExecucaoAcao;
	}

	public String getDescrTipoExecucaoAcao() {
		return descrTipoExecucaoAcao;
	}

	public void setDescrTipoExecucaoAcao(String descrTipoExecucaoAcao) {
		this.descrTipoExecucaoAcao = descrTipoExecucaoAcao;
	}
}
