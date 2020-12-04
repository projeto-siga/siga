package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoEstaCancelada implements Expression {
	ExMovimentacao mov;

	public ExMovimentacaoEstaCancelada(ExMovimentacao mov) {
		this.mov = mov;
	}

	@Override
	public boolean eval() {
		return mov.isCancelada();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está cancelada a movimentação", result);
	}

}
