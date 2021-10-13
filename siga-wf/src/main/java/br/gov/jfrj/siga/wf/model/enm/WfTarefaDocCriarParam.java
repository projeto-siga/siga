package br.gov.jfrj.siga.wf.model.enm;

public enum WfTarefaDocCriarParam {
	NAO_FINALIZAR("Não Finalizar"), FINALIZAR("Finalizar");

	private final String descr;

	WfTarefaDocCriarParam(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return this.descr;
	}
}
