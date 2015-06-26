package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrFatorMultiplicacao;

/**
 * Classe que representa um V.O. de {@link SrFatorMultiplicacao}.
 */
public class SrFatorMultiplicacaoVO {
    private Long idFatorMultiplicacao;
    private int numFatorMultiplicacao;
    private SelecionavelVO dpPessoaVO;
    private SelecionavelVO dpLotacaoVO;

    public SrFatorMultiplicacaoVO(Long idFatorMultiplicacao, int numFatorMultiplicacao, DpPessoa dpPessoa, DpLotacao dpLotacao) {
        this.setIdFatorMultiplicacao(idFatorMultiplicacao);
        this.setNumFatorMultiplicacao(numFatorMultiplicacao);
        this.setDpPessoaVO(SelecionavelVO.createFrom(dpPessoa));
        this.setDpLotacaoVO(SelecionavelVO.createFrom(dpLotacao));
    }

    public Long getIdFatorMultiplicacao() {
        return idFatorMultiplicacao;
    }

    public void setIdFatorMultiplicacao(Long idFatorMultiplicacao) {
        this.idFatorMultiplicacao = idFatorMultiplicacao;
    }

    public int getNumFatorMultiplicacao() {
        return numFatorMultiplicacao;
    }

    public void setNumFatorMultiplicacao(int numFatorMultiplicacao) {
        this.numFatorMultiplicacao = numFatorMultiplicacao;
    }

    public SelecionavelVO getDpPessoaVO() {
        return dpPessoaVO;
    }

    public void setDpPessoaVO(SelecionavelVO dpPessoaVO) {
        this.dpPessoaVO = dpPessoaVO;
    }

    public SelecionavelVO getDpLotacaoVO() {
        return dpLotacaoVO;
    }

    public void setDpLotacaoVO(SelecionavelVO dpLotacaoVO) {
        this.dpLotacaoVO = dpLotacaoVO;
    }
}