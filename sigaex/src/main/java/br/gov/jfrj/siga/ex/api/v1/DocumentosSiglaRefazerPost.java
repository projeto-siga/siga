package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaRefazerPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaRefazerPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaRefazerPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaRefazerPost implements IDocumentosSiglaRefazerPost {

	@Override
	public void run(DocumentosSiglaRefazerPostRequest req, DocumentosSiglaRefazerPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp,
						"Documento a Finalizar");

				if (!Ex.getInstance().getComp().podeRefazer(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
					throw new PresentableUnloggedException("O documento " + mob.getSigla()
							+ " não pode ser refeito por " + ctx.getTitular().getSiglaCompleta() + "/"
							+ ctx.getLotaTitular().getSiglaCompleta());
				}

				ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				ExDocumento doc = Ex.getInstance().getBL().refazer(ctx.getCadastrante(), ctx.getLotaCadastrante(),
						mob.doc());

				resp.sigla = doc.getCodigo();
				resp.status = "OK";
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Finalizar elaboração";
	}

}
