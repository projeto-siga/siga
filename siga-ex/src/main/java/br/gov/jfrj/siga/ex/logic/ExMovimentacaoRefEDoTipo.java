package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoRefEDoTipo implements Expression {
	ExMovimentacao mov;
	long tipo;

	public ExMovimentacaoRefEDoTipo(ExMovimentacao mov, long tipo) {
		this.mov = mov;
		this.tipo = tipo;
	}

	@Override
	public boolean eval() {
		return mov.getExMovimentacaoRef() != null
				&& mov.getExMovimentacaoRef().getExTipoMovimentacao().getIdTpMov().equals(tipo);
	}

	@Override
	public String explain(boolean result) {
		return "movimentação referenciada " + JLogic.explain("é do tipo " + tipo, result);
	}

}
