package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros implements Expression {
	ExDocumento doc;

	public ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc != null && !doc.getAssinaturasEAutenticacoesComTokenOuSenhaERegistros().isEmpty();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está assinado ou autenticado com token ou senha e registros", result);
	}

}
