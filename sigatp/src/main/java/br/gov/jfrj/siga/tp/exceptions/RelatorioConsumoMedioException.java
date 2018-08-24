package br.gov.jfrj.siga.tp.exceptions;

public class RelatorioConsumoMedioException extends Exception {
    private static final long serialVersionUID = 6855025006125619378L;

    public RelatorioConsumoMedioException(Exception e) {
        super(e.getMessage(), e);
    }

    public RelatorioConsumoMedioException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
