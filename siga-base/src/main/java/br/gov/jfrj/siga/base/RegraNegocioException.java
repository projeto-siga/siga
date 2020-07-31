package br.gov.jfrj.siga.base;

public class RegraNegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RegraNegocioException(String mensagem) {
		super(mensagem);
	}

}
