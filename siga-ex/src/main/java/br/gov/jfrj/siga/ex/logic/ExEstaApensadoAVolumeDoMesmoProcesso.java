package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaApensadoAVolumeDoMesmoProcesso implements Expression {
	ExMobil mob;

	public ExEstaApensadoAVolumeDoMesmoProcesso(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		return mob.isApensadoAVolumeDoMesmoProcesso();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está apensado a volume do mesmo processo", result);
	}

}
