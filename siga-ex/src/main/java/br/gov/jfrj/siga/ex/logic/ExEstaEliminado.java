package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaEliminado implements Expression {
	ExMobil mob;

	public ExEstaEliminado(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isEliminado();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está eliminado", result);
	}

}
