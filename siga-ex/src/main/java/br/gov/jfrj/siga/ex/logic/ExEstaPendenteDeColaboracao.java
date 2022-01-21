package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaPendenteDeColaboracao implements Expression {

	private ExMobil mob;

	public ExEstaPendenteDeColaboracao(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isPendenteDeColaboracao();
	}

	@Override
	public String explain(boolean result) {
		return mob.getCodigo() + (result ? "" : JLogic.NOT) + " está pendente de colaboração";
	}
};
