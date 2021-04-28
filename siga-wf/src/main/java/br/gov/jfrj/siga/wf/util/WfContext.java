package br.gov.jfrj.siga.wf.util;

import com.crivano.jflow.model.ProcessInstance;

public class WfContext {
	ProcessInstance pi;

	WfContext(ProcessInstance pi) {
		this.pi = pi;
	}

	public void set(String var, Object value) {
		pi.getVariable().put(var, value);
	}

}
