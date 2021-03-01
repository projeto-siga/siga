package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

public class PodeSim implements Expression {
	@Override
	public boolean eval() {
		return true;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("sempre pode", result);
	}
};