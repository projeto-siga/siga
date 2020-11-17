package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoESubscritor implements Expression {
	ExMovimentacao mov;
	DpPessoa pessoa;

	public ExMovimentacaoESubscritor(ExMovimentacao mov, DpPessoa pessoa) {
		this.mov = mov;
		this.pessoa = pessoa;
	}

	@Override
	public boolean eval() {
		return pessoa != null && mov.getSubscritor() != null && mov.getSubscritor().equivale(pessoa);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é subscritor", result);
	}

}
