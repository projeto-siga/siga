package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaSobrestado implements Expression {
	ExMobil mob;

	public ExEstaSobrestado(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isSobrestado();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está sobrestado", result);
	}

}
