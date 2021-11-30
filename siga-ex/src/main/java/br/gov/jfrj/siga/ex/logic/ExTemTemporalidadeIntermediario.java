package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExTemTemporalidadeIntermediario implements Expression {
	ExMobil mob;

	public ExTemTemporalidadeIntermediario(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.temTemporalidadeIntermediario();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem temporalidade no arquivo intermediário", result);
	}

}
