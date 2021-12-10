package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaPendenteDeAnexacao implements Expression {

	private ExMobil mob;

	public ExEstaPendenteDeAnexacao(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isPendenteDeAnexacao();
	}

	@Override
	public String explain(boolean result) {
		return mob.getCodigo() + (result ? "" : JLogic.NOT) + " está pendente de anexação";
	}
};
