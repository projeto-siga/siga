package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoEstaAutenticada implements Expression {
	ExMovimentacao mov;

	public ExMovimentacaoEstaAutenticada(ExMovimentacao mov) {
		this.mov = mov;
	}

	@Override
	public boolean eval() {
		return mov.isAssinaturaComSenha();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está assinada com senha a movimentação", result);
	}

}
