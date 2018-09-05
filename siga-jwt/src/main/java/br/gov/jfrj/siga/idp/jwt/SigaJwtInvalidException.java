package br.gov.jfrj.siga.idp.jwt;

public class SigaJwtInvalidException extends Exception {

	public SigaJwtInvalidException(String mensagem, Exception e) {
		super(mensagem,e);
	}

	public SigaJwtInvalidException(String mensagem) {
		super(mensagem);
	}

}
