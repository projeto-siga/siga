package br.gov.jfrj.siga.tp.exceptions;

public class RelatorioException extends Exception {

    private static final long serialVersionUID = 1165861702449985093L;

    public RelatorioException(Exception e) {
        super(e.getMessage(), e);
    }

    public RelatorioException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
