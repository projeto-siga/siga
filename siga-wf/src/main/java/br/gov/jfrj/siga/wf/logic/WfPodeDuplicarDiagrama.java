package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

public class WfPodeDuplicarDiagrama extends CompositeExpressionSupport {

	private WfDefinicaoDeProcedimento pd;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfPodeDuplicarDiagrama(WfDefinicaoDeProcedimento pd, DpPessoa titular, DpLotacao lotaTitular) {
		this.pd = pd;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return new PodeSim();
	}
};