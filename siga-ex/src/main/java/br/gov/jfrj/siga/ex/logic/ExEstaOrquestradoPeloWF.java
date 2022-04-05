package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaOrquestradoPeloWF implements Expression {
	ExDocumento doc;

	public ExEstaOrquestradoPeloWF(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.getTipoDePrincipal() != null && doc.getPrincipal() != null;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está sendo orquestrado pelo " + (doc.getPrincipal() != null ? doc.getPrincipal() : "workflow"), result);
	}

}
