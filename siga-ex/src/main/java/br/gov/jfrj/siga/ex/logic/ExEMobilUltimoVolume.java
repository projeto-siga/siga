package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEMobilUltimoVolume implements Expression {
	ExMobil mob;

	public ExEMobilUltimoVolume(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isUltimoVolume();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é último volume", result);
	}

}
