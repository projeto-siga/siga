package br.gov.jfrj.siga.wf.model.enm;

public enum WfPrioridade {

	MUITO_ALTA("Muito Alta"),
	//
	ALTA("Alta"),
	//
	MEDIA("Média"),
	//
	BAIXA("Baixa"),
	//
	MUITO_BAIXA("Muito Baixa");

	private final String descr;

	WfPrioridade(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return this.descr;
	}
}
