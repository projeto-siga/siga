package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.model.ResponsibleKind;

public enum WfTipoDeDesignacao implements ResponsibleKind {
	UNICA("Somente nesta tarefa"),
	//
	SEMPRE("Sempre nesta tarefa"),
	//
	TODAS("Em todas as tarefas");

	private final String descr;

	WfTipoDeDesignacao(String descr) {
		this.descr = descr;
	}

	@Override
	public String getDescr() {
		return this.descr;
	}
}
