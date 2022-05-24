package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.logic.ExPodeObterLinkPublicoDoProcesso;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

import java.util.Set;

public class ProcessosSiglaLinkPublicoGet implements IExApiV1.IProcessosSiglaLinkPublicoGet {

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Processo a obter link público");

        Set<ExMovimentacao> movs = mob.getMovsNaoCanceladas(ExTipoDeMovimentacao.GERAR_LINK_PUBLICO_PROCESSO);
        if (movs.isEmpty())
            throw new AplicacaoException("Link público ainda não foi gerado para este Processo.");

        if (!Ex.getInstance().getComp().pode(ExPodeObterLinkPublicoDoProcesso.class, mob.getDoc())) {
            throw new AplicacaoException("Não é possível obter o link para o processo especificado");
        }

        resp.link = Ex.getInstance().getBL().obterLinkPublicoProcesso(mob);

    }

    @Override
    public String getContext() {
        return "obter link público do Processo";
    }


}
