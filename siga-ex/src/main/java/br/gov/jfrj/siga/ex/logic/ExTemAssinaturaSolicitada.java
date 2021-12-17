package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExTemAssinaturaSolicitada implements Expression {
	ExDocumento doc;

	public ExTemAssinaturaSolicitada(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isAssinaturaSolicitada();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem solicitação de assinatura", result);
	}

}
