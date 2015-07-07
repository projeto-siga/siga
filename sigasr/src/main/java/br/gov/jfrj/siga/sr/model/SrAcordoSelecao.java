package br.gov.jfrj.siga.sr.model;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecao;

public class SrAcordoSelecao extends Selecao<SrAcordo>{

    @Override
    public String getAcaoBusca() {
        return "/acordo";
    }

    @Override
    public boolean buscarPorId() throws AplicacaoException {
        final SrAcordo o = buscarObjeto();
        if (o == null)
            return false;

        buscarPorObjeto(o);
        return true;
    }

    @Override
    public SrAcordo buscarObjeto() throws AplicacaoException {
        if (getId() == null)
            return null;

        final SrAcordo o = CpDao.getInstance().consultar(getId(), SrAcordo.class, false);

        return o;
    }

    @Override
    public boolean buscarPorSigla() throws AplicacaoException {
        final SrAcordo oExemplo = new SrAcordo();
        oExemplo.setSigla(getSigla());

        final SrAcordo o = CpDao.getInstance().consultarPorSigla(oExemplo);

        if (o == null) {
            apagar();
            return false;
        }

        buscarPorObjeto(o);
        return true;
    }

    public void carregarDadosParaView(SrAcordo acordo) {
        if (acordo != null) {
            this.setId(acordo.getId());
            this.setDescricao(acordo.getDescricao());
            this.setSigla(acordo.getSigla());
        }
    }

}
