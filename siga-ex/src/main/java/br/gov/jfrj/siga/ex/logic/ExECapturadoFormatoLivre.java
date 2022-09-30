package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExECapturadoFormatoLivre implements Expression {
	ExDocumento doc;

	public ExECapturadoFormatoLivre(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isCapturadoFormatoLivre();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é externo capturado de formato livre", result);
	}

}
