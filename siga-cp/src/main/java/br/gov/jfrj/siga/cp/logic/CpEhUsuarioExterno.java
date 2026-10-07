package br.gov.jfrj.siga.cp.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.dp.DpPessoa;

public class CpEhUsuarioExterno implements Expression {

	private DpPessoa titular;

	public CpEhUsuarioExterno(DpPessoa titular) {
		this.titular = titular;

	}

	@Override
	public boolean eval() {
		return titular != null && (titular.isUsuarioExterno() || CpBL.isUsuarioExterno(titular, titular.getLotacao()));
	}

	@Override
	public String explain(boolean result) {
		 return JLogic.NOT + " é Usuário Externo";
	}
};