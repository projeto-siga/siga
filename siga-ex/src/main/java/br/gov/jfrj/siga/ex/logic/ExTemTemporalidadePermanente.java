package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExTemTemporalidadePermanente implements Expression {
	ExMobil mob;

	public ExTemTemporalidadePermanente(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isDestinacaoGuardaPermanente();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("temporalidade com destinação ao arquivo permanente", result);
	}

}
