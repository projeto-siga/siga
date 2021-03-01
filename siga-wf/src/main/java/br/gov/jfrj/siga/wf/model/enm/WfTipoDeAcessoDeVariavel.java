package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.model.enm.VariableEditingKind;

public enum WfTipoDeAcessoDeVariavel {

	READ_WRITE_REQUIRED("Obrigatória", VariableEditingKind.READ_WRITE_REQUIRED),
	//
	READ_WRITE("Editável", VariableEditingKind.READ_WRITE),
	//
	READ_ONLY("Visível", VariableEditingKind.READ_ONLY);

	private final String descr;
	private final VariableEditingKind kind;

	WfTipoDeAcessoDeVariavel(String descr, VariableEditingKind kind) {
		this.descr = descr;
		this.kind = kind;
	}

	public String getDescr() {
		return this.descr;
	}

	public VariableEditingKind getKind() {
		return this.kind;
	}
}
