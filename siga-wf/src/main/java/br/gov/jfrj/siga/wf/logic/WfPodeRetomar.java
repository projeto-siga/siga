package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfPodeRetomar extends CompositeExpressionSupport {

	private WfProcedimento pi;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfPodeRetomar(WfProcedimento pi, DpPessoa titular, DpLotacao lotaTitular) {
		this.pi = pi;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return new WfEstaRetomando(pi);
	}
};