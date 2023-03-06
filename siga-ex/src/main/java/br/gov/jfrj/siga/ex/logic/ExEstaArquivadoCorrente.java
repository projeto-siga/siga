package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaArquivadoCorrente implements Expression {
	ExMobil mob;

	public ExEstaArquivadoCorrente(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob != null && mob.isArquivadoCorrente();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está no arquivo corrente", result);
	}

}
