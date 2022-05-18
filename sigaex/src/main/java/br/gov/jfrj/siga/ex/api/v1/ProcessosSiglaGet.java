package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodePublicarPortalDaTransparencia;

public class ProcessosSiglaGet implements IExApiV1.IProcessosSiglaGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpPessoa titular = ctx.getTitular();
		DpLotacao lotaTitular = ctx.getLotaTitular();

		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp);

		ctx.assertAcesso(mob, titular, lotaTitular);

		Ex.getInstance().getComp().afirmar("Não é possível gerar o link para o processo especificado", ExPodePublicarPortalDaTransparencia.class, cadastrante, lotaTitular, mob);
		
		resp.link = Prop.get("/siga.base.url") + "/siga/permalink/" + req.sigla;
	}

	@Override
	public String getContext() {
		return "obter link do processo";
	}


}
