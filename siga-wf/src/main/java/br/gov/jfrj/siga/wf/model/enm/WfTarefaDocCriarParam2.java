package br.gov.jfrj.siga.wf.model.enm;

public enum WfTarefaDocCriarParam2 {
	NAO_AGUARDAR("Não Aguardar"), AGUARDAR_ASSINATURA("Aguardar Assinatura"), AGUARDAR_JUNTADA("Aguardar Juntada ao Principal");

	private final String descr;

	WfTarefaDocCriarParam2(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return this.descr;
	}
}
