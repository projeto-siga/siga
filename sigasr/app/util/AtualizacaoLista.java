package util;

import models.SrPrioridade;

public class AtualizacaoLista {

	private Long idPrioridadeSolicitacao;
	private SrPrioridade prioridade;
	private Long numPosicao;

	public Long getIdPrioridadeSolicitacao() {
		return idPrioridadeSolicitacao;
	}

	public void setIdPrioridadeSolicitacao(Long idPrioridadeSolicitacao) {
		this.idPrioridadeSolicitacao = idPrioridadeSolicitacao;
	}

	public SrPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(SrPrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public Long getNumPosicao() {
		return numPosicao;
	}

	public void setNumPosicao(Long numPosicao) {
		this.numPosicao = numPosicao;
	}
}
