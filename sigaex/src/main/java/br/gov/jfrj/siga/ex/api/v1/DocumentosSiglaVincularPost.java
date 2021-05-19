package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaVincularPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaVincularPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaVincularPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;

public class DocumentosSiglaVincularPost implements IDocumentosSiglaVincularPost {

	@Override
	public void run(DocumentosSiglaVincularPostRequest req, DocumentosSiglaVincularPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				ExMobil mobFilho = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento Secundário");
				ExMobil mobPai = ctx.buscarEValidarMobil(req.siglavertambem, req, resp,
						"Documento Ver Também");

				ctx.assertAcesso(mobFilho, ctx.getCadastrante(), ctx.getLotaTitular());

				Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

				if (!Ex.getInstance().getComp().podeVincular(ctx.getCadastrante(), ctx.getLotaTitular(), mobFilho)) {
					throw new AplicacaoException("Não é possível fazer vínculo");
				}

				Ex.getInstance().getBL().referenciarDocumento(ctx.getCadastrante(), ctx.getLotaCadastrante(), mobFilho,
						mobPai, dt, ctx.getCadastrante(), ctx.getCadastrante());

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
