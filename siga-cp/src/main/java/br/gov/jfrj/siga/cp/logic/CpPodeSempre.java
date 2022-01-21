package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;

public class CpPodeSempre implements Expression {

	public CpPodeSempre() {
	}

	@Override
	public boolean eval() {
		return true;
	}

	@Override
	public String explain(boolean result) {
		return "sempre pode";
	}

}