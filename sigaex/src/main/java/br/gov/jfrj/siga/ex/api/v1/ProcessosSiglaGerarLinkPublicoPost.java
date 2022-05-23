package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class ProcessosSiglaGerarLinkPublicoPost implements IExApiV1.IProcessosSiglaGerarLinkPublicoPost {

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        DpPessoa cadastrante = ctx.getCadastrante();
        DpPessoa titular = ctx.getTitular();
        DpLotacao lotaTitular = ctx.getLotaTitular();

        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Processo a gerar link público");
    
        ctx.assertAcesso(mob, titular, lotaTitular);

        resp.link = Ex.getInstance().getBL().gerarLinkPublicoProcesso(cadastrante, titular, lotaTitular, mob);

    }

    @Override
    public String getContext() {
        return "gerar link público do Processo";
    }


}
