package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEFisico implements Expression {
	ExDocumento doc;

	public ExEFisico(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isFisico();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é físico", result);
	}

}
