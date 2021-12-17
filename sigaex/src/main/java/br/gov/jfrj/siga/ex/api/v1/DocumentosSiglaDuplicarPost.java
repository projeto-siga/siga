package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDuplicarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeDuplicar;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaDuplicarPost implements IDocumentosSiglaDuplicarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Duplicar");

		Ex.getInstance().getComp().afirmar(
				"O documento " + mob.getSigla() + " não pode ser duplicado por " + ctx.getTitular().getSiglaCompleta()
						+ "/" + ctx.getLotaTitular().getSiglaCompleta(),
				ExPodeDuplicar.class, ctx.getTitular(), ctx.getLotaTitular(), mob);

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		ExDocumento doc = Ex.getInstance().getBL().duplicar(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob.doc());

		resp.sigla = doc.getCodigo();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Duplicar documento";
	}

}
