package br.gov.jfrj.siga.tp.util;

@SuppressWarnings("serial")
public class SigaTpException extends Exception {

	public SigaTpException() {
	}

	public SigaTpException(String message) {
		super(message);
	}

	public SigaTpException(Throwable cause) {
		super(cause);
	}

	public SigaTpException(String message, Throwable cause) {
		super(message, cause);
	}
}