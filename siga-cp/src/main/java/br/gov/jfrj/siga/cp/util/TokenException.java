package br.gov.jfrj.siga.cp.util;

@SuppressWarnings("serial")
public class TokenException extends Exception {

	public static final String MSG_EX_TOKEN_INVALIDO = "TOKEN INVÁLIDO";

	public TokenException(String msg){
		super(msg);
	}

}
