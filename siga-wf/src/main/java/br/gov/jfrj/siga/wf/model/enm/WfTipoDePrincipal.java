package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.model.ResponsibleKind;

public enum WfTipoDePrincipal implements ResponsibleKind {
	DOC("Documento");

	private final String descr;

	WfTipoDePrincipal(String descr) {
		this.descr = descr;
	}

	@Override
	public String getDescr() {
		return this.descr;
	}
}
