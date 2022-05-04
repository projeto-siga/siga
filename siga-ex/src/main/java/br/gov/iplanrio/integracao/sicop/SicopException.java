package br.gov.iplanrio.integracao.sicop;

public class SicopException extends Exception {

    private static final long serialVersionUID = 1L;

    public SicopException(String mensagem){
        super(mensagem);
    }
    
    public SicopException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }
}
