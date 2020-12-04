package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaPesquisarSiglaGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaPesquisarSiglaGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaPesquisarSiglaGet;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.RequestInfo;

public class DocumentosSiglaPesquisarSiglaGet implements IDocumentosSiglaPesquisarSiglaGet {

	@Override
	public void run(DocumentosSiglaPesquisarSiglaGetRequest req, DocumentosSiglaPesquisarSiglaGetResponse resp) throws Exception {
		CurrentRequest.set(
				new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		SwaggerHelper.buscarEValidarUsuarioLogado();

		ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, req, resp);

		resp.sigla = mob.getSigla();
		resp.codigo = mob.getCodigoCompacto();
	}

	@Override
	public String getContext() {
		return "pesquisar mobil por sigla";
	}
}
