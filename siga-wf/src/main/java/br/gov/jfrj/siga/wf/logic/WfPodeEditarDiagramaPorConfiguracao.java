package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

public class WfPodeEditarDiagramaPorConfiguracao implements Expression {

	private WfDefinicaoDeProcedimento pd;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfPodeEditarDiagramaPorConfiguracao(WfDefinicaoDeProcedimento pd, DpPessoa titular, DpLotacao lotaTitular) {
		this.pd = pd;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		return Wf.getInstance().getComp().podeEditarProcedimentoPorConfiguracao(titular, lotaTitular, pd);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem configuração para editar", result);
	}
};