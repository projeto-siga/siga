package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;

public enum Mes {

    JANEIRO(Calendar.JANUARY, "JANEIRO", "Janeiro"),
    FEVEREIRO(Calendar.FEBRUARY, "FEVEREIRO", "Fevereiro"), 
    MARCO(Calendar.MARCH, "MARCO", "Marco"), 
    ABRIL(Calendar.APRIL, "ABRIL", "Abril"), 
    MAIO(Calendar.MAY, "MAIO", "Maio"), 
    JUNHO(Calendar.JUNE, "JUNHO", "Junho"), 
    JULHO(Calendar.JULY, "JULHO", "Julho"), 
    AGOSTO(Calendar.AUGUST, "AGOSTO", "Agosto"), 
    SETEMBRO(Calendar.SEPTEMBER, "SETEMBRO", "Setembro"), 
    OUTUBRO(Calendar.OCTOBER, "OUTUBRO", "Outubro"), 
    NOVEMBRO(Calendar.NOVEMBER, "NOVEMBRO", "Novembro"), 
    DEZEMBRO(Calendar.DECEMBER, "DEZEMBRO", "Dezembro");

    private int codigo;
    private String nomeMaiusculo;
    private String nomeExibicao;

    private Mes(int codigo, String nomeMaiusculo, String nomeExibicao) {
        this.codigo = codigo;
        this.nomeMaiusculo = nomeMaiusculo;
        this.nomeExibicao = nomeExibicao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNomeMaiusculo() {
        return nomeMaiusculo;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public static Mes getMes(int codigoMes) {
        switch (codigoMes) {
        case Calendar.JANUARY:
            return Mes.JANEIRO;
        case Calendar.FEBRUARY:
            return Mes.FEVEREIRO;
        case Calendar.MARCH:
            return Mes.MARCO;
        case Calendar.APRIL:
            return Mes.ABRIL;
        case Calendar.MAY:
            return Mes.MAIO;
        case Calendar.JUNE:
            return Mes.JUNHO;
        case Calendar.JULY:
            return Mes.JULHO;
        case Calendar.AUGUST:
            return Mes.AGOSTO;
        case Calendar.SEPTEMBER:
            return Mes.SETEMBRO;
        case Calendar.OCTOBER:
            return Mes.OUTUBRO;
        case Calendar.NOVEMBER:
            return Mes.NOVEMBRO;
        case Calendar.DECEMBER:
            return Mes.DEZEMBRO;

        default:
            return null;
        }
    }

    public static Mes getMes(String mesEmTexto) {
        if (Mes.JANEIRO.getNomeExibicao().equals(mesEmTexto) || Mes.JANEIRO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.JANEIRO;
        }

        if (Mes.FEVEREIRO.getNomeExibicao().equals(mesEmTexto) || Mes.FEVEREIRO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.FEVEREIRO;
        }

        if (Mes.MARCO.getNomeExibicao().equals(mesEmTexto) || Mes.MARCO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.MARCO;
        }

        if (Mes.ABRIL.getNomeExibicao().equals(mesEmTexto) || Mes.ABRIL.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.ABRIL;
        }

        if (Mes.MAIO.getNomeExibicao().equals(mesEmTexto) || Mes.MAIO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.MAIO;
        }

        if (Mes.JUNHO.getNomeExibicao().equals(mesEmTexto) || Mes.JUNHO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.JUNHO;
        }

        if (Mes.JULHO.getNomeExibicao().equals(mesEmTexto) || Mes.JULHO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.JULHO;
        }

        if (Mes.AGOSTO.getNomeExibicao().equals(mesEmTexto) || Mes.AGOSTO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.AGOSTO;
        }

        if (Mes.SETEMBRO.getNomeExibicao().equals(mesEmTexto) || Mes.SETEMBRO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.SETEMBRO;
        }

        if (Mes.OUTUBRO.getNomeExibicao().equals(mesEmTexto) || Mes.OUTUBRO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.OUTUBRO;
        }

        if (Mes.NOVEMBRO.getNomeExibicao().equals(mesEmTexto) || Mes.NOVEMBRO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.NOVEMBRO;
        }

        if (Mes.DEZEMBRO.getNomeExibicao().equals(mesEmTexto) || Mes.DEZEMBRO.getNomeMaiusculo().equals(mesEmTexto)) {
            return Mes.DEZEMBRO;
        }

        return null;
    }

}
