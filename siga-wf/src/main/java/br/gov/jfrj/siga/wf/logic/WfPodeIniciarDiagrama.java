package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;
import com.crivano.jlogic.utils.CompositeExpressionSuport;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

public class WfPodeIniciarDiagrama extends CompositeExpressionSuport {

	private WfDefinicaoDeProcedimento pd;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfPodeIniciarDiagrama(WfDefinicaoDeProcedimento pd, DpPessoa titular, DpLotacao lotaTitular) {
		this.pd = pd;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return Or.of(new WfPodeIniciarDiagramaPorAcesso(pd, titular, lotaTitular),
				new WfPodeIniciarDiagramaPorConfiguracao(pd, titular, lotaTitular));
	}
};