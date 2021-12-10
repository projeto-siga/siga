package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoEDoTipo implements Expression {
	ExMovimentacao mov;
	ITipoDeMovimentacao tipo;

	public ExMovimentacaoEDoTipo(ExMovimentacao mov, ITipoDeMovimentacao tipo) {
		this.mov = mov;
		this.tipo = tipo;
	}

	@Override
	public boolean eval() {
		return mov != null && mov.getExTipoMovimentacao() == tipo;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é do tipo " + tipo, result);
	}

}
