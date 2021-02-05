package br.gov.jfrj.siga.base;

public class UnauthorizedWebAppAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedWebAppAccessException(String mensagem) {
		super(mensagem);
	}

}
