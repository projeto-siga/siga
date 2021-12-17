package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaAssinadoComSenha implements Expression {
	ExDocumento doc;

	public ExEstaAssinadoComSenha(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.getAssinaturasComSenha().size() > 0;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está assinado com senha", result);
	}

}
