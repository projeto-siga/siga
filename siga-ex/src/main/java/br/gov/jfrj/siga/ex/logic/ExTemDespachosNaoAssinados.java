package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExTemDespachosNaoAssinados implements Expression {
	ExMobil mob;

	public ExTemDespachosNaoAssinados(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.temDespachosNaoAssinados();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem despachos não assinados", result);
	}

}
