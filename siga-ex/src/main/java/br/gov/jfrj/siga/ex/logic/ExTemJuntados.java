package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExTemJuntados implements Expression {
	ExMobil mob;

	public ExTemJuntados(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return !mob.getJuntados(Boolean.TRUE).isEmpty();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem juntados", result);
	}

}
