package br.gov.jfrj.siga.sr.model;

public enum SrOperador {
    MENOR(0, "Menor que"), MENOR_OU_IGUAL(1, "Menor ou Igual a"), IGUAL(2, "Igual a"), MAIOR(3, "Maior que"), MAIOR_OU_IGUAL(4, "Maior ou igual a");

    private int idOperador;
    private String nome;

    SrOperador(int idOperador, String nome) {
        this.setIdOperador(idOperador);
        this.setNome(nome);
    }

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
