package br.gov.jfrj.siga.sr.model;

public enum SrSemana {
    DOMINGO(1, "Domingo"), SEGUNDA(2, "Segunda-Feira"), TERCA(3, "Terça-Feira"), QUARTA(4, "Quarta-Feira"), QUINTA(5, "Quinta-feira"), SEXTA(6, "Sexta-Feira"), SABADO(7, "Sábado");

    private int idDiaSemana;
    private String descrDiaSemana;

    private SrSemana(int idDiaSemana, String descrDiaSemana) {
        this.setIdDiaSemana(idDiaSemana);
        this.setDescrDiaSemana(descrDiaSemana);
    }

    public int getIdDiaSemana() {
        return idDiaSemana;
    }

    public void setIdDiaSemana(int idDiaSemana) {
        this.idDiaSemana = idDiaSemana;
    }

    public String getDescrDiaSemana() {
        return descrDiaSemana;
    }

    public void setDescrDiaSemana(String descrDiaSemana) {
        this.descrDiaSemana = descrDiaSemana;
    }
}
