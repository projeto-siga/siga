package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaIncluirCopiaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaIncluirCopiaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaIncluirCopiaPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaIncluirCopiaPost implements IDocumentosSiglaIncluirCopiaPost {

	@Override
	public void run(DocumentosSiglaIncluirCopiaPostRequest req, DocumentosSiglaIncluirCopiaPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");
				SigaObjects so = ApiContext.getSigaObjects();

				ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, so, req, resp, "Documento Principal");
				ExMobil mobCopia = SwaggerHelper.buscarEValidarMobil(req.siglacopia, so, req, resp, "Documento Cópia");

				ApiContext.assertAcesso(mob, ctx.getCadastrante(), ctx.getLotaTitular());

				Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

				if (!Ex.getInstance().getComp().podeCopiar(ctx.getCadastrante(), ctx.getLotaTitular(), mob)) {
					throw new AplicacaoException("Não é possível copiar");
				}

				Ex.getInstance().getBL().copiar(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob, mobCopia, dt, null,
						ctx.getTitular());

				resp.status = "OK";
			} catch (RegraNegocioException | AplicacaoException e) {
				ctx.rollback(e);
				throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Juntar documento";
	}
}
