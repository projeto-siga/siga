package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaApensarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaApensarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaApensarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaApensarPost implements IDocumentosSiglaApensarPost {

	@Override
	public void run(DocumentosSiglaApensarPostRequest req, DocumentosSiglaApensarPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");
				SigaObjects so = ApiContext.getSigaObjects();

				ExMobil mobFilho = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento Secundário");
				ExMobil mobPai = ctx.buscarEValidarMobil(req.siglamestre, req, resp, "Documento Mestre");

				ApiContext.assertAcesso(mobFilho, ctx.getCadastrante(), ctx.getLotaTitular());

				Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

				if (!Ex.getInstance().getComp().podeApensar(ctx.getCadastrante(), ctx.getLotaTitular(), mobFilho)) {
					throw new AplicacaoException("Não é possível fazer apensação");
				}

				Ex.getInstance().getBL().apensarDocumento(ctx.getCadastrante(), ctx.getTitular(),
						ctx.getLotaCadastrante(), mobFilho, mobPai, dt, ctx.getCadastrante(), ctx.getCadastrante());

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
