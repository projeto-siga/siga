package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExTemSubscritor implements Expression {
	ExDocumento doc;

	public ExTemSubscritor(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.getSubscritor() != null;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem subscritor", result);
	}

}
