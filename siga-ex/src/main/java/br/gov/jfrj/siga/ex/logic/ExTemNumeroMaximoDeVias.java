package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExTemNumeroMaximoDeVias implements Expression {
	ExDocumento doc;

	public ExTemNumeroMaximoDeVias(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.getNumUltimaVia() >= 21;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem o número máximo de vias", result);
	}

}
