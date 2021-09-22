package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoHaCadastrante implements Expression {
	ExMovimentacao mov;

	public ExMovimentacaoHaCadastrante(ExMovimentacao mov) {
		this.mov = mov;
	}

	@Override
	public boolean eval() {
		return mov.getCadastrante() != null;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("há cadastrante na movimentação", result);
	}

}
