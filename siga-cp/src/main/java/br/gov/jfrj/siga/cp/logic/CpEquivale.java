package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.model.Historico;

public class CpEquivale implements Expression {
	Historico obj;
	String nome;
	Historico obj2;
	String nome2;

	public CpEquivale(Historico obj, String nome, Historico obj2, String nome2) {
		this.obj = obj;
		this.nome = nome;
		this.obj2 = obj2;
		this.nome2 = nome2;
	}

	@Override
	public boolean eval() {
		return Utils.equivale(obj, obj2);
	}

	@Override
	public String explain(boolean result) {
		if (result)
			return nome + " equivale a " + nome2;
		else
			return nome + " " + JLogic.NOT + " equivale a " + nome2;
	}

}