package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaSolicitadaPublicacaoNoDiario implements Expression {
	ExDocumento doc;

	public ExEstaSolicitadaPublicacaoNoDiario(ExDocumento doc) {
		this.doc = doc;
	}

	@Override
	public boolean eval() {
		return doc.isPublicacaoSolicitada();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está solicitado o agendamento da publicação no diário", result);
	}

}
