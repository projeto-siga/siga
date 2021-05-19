package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaCriarViaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaCriarViaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaCriarViaPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaCriarViaPost implements IDocumentosSiglaCriarViaPost {

	@Override
	public void run(DocumentosSiglaCriarViaPostRequest req, DocumentosSiglaCriarViaPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp,
						"Documento a Criar Via");

				if (!Ex.getInstance().getComp().podeCriarVia(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
					throw new PresentableUnloggedException("O documento " + mob.getSigla()
							+ " não pode ser arquivado no corrente por " + ctx.getLotaTitular().getSiglaCompleta() + "/"
							+ ctx.getLotaTitular().getSiglaCompleta());
				}

				ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				Ex.getInstance().getBL().criarVia(ctx.getTitular(), ctx.getLotaTitular(), mob.doc());

				resp.sigla = mob.doc().getUltimaVia().getCodigoCompacto();
				resp.status = "OK";
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Criar via";
	}

}
