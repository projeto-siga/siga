package br.gov.jfrj.siga.sr.model;

public enum SrTipoMotivoEscalonamento {

    ERRO_ESCALONAMENTO(1, "Erro de escalonamento"), NOVO_ATENDENTE(2, "Escalonar solicitação para novo atendente"), DADOS_INSUFICIENTES(3, "Dados insuficientes");

    private int nivelTipoMotivoEscalonamento;
    private String descrTipoMotivoEscalonamento;

    SrTipoMotivoEscalonamento(int nivel, String descricao) {
        this.setNivelTipoMotivoEscalonamento(nivel);
        this.setDescrTipoMotivoEscalonamento(descricao);
    }

    public int getNivelTipoMotivoEscalonamento() {
        return nivelTipoMotivoEscalonamento;
    }

    public void setNivelTipoMotivoEscalonamento(int nivelTipoMotivoEscalonamento) {
        this.nivelTipoMotivoEscalonamento = nivelTipoMotivoEscalonamento;
    }

    public String getDescrTipoMotivoEscalonamento() {
        return descrTipoMotivoEscalonamento;
    }

    public void setDescrTipoMotivoEscalonamento(String descrTipoMotivoEscalonamento) {
        this.descrTipoMotivoEscalonamento = descrTipoMotivoEscalonamento;
    }

}
