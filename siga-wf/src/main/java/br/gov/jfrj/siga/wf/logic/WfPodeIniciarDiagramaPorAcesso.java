package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfAcessoDeInicializacao;

public class WfPodeIniciarDiagramaPorAcesso implements Expression {

	private WfDefinicaoDeProcedimento pd;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public WfPodeIniciarDiagramaPorAcesso(WfDefinicaoDeProcedimento pd, DpPessoa titular, DpLotacao lotaTitular) {
		this.pd = pd;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		return WfAcessoDeInicializacao.acessoPermitido(pd, titular, lotaTitular);
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("tem acesso para iniciar", result);
	}
};