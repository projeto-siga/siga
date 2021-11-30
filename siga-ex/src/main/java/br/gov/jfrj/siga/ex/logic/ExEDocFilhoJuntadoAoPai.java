package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEDocFilhoJuntadoAoPai implements Expression {
	ExDocumento doc;

	public ExEDocFilhoJuntadoAoPai(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isDocFilhoJuntadoAoPai();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é documento filho juntado ao pai", result);
	}

}
