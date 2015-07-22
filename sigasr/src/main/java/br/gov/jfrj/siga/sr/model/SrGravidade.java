package br.gov.jfrj.siga.sr.model;

public enum SrGravidade {

	SEM_GRAVIDADE(1, "Sem gravidade", ""), NORMAL(2, "Solicitante impossibilitado de realizar o trabalho",
            ""), GRAVE(3, "Serviço fora do ar", ""), MUITO_GRAVE(4,
            "Evento em andamento (audiência, etc) ou solicitante prioritário", ""), EXTREMAMENTE_GRAVE(5,
            "Extremamente grave", "");


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
