package br.gov.jfrj.siga.sr.model;

public enum SrTipoMotivoFechamento {

	ATENDIMENTO_CONCLUÍDO(1, "Atendimento Concluído"),
	ERRO_CLASSIFICACAO(1, "Erro de classificação"), 
	DADOS_INCORRETOS(3, "Dados insuficientes ou incorretos"),
	SOLUCAO_ALTERNATIVA(4, "Solução alternativa apresentada"),
	ATENDIMENTO_NEGADO(5, "Atendimento não será efetuado"),
	ATENDIMENTO_EM_OUTRA_SOLICITACAO(6, "Atendimento sendo efetuado em outra solicitação");

    private int idTipoMotivoFechamento;

    private String descrTipoMotivoFechamento;

    SrTipoMotivoFechamento(int id, String descricao) {
        this.setidTipoMotivoFechamento(id);
        this.setDescrTipoMotivoFechamento(descricao);
    }

    public int getidTipoMotivoFechamento() {
        return idTipoMotivoFechamento;
    }

    public void setidTipoMotivoFechamento(int idTipoMotivoFechamento) {
        this.idTipoMotivoFechamento = idTipoMotivoFechamento;
    }

    public String getDescrTipoMotivoFechamento() {
        return descrTipoMotivoFechamento;
    }

    public void setDescrTipoMotivoFechamento(String descrTipoMotivoFechamento) {
        this.descrTipoMotivoFechamento = descrTipoMotivoFechamento;
    }

}
