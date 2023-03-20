package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEComposto implements Expression {
	ExDocumento doc;

	public ExEComposto(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isComposto();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é composto", result);
	}

}
