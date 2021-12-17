package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEMobilVolume implements Expression {
	ExMobil mob;

	public ExEMobilVolume(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isVolume();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é volume", result);
	}

}
