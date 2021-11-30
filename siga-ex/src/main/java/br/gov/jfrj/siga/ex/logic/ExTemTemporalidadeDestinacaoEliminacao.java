package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExTemTemporalidadeDestinacaoEliminacao implements Expression {
	ExMobil mob;

	public ExTemTemporalidadeDestinacaoEliminacao(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isDestinacaoEliminacao();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("temporalidade com destinação de eliminacao", result);
	}

}
