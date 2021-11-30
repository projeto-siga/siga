package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExTemPDF implements Expression {
	ExDocumento doc;

	public ExTemPDF(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.hasPDF();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem PDF", result);
	}

}
