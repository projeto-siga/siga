package br.gov.jfrj.siga.ex.model.enm;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum ExTipoDePrincipal implements IEnumWithId {
	PROCEDIMENTO(1, "Procedimento");

	private final int id;
	private final String descr;

	ExTipoDePrincipal(int id, String descr) {
		this.id = id;
		this.descr = descr;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	public String getDescr() {
		return this.descr;
	}

}
