package br.gov.jfrj.siga.tp.exceptions;

public class RestControllerException extends Exception {

    private static final long serialVersionUID = -3109562486253957042L;

    public RestControllerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RestControllerException(Exception e) {
        super(e.getMessage(), e);
    }
    
    public RestControllerException(String msg) {
        super(msg);
    }

}
