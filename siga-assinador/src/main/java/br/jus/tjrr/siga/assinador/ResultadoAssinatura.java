package br.jus.tjrr.siga.assinador;

public class ResultadoAssinatura {

    private String cadeia;
    private String assinatura;
    private String arquivo;
    private String nomeArquivo;

    public String getCadeia() {
        return cadeia;
    }

    public void setCadeia(String cadeia) {
        this.cadeia = cadeia;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

}
