package br.gov.jfrj.siga.tp.exceptions;

public class ControleGabineteControllerException extends Exception {

    private static final long serialVersionUID = -368050416283925168L;

    public ControleGabineteControllerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ControleGabineteControllerException(Exception e) {
        super(e.getMessage(), e);
    }
    
    public ControleGabineteControllerException(String msg) {
        super(msg);
    }

}
