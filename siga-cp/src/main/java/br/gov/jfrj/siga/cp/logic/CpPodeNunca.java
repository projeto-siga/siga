package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;

public class CpPodeNunca implements Expression {
	
	public CpPodeNunca() {
	}

	@Override
	public boolean eval() {
		return false;
	}

	@Override
	public String explain(boolean result) {
		return "nunca pode";
	}

}