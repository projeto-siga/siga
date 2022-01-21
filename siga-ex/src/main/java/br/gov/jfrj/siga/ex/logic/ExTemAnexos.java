package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExTemAnexos implements Expression {
	ExMobil mob;

	public ExTemAnexos(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.temAnexos();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem anexos", result);
	}

}
