package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExMobEstaCiente implements Expression {
	ExMobil mob;
	DpPessoa titular;

	public ExMobEstaCiente(ExMobil mob, DpPessoa titular) {
		this.mob = mob;
		this.titular = titular;
	}

	@Override
	public boolean eval() {
		return mob.isCiente(titular);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está ciente", result);
	}

}
