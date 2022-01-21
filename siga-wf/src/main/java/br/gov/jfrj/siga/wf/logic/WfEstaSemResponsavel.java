package br.gov.jfrj.siga.wf.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfEstaSemResponsavel implements Expression {

	private WfProcedimento pi;

	public WfEstaSemResponsavel(WfProcedimento pi) {
		this.pi = pi;
	}

	@Override
	public boolean eval() {
		if (pi.getEventoPessoa() == null && pi.getEventoLotacao() == null)
			return true;
		return false;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("em tarefa de sistema", result);
	}
};