package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.base.Prop;

public class ExEGovSP implements Expression {

	@Override
	public boolean eval() {
		return Prop.isGovSP();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é govsp", result);
	}

}
