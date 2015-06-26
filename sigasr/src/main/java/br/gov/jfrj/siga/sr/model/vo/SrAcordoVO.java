package br.gov.jfrj.siga.sr.model.vo;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAtributoAcordo;

public class SrAcordoVO extends AbstractSelecionavel {

    private boolean ativo;
    private Long hisIdIni;
    private String nomeAcordo;
    private String descrAcordo;
    private List<SrAtributoAcordoVO> atributoAcordoSet;

    public SrAcordoVO(Long id, String sigla, String descricao) {
        super(id, sigla, descricao);
    }

    public static SrAcordoVO createFrom(SrAcordo acordo) throws Exception {
        SrAcordoVO acordoVO = new SrAcordoVO(acordo.getId(), acordo.getSigla(), acordo.getDescricao());
        acordoVO.setAtivo(acordo.isAtivo());
        acordoVO.setHisIdIni(acordo.getHisIdIni());
        acordoVO.setNomeAcordo(acordo.getNomeAcordo());
        acordoVO.setDescrAcordo(acordo.getDescrAcordo());
        acordoVO.setAtributoAcordoSet(new ArrayList<SrAtributoAcordoVO>());

        if (acordo.getAtributoAcordoSet() != null)
            for (SrAtributoAcordo atributoAcordo : acordo.getAtributoAcordoSet())
                acordoVO.getAtributoAcordoSet().add(atributoAcordo.toVO());

        return acordoVO;
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

    public String getNomeAcordo() {
        return nomeAcordo;
    }

    public void setNomeAcordo(String nomeAcordo) {
        this.nomeAcordo = nomeAcordo;
    }

    public String getDescrAcordo() {
        return descrAcordo;
    }

    public void setDescrAcordo(String descrAcordo) {
        this.descrAcordo = descrAcordo;
    }

    public List<SrAtributoAcordoVO> getAtributoAcordoSet() {
        return atributoAcordoSet;
    }

    public void setAtributoAcordoSet(List<SrAtributoAcordoVO> atributoAcordoSet) {
        this.atributoAcordoSet = atributoAcordoSet;
    }

}
