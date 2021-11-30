package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExECapturado implements Expression {
	ExDocumento doc;

	public ExECapturado(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isCapturado();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é capturado", result);
	}

}
