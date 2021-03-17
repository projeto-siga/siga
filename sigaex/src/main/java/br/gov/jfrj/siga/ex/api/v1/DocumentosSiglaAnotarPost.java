package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaAnotarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaAnotarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaAnotarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaAnotarPost implements IDocumentosSiglaAnotarPost {

	@Override
	public void run(DocumentosSiglaAnotarPostRequest req, DocumentosSiglaAnotarPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			ApiContext.assertAcesso("");
			SigaObjects so = ApiContext.getSigaObjects();
	
			DpPessoa cadastrante = so.getCadastrante();
			DpLotacao lotaCadastrante = cadastrante.getLotacao();
			DpPessoa titular = cadastrante;
			DpLotacao lotaTitular = cadastrante.getLotacao();
	
			ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, so, req, resp, "Documento a Anotar");
	
			ApiContext.assertAcesso(mob, titular, lotaTitular);
	
			Ex.getInstance().getBL().anotar(cadastrante, lotaCadastrante, mob, null, null, null, null, cadastrante,
					req.anotacao, null);
	
			resp.status = "OK";
		} catch (AplicacaoException | SwaggerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}
	
	@Override
	public String getContext() {
		return "Anotar documento";
	}
}
