package br.gov.jfrj.siga.tp.exceptions;

public class RequisicaoTransporteException extends Exception {

    private static final long serialVersionUID = 6347729725523823241L;

    public RequisicaoTransporteException(Exception e) {
        super(e.getMessage(), e);
    }

    public RequisicaoTransporteException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
