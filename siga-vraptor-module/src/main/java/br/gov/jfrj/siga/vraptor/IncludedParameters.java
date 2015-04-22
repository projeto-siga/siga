package br.gov.jfrj.siga.vraptor;

import java.util.Map;

public class IncludedParameters {
	private Map<String, Object> map;

	public IncludedParameters(Map<String, Object> map) {
		this.map = map;
	}

	public Map<String, Object> getMap() {
		return map;
	}
}
