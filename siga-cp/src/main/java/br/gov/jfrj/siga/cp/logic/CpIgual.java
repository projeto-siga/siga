package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.base.util.Utils;

public class CpIgual implements Expression {
	Object obj;
	String nome;
	Object obj2;
	String nome2;

	public CpIgual(Object obj, String nome, Object obj2, String nome2) {
		this.obj = obj;
		this.nome = nome;
		this.obj2 = obj2;
		this.nome2 = nome2;
	}

	@Override
	public boolean eval() {
		return Utils.igual(obj, obj2);
	}

	@Override
	public String explain(boolean result) {
		if (result)
			return nome + " igual a " + nome2;
		else
			return nome + " " + JLogic.NOT + " igual a " + nome2;
	}

}