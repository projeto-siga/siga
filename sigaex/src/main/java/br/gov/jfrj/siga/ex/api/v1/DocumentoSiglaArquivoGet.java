package br.gov.jfrj.siga.ex.api.v1;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.itextpdf.Status;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentoSiglaArquivoGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentoSiglaArquivoGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentoSiglaArquivoGet;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@AcessoPublicoEPrivado
public class DocumentoSiglaArquivoGet implements IDocumentoSiglaArquivoGet {

	@Override
	public void run(DocumentoSiglaArquivoGetRequest req, DocumentoSiglaArquivoGetResponse resp) throws Exception {
		try {
			String usuario = ContextoPersistencia.getUserPrincipal();

			if (usuario == null)
				throw new SwaggerAuthorizationException("Usuário não está logado");

			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(req.sigla);
			ExMobil mob = (ExMobil) ExDao.getInstance().consultarPorSigla(filter);
			if (mob == null)
				throw new PresentableUnloggedException(
						"Não foi possível encontrar um documento a partir da sigla fornecida");

			HttpServletRequest request = SwaggerServlet.getHttpServletRequest();
			SigaObjects so = new SigaObjects(request);
			if (!Ex.getInstance().getComp().podeAcessarDocumento(so.getTitular(), so.getLotaTitular(), mob))
				throw new AplicacaoException(
						"Acesso ao documento " + mob.getSigla() + " permitido somente a usuários autorizados. ("
								+ so.getTitular().getSigla() + "/" + so.getLotaTitular().getSiglaCompleta() + ")");

			String filename = "text/html".equals(req.contenttype)
					? ((req.volumes != null && req.volumes) ? mob.doc().getReferenciaPDF() : mob.getReferenciaPDF())
					: ((req.volumes != null && req.volumes) ? mob.doc().getReferenciaHtml() : mob.getReferenciaHtml());
			final String servernameport = request.getServerName() + ":" + request.getServerPort();
			final String contextpath = request.getContextPath();

			iniciarGeracaoDePdf(req, resp, usuario, filename, contextpath, servernameport);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	public static void iniciarGeracaoDePdf(DocumentoSiglaArquivoGetRequest req, DocumentoSiglaArquivoGetResponse resp,
			String u, String filename, String contextpath, String servernameport) throws IOException, Exception {
		resp.uuid = UUID.randomUUID().toString();
		Status.update(resp.uuid, "Aguardando na fila de tarefas", 0, 100, 0L);

		resp.jwt = DownloadJwtFilenameGet.jwt(u, resp.uuid, req.sigla, req.contenttype, filename);
		ExApiV1Servlet.submitToExecutor(new DownloadAssincrono(resp.uuid, req.contenttype, req.sigla,
				(req.estampa != null && req.estampa), (req.volumes != null && req.volumes), contextpath, servernameport,
				(req.exibirReordenacao != null && req.exibirReordenacao)));
	}

	@Override
	public String getContext() {
		return "obter documento completo";
	}

}