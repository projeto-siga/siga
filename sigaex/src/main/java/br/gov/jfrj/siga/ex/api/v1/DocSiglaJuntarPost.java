package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaJuntarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaJuntarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaJuntarPost;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocSiglaJuntarPost implements IDocSiglaJuntarPost {

	@Override
	public void run(DocSiglaJuntarPostRequest req, DocSiglaJuntarPostResponse resp) throws Exception {
		CurrentRequest.set(
				new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		SwaggerHelper.buscarEValidarUsuarioLogado();

		SigaObjects so = SwaggerHelper.getSigaObjects();

		DpPessoa cadastrante = so.getCadastrante();
		DpLotacao lotaTitular = cadastrante.getLotacao();

		ExMobil mobFilho = SwaggerHelper.buscarEValidarMobil(req.sigla, so, req, resp, "Documento Secundário");
		ExMobil mobPai = SwaggerHelper.buscarEValidarMobil(req.siglapai, so, req, resp, "Documento Principal");

		Utils.assertAcesso(mobFilho, cadastrante, lotaTitular);

		Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		Ex.getInstance().getBL().juntarDocumento(cadastrante, cadastrante, lotaTitular, null, mobFilho, mobPai, dt,
				cadastrante, cadastrante, "1");

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Juntar documento";
	}
}
