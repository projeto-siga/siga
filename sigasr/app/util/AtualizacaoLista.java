package util;

import models.SrPrioridade;

public class AtualizacaoLista {

	private Long idPrioridadeSolicitacao;
	private SrPrioridade prioridade;
	private Long numPosicao;
	private boolean naoReposicionarAutomatico;

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

	public boolean isNaoReposicionarAutomatico() {
		return naoReposicionarAutomatico;
	}

	public void setNaoReposicionarAutomatico(boolean naoReposicionarAutomatico) {
		this.naoReposicionarAutomatico = naoReposicionarAutomatico;
	}
}	