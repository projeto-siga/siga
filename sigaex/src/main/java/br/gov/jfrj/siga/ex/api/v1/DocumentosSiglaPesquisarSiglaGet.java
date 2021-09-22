package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaPesquisarSiglaGet;

public class DocumentosSiglaPesquisarSiglaGet implements IDocumentosSiglaPesquisarSiglaGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp);

		resp.sigla = mob.getSigla();
		resp.codigo = mob.getCodigoCompacto();
	}

	@Override
	public String getContext() {
		return "pesquisar mobil por sigla";
	}
}
