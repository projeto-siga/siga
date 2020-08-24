package br.gov.jfrj.siga.base;

public class SigaConstraintViolationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public SigaConstraintViolationException(String message) {						
		super(message);
	}

}