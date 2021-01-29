package br.gov.jfrj.siga.vraptor;

public class SigaIdStringDescrString {
	private String id;
	private String descr;

	public SigaIdStringDescrString(String id, String descr) {
		this.id = id;
		this.descr = descr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
}
