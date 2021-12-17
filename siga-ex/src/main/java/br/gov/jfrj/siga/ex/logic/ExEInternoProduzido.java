package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEInternoProduzido implements Expression {
	ExDocumento doc;

	public ExEInternoProduzido(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isInternoProduzido();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é interno produzido", result);
	}

}
