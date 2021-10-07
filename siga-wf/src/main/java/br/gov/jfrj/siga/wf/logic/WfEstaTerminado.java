package br.gov.jfrj.siga.wf.logic;

import com.crivano.jflow.model.enm.ProcessInstanceStatus;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfEstaTerminado implements Expression {

	private WfProcedimento pi;

	public WfEstaTerminado(WfProcedimento pi) {
		this.pi = pi;
	}

	@Override
	public boolean eval() {
		if (pi.getStatus() == ProcessInstanceStatus.FINISHED)
			return true;
		return false;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("terminado", result);
	}
};