package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

public class CpENulo implements Expression {
	Object obj;
	String nome;

	public CpENulo(Object obj, String nome) {
		this.obj = obj;
		this.nome = nome;
	}

	@Override
	public boolean eval() {
		return obj == null;
	}

	@Override
	public String explain(boolean result) {
		if (result)
			return nome + " é nulo";
		else
			return nome + " " + JLogic.NOT + " é nulo";
	}

}