package br.gov.jfrj.siga.ex.api.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.itextpdf.Status;
import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaMarcadoresDisponiveisGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaMarcadoresDisponiveisGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentoSiglaArquivoGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentoSiglaArquivoGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaMarcadoresDisponiveisGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.Marcador;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeMarcarComMarcador;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@AcessoPublicoEPrivado
public class DocSiglaMarcadoresDisponiveisGet implements IDocSiglaMarcadoresDisponiveisGet {

	@Override
	public void run(DocSiglaMarcadoresDisponiveisGetRequest req, DocSiglaMarcadoresDisponiveisGetResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(false)) {
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
			DpPessoa titular = so.getTitular();
			DpLotacao lotaTitular = so.getLotaTitular();
			if (!Ex.getInstance().getComp().podeAcessarDocumento(titular, lotaTitular, mob))
				throw new AplicacaoException(
						"Acesso ao documento " + mob.getSigla() + " permitido somente a usuários autorizados. ("
								+ titular.getSigla() + "/" + lotaTitular.getSiglaCompleta() + ")");

			List<CpMarcador> marcadores = ExDao.getInstance().listarCpMarcadoresGeraisTaxonomiaAdministrada();

			if (marcadores != null) {
				resp.list = new ArrayList<>();
				for (CpMarcador m : marcadores) {
					Marcador mr = new Marcador();
					mr.idMarcador = m.getIdMarcador().toString();
					mr.nome = m.getDescrMarcador();
					ExPodeMarcarComMarcador pode = new ExPodeMarcarComMarcador(mob, m, titular, lotaTitular);
					mr.ativo = pode.eval();
					mr.explicacao = AcaoVO.Helper.formatarExplicacao(pode, mr.ativo);
					resp.list.add(mr);
				}
			}

		} catch (Exception e) {
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