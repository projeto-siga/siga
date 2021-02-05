package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoECadastrante implements Expression {
	ExMovimentacao mov;
	DpPessoa pessoa;

	public ExMovimentacaoECadastrante(ExMovimentacao mov, DpPessoa pessoa) {
		this.mov = mov;
		this.pessoa = pessoa;
	}

	@Override
	public boolean eval() {
		return pessoa != null && mov.getCadastrante() != null && mov.getCadastrante().equivale(pessoa);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é cadastrante", result);
	}

}
