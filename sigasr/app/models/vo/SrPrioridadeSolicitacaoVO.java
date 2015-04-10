package models.vo;

import models.SrPrioridade;
import models.SrPrioridadeSolicitacao;

public class SrPrioridadeSolicitacaoVO {
	public Long idPrioridadeSolicitacao;
	public Long numPosicao;
	public SrPrioridade prioridade;
	public Boolean naoReposicionarAutomatico;
	public Long idSolicitacao;
	
	public static SrPrioridadeSolicitacaoVO createFrom(SrPrioridadeSolicitacao ps) {
		SrPrioridadeSolicitacaoVO vo = new SrPrioridadeSolicitacaoVO();
		vo.idPrioridadeSolicitacao = ps.idPrioridadeSolicitacao;
		vo.naoReposicionarAutomatico = ps.naoReposicionarAutomatico;
		vo.prioridade = ps.prioridade;
		vo.numPosicao = ps.numPosicao;
		vo.idSolicitacao = ps.solicitacao.getId();
		
		return vo;
	}
	
}
