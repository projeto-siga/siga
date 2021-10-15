package br.gov.jfrj.siga.ex.api.v1;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.itextpdf.Status;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.context.AcessoPublicoEPrivado;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaArquivoGet;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@AcessoPublicoEPrivado
public class DocumentosSiglaArquivoGet implements IDocumentosSiglaArquivoGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
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

		String filename = "application/pdf".equals(req.contenttype)
				? (req.volumes != null && req.volumes ? mob.doc().getReferenciaPDF() : mob.getReferenciaPDF())
				: (req.volumes != null && req.volumes ? mob.doc().getReferenciaHtml() : mob.getReferenciaHtml());
		final String servernameport = request.getServerName() + ":" + request.getServerPort();
		final String contextpath = request.getContextPath();

		iniciarGeracaoDePdf(req, resp, usuario, filename, contextpath, servernameport);
	}

	public static void iniciarGeracaoDePdf(Request req, Response resp, String u, String filename, String contextpath,
			String servernameport) throws IOException, Exception {
		resp.uuid = UUID.randomUUID().toString();
		String uuid = resp.uuid;
		Status.update(uuid, "Aguardando na fila de tarefas", 0, 100, 0L);

		String contenttype = req.contenttype;
		String sigla = req.sigla;
		resp.jwt = DownloadJwtFilenameGet.jwt(u, uuid, sigla, contenttype, filename);
		boolean estampar = req.estampa == null ? false : req.estampa;
		boolean volumes = req.volumes == null ? false : req.volumes;
		boolean exibirReordenacao = req.exibirReordenacao == null ? false : req.exibirReordenacao;
		DownloadAssincrono task = new DownloadAssincrono(uuid, contenttype, sigla, estampar, volumes, contextpath,
				servernameport, exibirReordenacao);

		ExApiV1Servlet.submitToExecutor(task);
	}

	@Override
	public String getContext() {
		return "obter documento completo";
	}

}