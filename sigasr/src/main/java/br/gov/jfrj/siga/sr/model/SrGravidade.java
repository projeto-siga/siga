package br.gov.jfrj.siga.sr.model;

public enum SrGravidade {

    SEM_GRAVIDADE(1, "Sem gravidade.", "Sem gravidade"), NORMAL(2, "Gravidade normal.", "Pouco grave"), GRAVE(3, "Grave.", "Grave"), MUITO_GRAVE(4, "Muito grave.", "Muito grave"), EXTREMAMENTE_GRAVE(
            5, "Extremamente grave.", "Extremamente grave");

    private int nivelGravidade;

    private String descrGravidade;

    private String respostaEnunciado;

    SrGravidade(int nivel, String descricao) {
        this(nivel, descricao, descricao);
    }

    private SrGravidade(int nivel, String descrGravidade, String respostaEnunciado) {
        this.setNivelGravidade(nivel);
        this.setDescrGravidade(descrGravidade);
        this.setRespostaEnunciado(respostaEnunciado);
    }

    public int getNivelGravidade() {
        return nivelGravidade;
    }

    public void setNivelGravidade(int nivelGravidade) {
        this.nivelGravidade = nivelGravidade;
    }

    public String getDescrGravidade() {
        return descrGravidade;
    }

    public void setDescrGravidade(String descrGravidade) {
        this.descrGravidade = descrGravidade;
    }

    public String getRespostaEnunciado() {
        return respostaEnunciado;
    }

    public void setRespostaEnunciado(String respostaEnunciado) {
        this.respostaEnunciado = respostaEnunciado;
    }

}
