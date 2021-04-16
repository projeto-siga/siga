package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoHaSubscritor implements Expression {
	ExMovimentacao mov;

	public ExMovimentacaoHaSubscritor(ExMovimentacao mov) {
		this.mov = mov;
	}

	@Override
	public boolean eval() {
		return mov.getSubscritor() != null;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("há subscritor na movimentação", result);
	}

}
