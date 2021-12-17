package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEColaborativo implements Expression {
	ExDocumento doc;

	public ExEColaborativo(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isColaborativo();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é colaborativo", result);
	}

}
