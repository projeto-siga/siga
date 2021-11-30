package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEMobilVia implements Expression {
	ExMobil mob;

	public ExEMobilVia(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isVia();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é via", result);
	}

}
