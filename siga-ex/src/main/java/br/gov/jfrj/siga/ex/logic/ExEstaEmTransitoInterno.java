package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaEmTransitoInterno implements Expression {

	ExMobil mob;

	public ExEstaEmTransitoInterno(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isEmTransitoInterno();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está em transito interno", result);
	}

}
