package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEInternoCapturado implements Expression {
	ExDocumento doc;

	public ExEInternoCapturado(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isInternoCapturado();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é interno capturado", result);
	}

}
