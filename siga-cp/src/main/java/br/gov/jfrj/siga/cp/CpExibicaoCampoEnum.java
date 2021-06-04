package br.gov.jfrj.siga.cp;

public enum CpExibicaoCampoEnum {
	OCULTO(0, "Oculto"), DESABILITADO(1, "Desabilitado"), OPCIONAL(2, "Opcional"), OBRIGATORIO(3, "Obrigatório");

	private final Integer id;
	private final String descricao;

	private CpExibicaoCampoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
}
