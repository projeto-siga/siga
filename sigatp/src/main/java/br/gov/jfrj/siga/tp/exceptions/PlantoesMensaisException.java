package br.gov.jfrj.siga.tp.exceptions;

public class PlantoesMensaisException extends RuntimeException {

    private static final long serialVersionUID = 8832328559197983997L;

    public PlantoesMensaisException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PlantoesMensaisException(Exception e) {
        super(e.getMessage(), e);
    }

}
