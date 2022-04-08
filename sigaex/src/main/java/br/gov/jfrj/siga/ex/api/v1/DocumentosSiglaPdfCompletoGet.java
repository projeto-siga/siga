package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaPdfCompletoGet;

public class DocumentosSiglaPdfCompletoGet implements IDocumentosSiglaPdfCompletoGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpPessoa titular = ctx.getTitular();
		DpLotacao lotaTitular = ctx.getLotaTitular();

		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp);

		ctx.assertAcesso(mob, titular, lotaTitular);

		resp.jwt = DownloadJwtFilenameGet.jwt(cadastrante.getSiglaCompleta(), mob.getCodigoCompacto(), null, null,
				null);
	}

	@Override
	public String getContext() {
		return "obter documento";
	}

}
