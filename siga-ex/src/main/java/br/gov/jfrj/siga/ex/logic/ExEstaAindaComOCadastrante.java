package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.ExTramiteBL.Pendencias;

public class ExEstaAindaComOCadastrante implements Expression {

	private ExMobil mob;

	public ExEstaAindaComOCadastrante(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		Pendencias p = mob.calcularTramitesPendentes();
		return p.fIncluirCadastrante;
	}

	@Override
	public String explain(boolean result) {
		return mob.getCodigo() + (result ? "" : JLogic.NOT) + " está ainda com o cadastrante";
	}
};
