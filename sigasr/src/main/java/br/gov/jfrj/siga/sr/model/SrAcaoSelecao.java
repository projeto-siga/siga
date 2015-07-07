package br.gov.jfrj.siga.sr.model;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecao;

public class SrAcaoSelecao extends Selecao<SrAcao> {

    @Override
    public String getAcaoBusca() {
        return "/acao";
    }

    @Override
    public boolean buscarPorId() throws AplicacaoException {
        final SrAcao o = buscarObjeto();
        if (o == null)
            return false;

        buscarPorObjeto(o);
        return true;
    }

    @Override
    public SrAcao buscarObjeto() throws AplicacaoException {
        if (getId() == null)
            return null;

        final SrAcao o = CpDao.getInstance().consultar(getId(), SrAcao.class, false);

        return o;
    }

    @Override
    public boolean buscarPorSigla() throws AplicacaoException {
        final SrAcao oExemplo = new SrAcao();
        oExemplo.setSigla(getSigla());

        final SrAcao o = CpDao.getInstance().consultarPorSigla(oExemplo);

        if (o == null) {
            apagar();
            return false;
        }

        buscarPorObjeto(o);
        return true;
    }

    public void carregarDadosParaView(SrAcao acao) {
        if (acao != null) {
            this.setId(acao.getId());
            this.setDescricao(acao.getDescricao());
            this.setSigla(acao.getSigla());
        }
    }

}
