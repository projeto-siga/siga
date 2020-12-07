package br.gov.jfrj.siga.cp.model.enm;

public enum TipoDeExibicaoDeMarca {
	IMEDIATA("Imediata"), DEPOIS_DE_EXPIRADO_O_PRAZO("Depois de expirado o prazo");

	String descr;

	TipoDeExibicaoDeMarca(String descr) {
		this.descr = descr;
	}

	String getDescr() {
		return this.descr;
	}
}
