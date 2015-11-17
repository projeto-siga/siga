package br.gov.jfrj.siga.sr.model;

public enum SrTipoMotivoFechamento {

	ATENDIMENTO_CONCLUÍDO(1, "Atendimento Concluído"),
	ERRO_CLASSIFICACAO(3, "Erro de classificação"), 
	DADOS_INCORRETOS(4, "Dados insuficientes ou incorretos"),
	SOLUCAO_ALTERNATIVA(5, "Solução alternativa apresentada"),
	ATENDIMENTO_NEGADO(6, "Atendimento não será efetuado"),
	ATENDIMENTO_EM_OUTRA_SOLICITACAO(7, "Atendimento efetuado em outra solicitação"),
	ATENDIMENTO_CONCLUIDO_PARCIALMENTE(2, "Concluído parcialmente (pendente de solução por outra Equipe)");

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
