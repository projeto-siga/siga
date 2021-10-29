package br.gov.jfrj.siga.wf.service;

import java.util.Map;

public class WfProcedimentoWSTO {
	private String sigla;
	private String principal;
	private String titular;
	private String lotaTitular;
	private Map<String, Object> var;

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public String getLotaTitular() {
		return lotaTitular;
	}

	public void setLotaTitular(String lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	public Map<String, Object> getVar() {
		return var;
	}

	public void setVar(Map<String, Object> var) {
		this.var = var;
	}

}
