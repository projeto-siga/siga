package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

import java.util.Set;

@AcessoPublico
public class DocumentosSiglaLinkPublicoGet implements IExApiV1.IDocumentosSiglaLinkPublicoGet {

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
        filter.setSigla(req.sigla);
        ExMobil mob = ExDao.getInstance().consultarPorSigla(filter);
        if (mob == null)
            throw new AplicacaoException(
                    "Não foi possível encontrar o documento a partir da sigla fornecida");

        Set<ExMovimentacao> movs = mob.getMovsNaoCanceladas(ExTipoDeMovimentacao.GERAR_LINK_PUBLICO_PROCESSO);
        if (movs.isEmpty())
            throw new AplicacaoException("Link público ainda não foi gerado para o documento " + mob.getSigla());

        CpToken cpToken = Cp.getInstance().getBL().gerarUrlPermanente(mob.getDoc().getIdDoc());

        resp.link = Cp.getInstance().getBL().obterURLPermanente(cpToken.getIdTpToken().toString(), cpToken.getToken());

    }

    @Override
    public String getContext() {
        return "obter link público do documento";
    }


}
