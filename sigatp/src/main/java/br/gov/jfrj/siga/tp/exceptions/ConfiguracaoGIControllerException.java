package br.gov.jfrj.siga.tp.exceptions;

public class ConfiguracaoGIControllerException extends Exception {

    private static final long serialVersionUID = 7185221888531521092L;

    public ConfiguracaoGIControllerException(Exception e) {
        super(e.getMessage(), e);
    }

    public ConfiguracaoGIControllerException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
