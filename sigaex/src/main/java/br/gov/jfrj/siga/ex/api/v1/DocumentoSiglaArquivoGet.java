package br.gov.jfrj.siga.ex.api.v1;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.itextpdf.Status;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentoSiglaArquivoGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentoSiglaArquivoGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentoSiglaArquivoGet;

@AcessoPublicoEPrivado
public class DocumentoSiglaArquivoGet implements IDocumentoSiglaArquivoGet {

	@Override
	public void run(DocumentoSiglaArquivoGetRequest req, DocumentoSiglaArquivoGetResponse resp) throws Exception {
		String usuario = SwaggerHelper.buscarEValidarUsuarioLogado();

		ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, req, resp);
		String filename = "text/html".equals(req.contenttype)
				? (req.volumes ? mob.doc().getReferenciaPDF() : mob.getReferenciaPDF())
				: (req.volumes ? mob.doc().getReferenciaHtml() : mob.getReferenciaHtml());

		HttpServletRequest request = SwaggerServlet.getHttpServletRequest();
		final String contextpath = request.getContextPath();
		final String servernameport = request.getServerName() + ":" + request.getServerPort();

		iniciarGeracaoDePdf(req, resp, usuario, filename, contextpath, servernameport);
	}

	public static void iniciarGeracaoDePdf(DocumentoSiglaArquivoGetRequest req, DocumentoSiglaArquivoGetResponse resp,
			String u, String filename, String contextpath, String servernameport) throws IOException, Exception {
		resp.uuid = UUID.randomUUID().toString();
		Status.update(resp.uuid, "Aguardando na fila de tarefas", 0, 100, 0L);

		resp.jwt = DownloadJwtFilenameGet.jwt(u, resp.uuid, req.sigla, req.contenttype, filename);
		ExApiV1Servlet.submitToExecutor(
				new DownloadAssincrono(resp.uuid, req.contenttype, req.sigla, req.estampa == null ? false : req.estampa,
						req.volumes == null ? false : req.volumes, contextpath, servernameport, req.exibirReordenacao));
	}

	@Override
	public String getContext() {
		return "obter documento completo";
	}

}