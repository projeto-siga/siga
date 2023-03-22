package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaJuntadoAOutroProcesso implements Expression {
	ExMobil mob;

	public ExEstaJuntadoAOutroProcesso(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isProcessoJuntado();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("processo está juntado a outro processo", result);
	}

}
