package br.gov.jfrj.siga.wf.model.enm;

public enum WfAcessoDeEdicao {

	ACESSO_PUBLICO("Público"),
	//
	ACESSO_ORGAO_USU("Restrito ao Órgão"),
	//
	ACESSO_LOTACAO_E_SUPERIORES("Lotação e Superiores"),
	//
	ACESSO_LOTACAO_E_INFERIORES("Lotação e Inferiores"),
	//
	ACESSO_LOTACAO("Lotação"),
	//
	ACESSO_PESSOAL("Pessoal"),
	//
	ACESSO_LOTACAO_E_GRUPO("Lotação e Grupo");

	private final String descr;

	WfAcessoDeEdicao(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return this.descr;
	}
}
