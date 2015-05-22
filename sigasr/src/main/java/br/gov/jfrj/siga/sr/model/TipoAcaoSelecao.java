package br.gov.jfrj.siga.sr.model;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecao;

public class TipoAcaoSelecao extends Selecao<SrTipoAcao> {

    @Override
    public String getAcaoBusca() {
        return "/tipoAcao";
    }

    @Override
    public boolean buscarPorId() throws AplicacaoException {
        final SrTipoAcao o = buscarObjeto();
        if (o == null)
            return false;

        buscarPorObjeto(o);
        return true;
    }

    @Override
    public SrTipoAcao buscarObjeto() throws AplicacaoException {
        if (getId() == null)
            return null;

        final SrTipoAcao o = CpDao.getInstance().consultar(getId(), SrTipoAcao.class, false);

        return o;
    }

    @Override
    public boolean buscarPorSigla() throws AplicacaoException {
        final SrTipoAcao oExemplo = new SrTipoAcao();
        oExemplo.setSigla(getSigla());

        final SrTipoAcao o = CpDao.getInstance().consultarPorSigla(oExemplo);

        if (o == null) {
            apagar();
            return false;
        }

        buscarPorObjeto(o);
        return true;
    }

}
