package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaPublicadoNoDiario implements Expression {
	ExDocumento doc;

	public ExEstaPublicadoNoDiario(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isDJEPublicado();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está publicado no diário", result);
	}

}
