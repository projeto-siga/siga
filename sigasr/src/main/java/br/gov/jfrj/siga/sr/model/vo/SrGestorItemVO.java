package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrGestorItem;

/**
 * Classe que representa um V.O. de {@link SrGestorItem}.
 */
public class SrGestorItemVO {
    private Long idGestorItem;
    private SelecionavelVO dpPessoaVO;
    private SelecionavelVO dpLotacaoVO;

    public SrGestorItemVO(Long idGestorItem, DpPessoa dpPessoa, DpLotacao dpLotacao) {
        this.setIdGestorItem(idGestorItem);
        this.setDpPessoaVO(SelecionavelVO.createFrom(dpPessoa));
        this.setDpLotacaoVO(SelecionavelVO.createFrom(dpLotacao));
    }

    public Long getIdGestorItem() {
        return idGestorItem;
    }

    public void setIdGestorItem(Long idGestorItem) {
        this.idGestorItem = idGestorItem;
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