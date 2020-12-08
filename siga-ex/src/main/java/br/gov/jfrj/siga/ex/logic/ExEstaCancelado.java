package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaCancelado implements Expression {
	ExDocumento doc;

	public ExEstaCancelado(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isCancelado();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está cancelado", result);
	}

}
