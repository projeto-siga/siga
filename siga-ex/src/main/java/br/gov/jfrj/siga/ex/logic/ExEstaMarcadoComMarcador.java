package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExEstaMarcadoComMarcador implements Expression {

	private ExMobil mob;
	private CpMarcador marcador;

	public ExEstaMarcadoComMarcador(ExMobil mob, CpMarcador marcador) {
		this.mob = mob;
		this.marcador = marcador;
	}

	@Override
	public boolean eval() {
		for (ExMarca mar : mob.getExMarcaSetAtivas()) {
			if (marcador.equals(mar.getCpMarcador()))
				return true;
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("está marcado com " + marcador.getDescrMarcador(), result);
	}

}
