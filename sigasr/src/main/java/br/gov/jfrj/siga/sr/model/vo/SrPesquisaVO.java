package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.sr.model.SrPesquisa;

public class SrPesquisaVO extends AbstractSelecionavel {

    private Long hisIdIni;
    private boolean ativo;

    public SrPesquisaVO(Long id, String descricao) {
        super(id, descricao);
    }

    public SrPesquisaVO(Long id, String sigla, String descricao) {
        super(id, sigla, descricao);
    }

    public SrPesquisaVO(SrPesquisa pesquisa) {
        super(pesquisa.getIdPesquisa(), pesquisa.getDescrPesquisa());
        this.setHisIdIni(pesquisa.getHisIdIni());
        this.setAtivo(pesquisa.isAtivo());
    }

    public static SrPesquisaVO createFrom(SrPesquisa pesquisa) {
        if (pesquisa != null) {
            return new SrPesquisaVO(pesquisa);
        } else
            return null;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Long getHisIdIni() {
        return hisIdIni;
    }

    public void setHisIdIni(Long hisIdIni) {
        this.hisIdIni = hisIdIni;
    }
}
