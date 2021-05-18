package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaFinalizarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaFinalizarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaFinalizarPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaFinalizarPost implements IDocumentosSiglaFinalizarPost {

	@Override
	public void run(DocumentosSiglaFinalizarPostRequest req, DocumentosSiglaFinalizarPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp,
						"Documento a Finalizar");

				if (!Ex.getInstance().getComp().podeFinalizar(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
					throw new PresentableUnloggedException("A elaboração do documento " + mob.getSigla()
							+ " não pode ser finalizado por " + ctx.getTitular().getSiglaCompleta() + "/"
							+ ctx.getLotaTitular().getSiglaCompleta());
				}

				ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				Ex.getInstance().getBL().finalizar(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob.doc());

				resp.sigla = mob.doc().getCodigo();
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
