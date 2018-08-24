package br.gov.jfrj.siga.tp.exceptions;

public class GabineteControllerException extends Exception {

    private static final long serialVersionUID = -954427155838033058L;

    public GabineteControllerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public GabineteControllerException(Exception e) {
        super(e.getMessage(), e);
    }

    public GabineteControllerException(String msg) {
        super(msg);
    }

}
