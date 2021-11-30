package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEMobilCancelado implements Expression {
	ExMobil mob;

	public ExEMobilCancelado(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isCancelada();
	}

	@Override
	public String explain(boolean result) {
		return mob.getCodigo() + (result ? "" : JLogic.NOT) + " está cancelado";
	}

}
