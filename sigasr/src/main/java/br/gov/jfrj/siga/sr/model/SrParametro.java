package br.gov.jfrj.siga.sr.model;

public enum SrParametro{

	CADASTRO(1, "Cadastro", "Tempo de Cadastramento"), 
	PRIMEIRO_ATENDIMENTO(2, "Atendimento", "Tempo do Primeiro Escalonamento/Fechamento"), 
	ATENDIMENTO(3, "Atendimento", "Tempo de Atendimento"), 
	ULTIMO_ATENDIMENTO(4, "Atendimento", "Tempo do Último Atendimento"), 
	ATENDIMENTO_GERAL(5, "Atendimento (Total)", "Tempo Total de Atendimento");

	private long idEtapa;
	private String tituloEtapa;
	private String descrEtapa;

	private SrParametro(long idEtapa, String tituloEtapa, String descrEtapa) {
		this.idEtapa = idEtapa;
		this.tituloEtapa = tituloEtapa;
		this.descrEtapa = descrEtapa;
	}

	public long getIdEtapa() {
		return idEtapa;
	}

	public void setIdEtapa(long idEtapa) {
		this.idEtapa = idEtapa;
	}

	public String getTituloEtapa() {
		return tituloEtapa;
	}

	public void setTituloEtapa(String descrEtapa) {
		this.tituloEtapa = descrEtapa;
	}

	public String getDescrEtapa() {
		return descrEtapa;
	}

	public void setDescrEtapa(String descrEtapa) {
		this.descrEtapa = descrEtapa;
	}

	public Long getId() {
		return getIdEtapa();
	}

	public String getDescricao() {
		return getDescrEtapa();
	}

}
