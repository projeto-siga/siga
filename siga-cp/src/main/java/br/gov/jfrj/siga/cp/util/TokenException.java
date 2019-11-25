package br.gov.jfrj.siga.cp.util;

public class TokenException extends Exception {
	
	private static final long serialVersionUID = 6403915747212849147L;
	
	public static final String MSG_EX_TOKEN_INVALIDO = "TOKEN INVÁLIDO";

	public TokenException(String msg){
		super(msg);
	}

}
