package br.gov.jfrj.siga.sr.model;

public enum SrTipoMotivoFechamento {

	ATENDIMENTO_CONCLUÍDO(1, "Demanda atendida. Chamado pode ser fechado."),
	ATENDIMENTO_CONCLUIDO_PARCIALMENTE(2, "Atendido parcialmente. Requer atuação de outra equipe."),
	TAREFA_CONCLUÍDA(9, "Tarefa concluída."),
	ERRO_CLASSIFICACAO(3, "Devolução por erro de escalonamento."), 
	DADOS_INCORRETOS(4, "Script de atendimento não cumprido - Requer mais informações."),
	SOLUCAO_ALTERNATIVA(5, "Solução de contorno criada/aplicada - Avaliar abertura de Problema."),
	ATENDIMENTO_NEGADO(6, "Chamado não atendido - Não atende pré-requisitos."),
	//ATENDIMENTO_EM_OUTRA_SOLICITACAO(7, "Atendimento efetuado em outra solicitação"),
	ATENDIMENTO_OUTRO_CANAL(8, "Solicitação já se encontra em atendimento por outro canal.");
	
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
