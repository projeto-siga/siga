package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExMovimentacaoELotaSubscritor implements Expression {
	ExMovimentacao mov;
	DpLotacao lotacao;

	public ExMovimentacaoELotaSubscritor(ExMovimentacao mov, DpLotacao lotacao) {
		this.mov = mov;
		this.lotacao = lotacao;
	}

	@Override
	public boolean eval() {
		return lotacao != null && mov.getLotaSubscritor() != null && mov.getLotaSubscritor().equivale(lotacao);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é da lotação do subscritor", result);
	}

}
