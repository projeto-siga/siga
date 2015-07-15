package br.gov.jfrj.siga.sr.validator;

public class SrError {

	private final String key;
	private final String message;

	public SrError(String key, String value) {
		this.key = key;
		this.message = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return message;
	}
}
