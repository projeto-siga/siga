package br.gov.jfrj.siga.sr.enumeration;

public enum SrUnidadeMedida {
	ANO(0L, "Ano"), 
	MES(1L, "Mes"), 
	DIA(2L, "Dia"), 
	HORA(3L, "Hora"), 
	MINUTO(4L, "Minuto"),
	SEGUNDO(5L, "Segundo");

	private Long idUnidadeMedida;
	private String nome;

	SrUnidadeMedida(Long idUnidadeMedida, String nome) {
		this.setIdUnidadeMedida(idUnidadeMedida);
		this.setNome(nome);
	}

	public Long getIdUnidadeMedida() {
		return idUnidadeMedida;
	}

	public void setIdUnidadeMedida(Long idUnidadeMedida) {
		this.idUnidadeMedida = idUnidadeMedida;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
