package br.gov.jfrj.siga.ex.api.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.context.AcessoPublicoEPrivado;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaArquivoProduzirGet;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@AcessoPublicoEPrivado
public class DocumentosSiglaArquivoProduzirGet implements IDocumentosSiglaArquivoProduzirGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		req.completo = req.completo != null ? req.completo : false;
		req.estampa = req.estampa != null ? req.estampa : false;
		req.volumes = req.volumes != null ? req.volumes : false;

		String usuario = ContextoPersistencia.getUserPrincipal();

		if (usuario == null)
			throw new SwaggerAuthorizationException("Usuário não está logado");

		String sigla = req.sigla;
		String idMov = null;
		if (sigla.contains(":")) {
			String[] split = sigla.split(":");
			sigla = split[0];
			idMov = split[1];
		}

		final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
		filter.setSigla(sigla);
		ExMobil mob = (ExMobil) ExDao.getInstance().consultarPorSigla(filter);
		if (mob == null)
			throw new PresentableUnloggedException(
					"Não foi possível encontrar um documento a partir da sigla fornecida");

		ExMovimentacao mov = null;
		if (idMov != null) {
			mov = ExDao.getInstance().consultar(Long.parseLong(idMov), ExMovimentacao.class, false);
			if (!mov.mob().doc().equals(mob.doc()))
				throw new PresentableUnloggedException("Movimentação não é referente ao documento informado");
		}

		HttpServletRequest request = SwaggerServlet.getHttpServletRequest();
		SigaObjects so = new SigaObjects(request);
		if (!Ex.getInstance().getComp().podeAcessarDocumento(so.getTitular(), so.getLotaTitular(), mob))
			throw new AplicacaoException(
					"Acesso ao documento " + mob.getSigla() + " permitido somente a usuários autorizados. ("
							+ so.getTitular().getSigla() + "/" + so.getLotaTitular().getSiglaCompleta() + ")");
		final String servernameport = request.getServerName() + ":" + request.getServerPort();
		final String contextpath = request.getContextPath();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if ("application/pdf".equals(req.contenttype)) {
			resp.contenttype = "application/pdf";
			Documento.getDocumento(baos, null, mob, mov, req.completo, req.estampa, req.volumes, null, null);
		} else {
			Documento.getDocumentoHTML(baos, null, mob, mov, req.completo, req.volumes, contextpath, servernameport);
		}
		byte[] ab;
		ab = baos.toByteArray();
		if (ab == null) {
			throw new Exception("Arquivo inválido!");
		}
		resp.contentlength = (long) ab.length;
		resp.contentdisposition = "inline";
		resp.inputstream = new ByteArrayInputStream(ab);
	}

	@Override
	public String getContext() {
		return "produzir documento";
	}

}