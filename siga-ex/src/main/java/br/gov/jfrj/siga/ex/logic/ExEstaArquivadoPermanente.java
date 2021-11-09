package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaArquivadoPermanente implements Expression {
	ExMobil mob;

	public ExEstaArquivadoPermanente(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isArquivadoPermanente();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está no arquivo permanente", result);
	}

}
