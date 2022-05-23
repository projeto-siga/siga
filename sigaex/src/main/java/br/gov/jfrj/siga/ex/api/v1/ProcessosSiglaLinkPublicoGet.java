package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeObterLinkPublicoDoProcesso;

public class ProcessosSiglaLinkPublicoGet implements IExApiV1.IProcessosSiglaLinkPublicoGet {

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Processo a obter link público");
        
        
        String urlPermanente = Ex.getInstance().getBL().obterLinkPublicoProcesso(mob);

        if (urlPermanente == null) {
            throw new AplicacaoException("Link público ainda não foi gerado para este Processo.");
        }

        resp.link = urlPermanente;
    }

    @Override
    public String getContext() {
        return "obter link público do Processo";
    }


}
