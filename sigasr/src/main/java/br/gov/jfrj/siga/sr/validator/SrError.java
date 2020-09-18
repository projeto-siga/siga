package br.gov.jfrj.siga.sr.validator;

public class SrError {

	private final String category;
	private final String message;


	

	public SrError(String category, String message) {
		this.category = category;
		this.message = message;
	}

	public String getCategory() {
		return category;
	}

	public String getMessage() {
		return message;
	}
}
