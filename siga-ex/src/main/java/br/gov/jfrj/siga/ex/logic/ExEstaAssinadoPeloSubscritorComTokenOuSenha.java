package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaAssinadoPeloSubscritorComTokenOuSenha implements Expression {
	ExDocumento doc;

	public ExEstaAssinadoPeloSubscritorComTokenOuSenha(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isAssinadoPeloSubscritorComTokenOuSenha();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está assinado pelo subscritor com token ou senha", result);
	}

}
