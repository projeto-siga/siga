package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfEstaResponsavel implements Expression {

	private WfProcedimento pi;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfEstaResponsavel(WfProcedimento pi, DpPessoa titular, DpLotacao lotaTitular) {
		this.pi = pi;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		if (titular != null && titular.equivale(pi.getEventoPessoa()))
			return true;
		if (titular != null && titular.getLotacao().equivale(pi.getEventoLotacao()))
			return true;
		if (lotaTitular != null && lotaTitular.equivale(pi.getEventoLotacao()))
			return true;
		return false;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("responsável", result);
	}
};