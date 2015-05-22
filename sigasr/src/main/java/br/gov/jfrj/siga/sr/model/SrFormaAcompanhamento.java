package br.gov.jfrj.siga.sr.model;

public enum SrFormaAcompanhamento {

    ABERTURA_ANDAMENTO(1, "Na abertura e a cada andamento"), ABERTURA_FECHAMENTO(2, "Na abertura e no fechamento"), ABERTURA(3, "Somente na abertura");

    private long idFormaAcompanhamento;
    private String descrFormaAcompanhamento;

    SrFormaAcompanhamento(int id, String descricao) {
        this.setIdFormaAcompanhamento(id);
        this.setDescrFormaAcompanhamento(descricao);
    }

    public long getIdFormaAcompanhamento() {
        return idFormaAcompanhamento;
    }

    public void setIdFormaAcompanhamento(long idFormaAcompanhamento) {
        this.idFormaAcompanhamento = idFormaAcompanhamento;
    }

    public String getDescrFormaAcompanhamento() {
        return descrFormaAcompanhamento;
    }

    public void setDescrFormaAcompanhamento(String descrFormaAcompanhamento) {
        this.descrFormaAcompanhamento = descrFormaAcompanhamento;
    }

}
