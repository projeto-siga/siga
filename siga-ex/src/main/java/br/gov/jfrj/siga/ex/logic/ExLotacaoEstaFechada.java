package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;

public class ExLotacaoEstaFechada implements Expression {

	private DpLotacao lotaTitular;

	public ExLotacaoEstaFechada(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		return lotaTitular.isFechada();
	}

	@Override
	public String explain(boolean result) {
		return "lotação " + lotaTitular.getSiglaCompleta() + (result ? "" : JLogic.NOT) + " está fechada";
	}
};
