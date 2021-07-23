package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.model.ResponsibleKind;

public enum WfTipoDeVinculoComPrincipal implements ResponsibleKind {
	//
	OPCIONAL("Opcional"),
	//
	OPCIONAL_E_EXCLUSIVO("Opcional e Exclusivo"),
	//
	OBRIGATORIO("Obrigatório"),
	//
	OBRIGATORIO_E_EXCLUSIVO("Obrigatório e Exclusivo");

	private final String descr;

	WfTipoDeVinculoComPrincipal(String descr) {
		this.descr = descr;
	}

	@Override
	public String getDescr() {
		return this.descr;
	}
}
