package br.gov.jfrj.siga.ex.api.v1;

enum TramitacaoTipoDestinoEnum {

	ORGAO("Código de Órgão", "^[A-Z]+-[A-Z0-9]+$", "Deve estar no formato 'OO-UUU', "
			+ "onde 'OO' (apenas letras maiúsculas, quantidade variável) corresponde ao código do órgão propriamente dito "
			+ "e 'UUU' (letras maiúsculas e dígitos, quantidade variável) correspondem ao código da unidade."),
	USUARIO("Código do Usuário", "^[A-Z]+\\d+$", "Deve estar no formato OONNNNN, "
			+ "onde 'OO' (apenas letras maiúsculas, quantidade variável) corresponde ao código do órgão propriamente dito "
			+ "e 'NNNN' (apenas dígitos, quantidade variável) à sua respectiva matrícula nesse órgão."),
	EXTERNO("Código de Órgão Externo", "^[A-Z]+$", "Código do órgão externo ao São Paulo Sem Papel");

	final String descricao;

	final String pattern;

	final String msgErroPattern;

	private TramitacaoTipoDestinoEnum(String descricao, String pattern, String msgErroPattern) {
		this.descricao = descricao;
		this.pattern = pattern;
		this.msgErroPattern = msgErroPattern;
	}

}
