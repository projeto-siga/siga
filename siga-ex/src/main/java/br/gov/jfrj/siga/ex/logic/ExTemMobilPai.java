package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExTemMobilPai implements Expression {
	ExDocumento doc;

	public ExTemMobilPai(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc != null && doc.getExMobilPai() != null;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem móbil pai", result);
	}

}
