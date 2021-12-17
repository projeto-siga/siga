package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEExterno implements Expression {
	ExDocumento doc;

	public ExEExterno(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isExterno();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é externo", result);
	}

}
