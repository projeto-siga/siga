package br.gov.jfrj.siga.tp.exceptions;

public class AndamentoControllerException extends Exception {

    private static final long serialVersionUID = -1622966722308118372L;

    public AndamentoControllerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AndamentoControllerException(Exception e) {
        super(e.getMessage(), e);
    }

    public AndamentoControllerException(String msg) {
        super(msg);
    }

}
