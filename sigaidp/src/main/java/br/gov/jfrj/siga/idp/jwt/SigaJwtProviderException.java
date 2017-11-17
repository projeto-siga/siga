package br.gov.jfrj.siga.idp.jwt;

public class SigaJwtProviderException extends Exception {

	public SigaJwtProviderException(String mensagem, Exception e) {
		super(mensagem,e);
	}

	public SigaJwtProviderException(String mensagem) {
		super(mensagem);
	}

}
