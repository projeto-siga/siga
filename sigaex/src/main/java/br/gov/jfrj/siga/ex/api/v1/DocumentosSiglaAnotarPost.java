package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaAnotarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaAnotarPost implements IDocumentosSiglaAnotarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpLotacao lotaCadastrante = cadastrante.getLotacao();
		DpPessoa titular = cadastrante;
		DpLotacao lotaTitular = cadastrante.getLotacao();

		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Anotar");

		ctx.assertAcesso(mob, titular, lotaTitular);

		Ex.getInstance().getBL().anotar(cadastrante, lotaCadastrante, mob, null, null, null, null, cadastrante,
				req.anotacao, null);
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Anotar documento";
	}
}
