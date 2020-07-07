package br.gov.jfrj.siga.ex.api.v1;

import java.io.IOException;
import java.util.UUID;

import com.crivano.swaggerservlet.SwaggerAuthorizationException;

import br.gov.jfrj.itextpdf.Status;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentoSiglaPdfGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentoSiglaPdfGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentoSiglaPdfGet;
import br.gov.jfrj.siga.model.ContextoPersistencia;

@AcessoPublicoEPrivado
public class DocumentoSiglaPdfGet implements IDocumentoSiglaPdfGet {

	@Override
	public void run(DocumentoSiglaPdfGetRequest req, DocumentoSiglaPdfGetResponse resp) throws Exception {
		String usuario = null;
		String senha = null;
		String origem = null;

		String u = ContextoPersistencia.getUserPrincipal();

		if (usuario == null)
			throw new SwaggerAuthorizationException("Usuário não está logado");

		iniciarGeracaoDePdf(req, resp, u);
	}

	public static void iniciarGeracaoDePdf(DocumentoSiglaPdfGetRequest req, DocumentoSiglaPdfGetResponse resp, String u)
			throws IOException, Exception {
		resp.uuid = UUID.randomUUID().toString();
		Status.update(resp.uuid, "Aguardando na fila de tarefas", 0, 100, 0L);

		resp.jwt = DownloadJwtFilenameGet.jwt(u, resp.uuid, req.sigla);
		ExApiV1Servlet
				.submitToExecutor(new PdfCompleto(resp.uuid, req.sigla, req.estampa == null ? false : req.estampa));
	}

	@Override
	public String getContext() {
		return "obter documento completo";
	}

}
