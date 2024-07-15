package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaMigradoSEI implements Expression{
	ExMobil mob;

	public ExEstaMigradoSEI(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		
		if (mob.doc().isFinalizado() && mob.isGeral() && mob.doc().isProcesso()) {
			ExMobil ultVol = mob.doc().getUltimoVolume();
			return ultVol.isMigradoSEI();
		}
		return mob.isMigradoSEI();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("foi migrado para o SEI", result);
	}
	

}
