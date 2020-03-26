package br.gov.jfrj.siga.wf.model.enm;

import com.crivano.jflow.model.ResponsibleKind;

public enum WfTipoDeResponsavel implements ResponsibleKind {
	PRINCIPAL_CADASTRANTE("Principal: Cadastrante"),
	//
	PRINCIPAL_LOTA_CADASTRANTE("Principal: Lotação do Cadastrante"),
	//
	PRINCIPAL_TITULAR("Principal: Titular"),
	//
	PRINCIPAL_LOTA_TITULAR("Principal: Lotação do Titular"),
	//
	PRINCIPAL_SUBSCRITOR("Principal: Subscritor"),
	//
	PRINCIPAL_LOTA_SUBSCRITOR("Principal: Lotação do Subscritor"),
	//
	PRINCIPAL_DESTINATARIO("Principal: Destinatário"),
	//
	PRINCIPAL_LOTA_DESTINATARIO("Principal: Lotação do Destinatário"),
	//
	PRINCIPAL_GESTOR("Principal: Gestor"),
	//
	PRINCIPAL_LOTA_GESTOR("Principal: Lotação do Gestor"),
	//
	PRINCIPAL_FISCAL_TECNICO("Principal: Fiscal Técnico"),
	//
	PRINCIPAL_LOTA_FISCAL_TECNICO("Principal: Lotação do Fiscal Técnico"),
	//
	PRINCIPAL_FISCAL_ADMINISTRATIVO("Principal: Fiscal Administrativo"),
	//
	PRINCIPAL_LOTA_FISCAL_ADMINISTRATIVO("Principal: Lotação do Fiscal Administrativo"),
	//
	PRINCIPAL_INTERESSADO("Principal: Interessado"),
	//
	PRINCIPAL_LOTA_INTERESSADO("Principal: Lotação do Interessado"),
	//
	RESPONSAVEL("Cadastrado"),
	//
	PESSOA("Pessoa"),
	//
	LOTACAO("Lotação");

	private final String descr;

	WfTipoDeResponsavel(String descr) {
		this.descr = descr;
	}

	@Override
	public String getDescr() {
		return this.descr;
	}
}
