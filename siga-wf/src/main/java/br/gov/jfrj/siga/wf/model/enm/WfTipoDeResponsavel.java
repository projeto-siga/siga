package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.model.ResponsibleKind;

public enum WfTipoDeResponsavel implements ResponsibleKind {
	CADASTRANTE("Cadastrante"),
	//
	LOTA_CADASTRANTE("Lotação do Cadastrante"),
	//
	TITULAR("Titular"),
	//
	LOTA_TITULAR("Lotação do Titular"),
	//
	SUBSCRITOR("Subscritor"),
	//
	LOTA_SUBSCRITOR("Lotação do Subscritor"),
	//
	DESTINATARIO("Destinatário"),
	//
	LOTA_DESTINATARIO("Lotação do Destinatário"),
	//
	TABELADO("Tabelado");

	private final String descr;

	WfTipoDeResponsavel(String descr) {
		this.descr = descr;
	}

	@Override
	public String getDescr() {
		return this.descr;
	}
}
