package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ExEstaAgendadaPublicacaoNoDOE implements Expression {
	ExDocumento doc;
	DpPessoa titular;

	public ExEstaAgendadaPublicacaoNoDOE(ExDocumento doc, DpPessoa titular) {
		this.doc = doc;
		this.titular = titular;
	}

	@Override
	public boolean eval() {
		return doc.isPublicacaoAgendadaEhCadastranteDOE(titular);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está agendada a publicação no DOE", result);
	}

}
