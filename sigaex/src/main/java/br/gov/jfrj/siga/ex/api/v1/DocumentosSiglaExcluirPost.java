package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaExcluirPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaExcluirPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaExcluirPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaExcluirPost implements IDocumentosSiglaExcluirPost {

	@Override
	public void run(DocumentosSiglaExcluirPostRequest req, DocumentosSiglaExcluirPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");

				ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, ctx.getSigaObjects(), req, resp,
						"Documento a Excluir");

				if (!Ex.getInstance().getComp().podeExcluir(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
					throw new PresentableUnloggedException("Exclusão do documento " + mob.getSigla()
							+ " não pode ser realizada por " + ctx.getTitular().getSiglaCompleta() + "/"
							+ ctx.getLotaTitular().getSiglaCompleta());
				}

				ApiContext.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				Ex.getInstance().getBL().excluirDocumento(mob.doc(), ctx.getCadastrante(), ctx.getLotaCadastrante(),
						false);

				resp.status = "OK";
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Excluir documento";
	}

}
