package br.gov.jfrj.siga.sr.model;

public enum SrUrgencia {

    SEM_PRESSA(1, "Sem urgência.", "Sem pressa"), NORMAL(2, "Urgência normal.", "Quando for oportuno"), URGENCIA(3, "Urgente.", "Com urgência"), MUITA_URGENCIA(4, "Muito urgente.",
            "Com muita urgência"), AGIR_IMEDIATO(5, "Extremamente urgente.", "Imediatamente");

    private int nivelUrgencia;

    private String descrUrgencia;

    private String respostaEnunciado;

    private SrUrgencia(int nivelUrgencia, String descrUrgencia) {
        this(nivelUrgencia, descrUrgencia, descrUrgencia);
    }

    SrUrgencia(int nivel, String descricao, String respostaEnunciado) {
        this.setNivelUrgencia(nivel);
        this.setDescrUrgencia(descricao);
        this.setRespostaEnunciado(respostaEnunciado);
    }

    public int getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(int nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public String getDescrUrgencia() {
        return descrUrgencia;
    }

    public void setDescrUrgencia(String descrUrgencia) {
        this.descrUrgencia = descrUrgencia;
    }

    public String getRespostaEnunciado() {
        return respostaEnunciado;
    }

    public void setRespostaEnunciado(String respostaEnunciado) {
        this.respostaEnunciado = respostaEnunciado;
    }

}
