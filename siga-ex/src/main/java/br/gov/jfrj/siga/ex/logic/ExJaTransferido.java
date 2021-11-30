package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExJaTransferido implements Expression {
	ExDocumento doc;

	public ExJaTransferido(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.jaTransferido();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("sofreu trâmite", result);
	}

}
