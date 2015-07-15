package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.sr.model.SrPrioridade;
import br.gov.jfrj.siga.sr.model.SrPrioridadeSolicitacao;

public class SrPrioridadeSolicitacaoVO {
    private Long idPrioridadeSolicitacao;
    private Long numPosicao;
    private SrPrioridade prioridade;
    private Boolean naoReposicionarAutomatico;
    private Long idSolicitacao;

    public static SrPrioridadeSolicitacaoVO createFrom(SrPrioridadeSolicitacao ps) {
        SrPrioridadeSolicitacaoVO vo = new SrPrioridadeSolicitacaoVO();
        vo.setIdPrioridadeSolicitacao(ps.getIdPrioridadeSolicitacao());
        vo.setNaoReposicionarAutomatico(ps.getNaoReposicionarAutomatico());
        vo.setPrioridade(ps.getPrioridade());
        vo.setNumPosicao(ps.getNumPosicao());
        vo.setIdSolicitacao(ps.getSolicitacao().getId());

        return vo;
    }

    public Long getIdPrioridadeSolicitacao() {
        return idPrioridadeSolicitacao;
    }

    public void setIdPrioridadeSolicitacao(Long idPrioridadeSolicitacao) {
        this.idPrioridadeSolicitacao = idPrioridadeSolicitacao;
    }

    public Long getNumPosicao() {
        return numPosicao;
    }

    public void setNumPosicao(Long numPosicao) {
        this.numPosicao = numPosicao;
    }

    public SrPrioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(SrPrioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Boolean getNaoReposicionarAutomatico() {
        return naoReposicionarAutomatico;
    }

    public void setNaoReposicionarAutomatico(Boolean naoReposicionarAutomatico) {
        this.naoReposicionarAutomatico = naoReposicionarAutomatico;
    }

    public Long getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(Long idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

}
