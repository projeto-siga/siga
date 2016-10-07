package br.gov.jfrj.siga.sr.model;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecao;

public class SrItemConfiguracaoSelecao extends Selecao<SrItemConfiguracao> {

    @Override
    public String getAcaoBusca() {
        return "/itemConfiguracao";
    }

    @Override
    public boolean buscarPorId() throws AplicacaoException {
        final SrItemConfiguracao o = buscarObjeto();
        if (o == null)
            return false;

        buscarPorObjeto(o);
        return true;
    }

    @Override
    public SrItemConfiguracao buscarObjeto() throws AplicacaoException {
        if (getId() == null)
            return null;

        final SrItemConfiguracao o = CpDao.getInstance().consultar(getId(), SrItemConfiguracao.class, false);

        return o;
    }

    @Override
    public boolean buscarPorSigla() throws AplicacaoException {
        final SrItemConfiguracao oExemplo = new SrItemConfiguracao();
        oExemplo.setSigla(getSigla());

        final SrItemConfiguracao o = CpDao.getInstance().consultarPorSigla(oExemplo);

        if (o == null) {
            apagar();
            return false;
        }

        buscarPorObjeto(o);
        return true;
    }

    public void carregarDadosParaView(SrItemConfiguracao itemConfiguracao) {
        if (itemConfiguracao != null) {
            this.setId(itemConfiguracao.getId());
            this.setDescricao(itemConfiguracao.getTituloItemConfiguracao());
            this.setSigla(itemConfiguracao.getSigla());
        }
    }

}
