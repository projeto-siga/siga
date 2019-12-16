package br.gov.jfrj.siga.vraptor;

public class SigaIdDescr {
	private Object id;
	private Object descr;

	public SigaIdDescr(Object id, Object descr) {
		this.id = id;
		this.descr = descr;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getDescr() {
		return descr;
	}

	public void setDescr(Object descr) {
		this.descr = descr;
	}
}
