package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.ex.ExArquivoNumerado;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDossieGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaDossieGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DossieItem;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaDossieGet;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaDossieGet implements IDocumentosSiglaDossieGet {

	@Override
	public void run(DocumentosSiglaDossieGetRequest req, DocumentosSiglaDossieGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			ApiContext.assertAcesso("");
			SigaObjects so = ApiContext.getSigaObjects();

			ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, so, req, resp, "Documento");

			ApiContext.assertAcesso(mob, ctx.getCadastrante(), ctx.getLotaTitular());

			List<ExArquivoNumerado> ans = mob.getArquivosNumerados();
			resp.list = new ArrayList<>();
			for (ExArquivoNumerado an : ans) {
				DossieItem di = new DossieItem();
				di.paginaInicial = an.getPaginaInicial().toString();
				di.paginaFinal = an.getPaginaFinal().toString();
				di.mobil = an.getReferencia();
				di.descr = an.getNomeOuDescricao();
				di.origem = an.getArquivo().getLotacao().getSigla();
				di.data = an.getData();
				di.nivel = Integer.toString(an.getNivel());
				di.copia = an.isCopia();
				di.referenciaHtmlCompletoDocPrincipal = an.getReferenciaHtmlCompletoDocPrincipal();
				di.referenciaPDFCompletoDocPrincipal = an.getReferenciaPDFCompletoDocPrincipal();
				resp.list.add(di);
			}
		}
	}

	@Override
	public String getContext() {
		return "Listar documentos do dossiê";
	}
}
