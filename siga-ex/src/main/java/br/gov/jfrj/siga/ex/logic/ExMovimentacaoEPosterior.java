package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoEPosterior implements Expression {
	ExMovimentacao mov;
	ExMovimentacao mov2;

	public ExMovimentacaoEPosterior(ExMovimentacao mov, ExMovimentacao mov2) {
		this.mov = mov;
		this.mov2 = mov2;
	}

	@Override
	public boolean eval() {
		return mov != null && mov2 != null && mov.getDtMov() != null && mov2.getDtMov() != null
				&& mov.getDtMov().after(mov2.getDtMov());
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem movimentação posterior ", result);
	}

}
