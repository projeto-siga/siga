package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDuplicarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDuplicarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDuplicarPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaDuplicarPost implements IDocumentosSiglaDuplicarPost {

	@Override
	public void run(DocumentosSiglaDuplicarPostRequest req, DocumentosSiglaDuplicarPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");

				ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, ctx.getSigaObjects(), req, resp,
						"Documento a Duplicar");

				if (!Ex.getInstance().getComp().podeDuplicar(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
					throw new PresentableUnloggedException("O documento " + mob.getSigla()
							+ " não pode ser duplicado por " + ctx.getTitular().getSiglaCompleta() + "/"
							+ ctx.getLotaTitular().getSiglaCompleta());
				}

				ApiContext.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				ExDocumento doc = Ex.getInstance().getBL().duplicar(ctx.getCadastrante(), ctx.getLotaCadastrante(),
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
		return "Duplicar documento";
	}

}
