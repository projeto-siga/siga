package br.gov.jfrj.siga.tp.exceptions;

public class ApplicationControllerException extends Exception {

    private static final long serialVersionUID = -3591268859117336958L;

    public ApplicationControllerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ApplicationControllerException(Exception e) {
        super(e.getMessage(), e);
    }

}
