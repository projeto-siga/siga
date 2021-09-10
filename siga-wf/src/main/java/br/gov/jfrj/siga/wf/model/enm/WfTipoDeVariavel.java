package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.model.enm.VariableKind;

public enum WfTipoDeVariavel {

	STRING("Texto", VariableKind.STRING),
	//
	DOUBLE("Número", VariableKind.DOUBLE),
	//
	DATE("Data", VariableKind.DATE),
	//
	BOOLEAN("Booleano", VariableKind.BOOLEAN),
	//
	SELECAO("Seleção", VariableKind.STRING),
	//
	PESSOA("Pessoa", VariableKind.STRING),
	//
	LOTACAO("Lotação", VariableKind.STRING),
	//
	DOC_MOBIL("Documento", VariableKind.STRING);

	private final String descr;
	private final VariableKind kind;

	WfTipoDeVariavel(String descr, VariableKind kind) {
		this.descr = descr;
		this.kind = kind;
	}

	public String getDescr() {
		return this.descr;
	}

	public VariableKind getKind() {
		return this.kind;
	}
}
