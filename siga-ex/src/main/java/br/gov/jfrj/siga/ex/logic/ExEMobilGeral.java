package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEMobilGeral implements Expression {
	ExMobil mob;

	public ExEMobilGeral(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isGeral();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é móbil geral", result);
	}

}
