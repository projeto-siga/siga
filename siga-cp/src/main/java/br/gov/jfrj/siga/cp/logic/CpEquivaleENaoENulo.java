package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.model.Historico;

public class CpEquivaleENaoENulo implements Expression {
	Historico obj;
	String nome;
	Historico obj2;
	String nome2;

	public CpEquivaleENaoENulo(Historico obj, String nome, Historico obj2, String nome2) {
		this.obj = obj;
		this.nome = nome;
		this.obj2 = obj2;
		this.nome2 = nome2;
	}

	@Override
	public boolean eval() {
		return Utils.equivaleENaoENulo(obj, obj2);
	}

	@Override
	public String explain(boolean result) {
		if (result)
			return nome + " não é nulo e equivale a " + nome2;
		else
			return nome + " é nulo ou " + JLogic.NOT + " equivale a " + nome2;
	}

}