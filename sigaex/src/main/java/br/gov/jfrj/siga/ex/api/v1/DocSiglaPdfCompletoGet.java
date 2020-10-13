package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaPdfCompletoGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaPdfCompletoGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaPdfCompletoGet;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocSiglaPdfCompletoGet implements IDocSiglaPdfCompletoGet {

	@Override
	public void run(DocSiglaPdfCompletoGetRequest req, DocSiglaPdfCompletoGetResponse resp) throws Exception {
		CurrentRequest.set(
				new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));

		SwaggerHelper.buscarEValidarUsuarioLogado();
		SigaObjects so = SwaggerHelper.getSigaObjects();

		DpPessoa cadastrante = so.getCadastrante();
		DpPessoa titular = cadastrante;
		DpLotacao lotaTitular = cadastrante.getLotacao();

		ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, req, resp);

		SwaggerHelper.assertAcesso(mob, titular, lotaTitular);

		resp.jwt = DownloadJwtFilenameGet.jwt(cadastrante.getSiglaCompleta(), mob.getCodigoCompacto(), null, null,
				null);
	}

	@Override
	public String getContext() {
		return "obter documento";
	}

}
