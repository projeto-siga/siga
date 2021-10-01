package br.gov.jfrj.siga.wf.service;

import java.util.Map;

public class WfProcedimentoWSTO {
	private String sigla;
	private Map<String, Object> var;

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Map<String, Object> getVar() {
		return var;
	}

	public void setVar(Map<String, Object> var) {
		this.var = var;
	}
}
