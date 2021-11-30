package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEExpediente implements Expression {
	ExDocumento doc;

	public ExEExpediente(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isExpediente();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é expediente", result);
	}

}
