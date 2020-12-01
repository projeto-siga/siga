package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaJuntarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaJuntarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaJuntarPost;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaJuntarPost implements IDocumentosSiglaJuntarPost {

	@Override
	public void run(DocumentosSiglaJuntarPostRequest req, DocumentosSiglaJuntarPostResponse resp) throws Exception {
		CurrentRequest.set(
				new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		SwaggerHelper.buscarEValidarUsuarioLogado();

		SigaObjects so = SwaggerHelper.getSigaObjects();

		DpPessoa cadastrante = so.getCadastrante();
		DpLotacao lotaTitular = cadastrante.getLotacao();

		ExMobil mobFilho = SwaggerHelper.buscarEValidarMobil(req.sigla, so, req, resp, "Documento Secundário");
		ExMobil mobPai = SwaggerHelper.buscarEValidarMobil(req.siglapai, so, req, resp, "Documento Principal");

		SwaggerHelper.assertAcesso(mobFilho, cadastrante, lotaTitular);

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
