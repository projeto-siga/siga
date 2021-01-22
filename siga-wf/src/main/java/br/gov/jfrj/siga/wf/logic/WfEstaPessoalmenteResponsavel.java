package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfEstaPessoalmenteResponsavel implements Expression {

	private WfProcedimento pi;
	private DpPessoa titular;

	public WfEstaPessoalmenteResponsavel(WfProcedimento pi, DpPessoa titular) {
		this.pi = pi;
		this.titular = titular;
	}

	@Override
	public boolean eval() {
		if (titular != null && titular.equivale(pi.getEventoPessoa()))
			return true;
		return false;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("pessoalmente responsável", result);
	}
};