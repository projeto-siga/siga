package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

public class CpPodeBoolean implements Expression {
	boolean pode;
	String explain;

	public CpPodeBoolean(boolean pode, String explain) {
		this.pode = pode;
		this.explain = explain;
	}

	@Override
	public boolean eval() {
		return pode;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain(explain, result);
	}

}