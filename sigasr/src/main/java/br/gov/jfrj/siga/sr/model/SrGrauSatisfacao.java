package br.gov.jfrj.siga.sr.model;

public enum SrGrauSatisfacao {

    MUITO_RUIM(0, "Muito ruim"), RUIM(1, "Ruim"), REGULAR(2, "Regular"), BOM(3, "Bom"), MUITO_BOM(4, "Muito bom"), EXCELENTE(5, "Excelente");

    private long idGrauSatisfacao;
    private String descrGrauSatisfacao;

    SrGrauSatisfacao(int id, String descricao) {
        this.setIdGrauSatisfacao(id);
        this.setDescrGrauSatisfacao(descricao);
    }

    public long getIdGrauSatisfacao() {
        return idGrauSatisfacao;
    }

    public void setIdGrauSatisfacao(long idGrauSatisfacao) {
        this.idGrauSatisfacao = idGrauSatisfacao;
    }

    public String getDescrGrauSatisfacao() {
        return descrGrauSatisfacao;
    }

    public void setDescrGrauSatisfacao(String descrGrauSatisfacao) {
        this.descrGrauSatisfacao = descrGrauSatisfacao;
    }

}
