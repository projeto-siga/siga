package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaPesquisarSiglaGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaPesquisarSiglaGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaPesquisarSiglaGet;

public class DocumentosSiglaPesquisarSiglaGet implements IDocumentosSiglaPesquisarSiglaGet {

	@Override
	public void run(DocumentosSiglaPesquisarSiglaGetRequest req, DocumentosSiglaPesquisarSiglaGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			ApiContext.assertAcesso("");
	
			ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, req, resp);
	
			resp.sigla = mob.getSigla();
			resp.codigo = mob.getCodigoCompacto();
		} catch (AplicacaoException | SwaggerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "pesquisar mobil por sigla";
	}
}
