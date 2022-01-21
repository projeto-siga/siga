package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaAgendadaPublicacaoNoDiario implements Expression {
	ExDocumento doc;

	public ExEstaAgendadaPublicacaoNoDiario(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isPublicacaoAgendada();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está agendada a publicação no diário", result);
	}

}
