package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaSemEfeito implements Expression {
	ExDocumento doc;

	public ExEstaSemEfeito(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isSemEfeito();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está sem efeito", result);
	}

}
