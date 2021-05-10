package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDesentranharPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDesentranharPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDesentranharPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;

public class DocumentosSiglaDesentranharPost implements IDocumentosSiglaDesentranharPost {

	@Override
	public void run(DocumentosSiglaDesentranharPostRequest req, DocumentosSiglaDesentranharPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");

				ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, ctx.getSigaObjects(), req, resp,
						"Documento a Desentranhar");

				if (!Ex.getInstance().getComp().podeCancelarJuntada(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
					throw new PresentableUnloggedException("O documento " + mob.getSigla()
							+ " não pode ser desentranhado por " + ctx.getTitular().getSiglaCompleta() + "/"
							+ ctx.getLotaTitular().getSiglaCompleta());
				}

				ApiContext.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

				Ex.getInstance().getBL().cancelarJuntada(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob,
						ExDao.getInstance().getServerDateTime(), ctx.getTitular(), ctx.getTitular(), req.motivo);

				resp.status = "OK";
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Arquivar no Corrente";
	}

}
