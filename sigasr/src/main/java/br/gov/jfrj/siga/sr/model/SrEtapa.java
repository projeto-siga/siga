package br.gov.jfrj.siga.sr.model;

public enum SrEtapa {

	CADASTRO(1, "Cadastro"), PRIMEIRO_ATENDIMENTO(2, "Atendimento"), ATENDIMENTO(
			3, "Atendimento"), ULTIMO_ATENDIMENTO(2, "Atendimento"), ATENDIMENTO_GERAL(
			2, "Atendimento (Total)");

	private long idEtapa;
	private String descrEtapa;

	private SrEtapa(long idEtapa, String descrEtapa) {
		this.idEtapa = idEtapa;
		this.descrEtapa = descrEtapa;
	}

	public long getIdEtapa() {
		return idEtapa;
	}

	public void setIdEtapa(long idEtapa) {
		this.idEtapa = idEtapa;
	}

	public String getDescrEtapa() {
		return descrEtapa;
	}

	public void setDescrEtapa(String descrEtapa) {
		this.descrEtapa = descrEtapa;
	}

}
