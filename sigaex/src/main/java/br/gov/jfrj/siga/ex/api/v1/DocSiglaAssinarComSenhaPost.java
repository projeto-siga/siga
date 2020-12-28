package br.gov.jfrj.siga.ex.api.v1;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAssinarComSenhaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAssinarComSenhaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaAssinarComSenhaPost;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocSiglaAssinarComSenhaPost implements IDocSiglaAssinarComSenhaPost {

	@SuppressWarnings("resource")
	@Override
	public void run(DocSiglaAssinarComSenhaPostRequest req, DocSiglaAssinarComSenhaPostResponse resp) throws Exception {
		// Necessário pois é chamado o método "realPath" durante a criação do
		// PDF.
		CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));

		SwaggerHelper.buscarEValidarUsuarioLogado();
		SigaObjects so = SwaggerHelper.getSigaObjects();
		so.assertAcesso("DOC:Módulo de Documentos;" + "");


		try {
			DpPessoa cadastrante = so.getCadastrante();
			DpPessoa titular = cadastrante;
			DpLotacao lotaTitular = cadastrante.getLotacao();

			ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
			flt.setSigla(req.sigla);
			ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);
			ExDocumento doc = mob.doc();

			assertAcesso(mob, titular, lotaTitular);

			if (!Ex.getInstance().getComp().podeAssinarComSenha(titular, lotaTitular, mob)) {
				throw new PresentableUnloggedException(
						"O documento " + req.sigla + " não pode ser assinado com senha por "
								+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
			}

			Ex.getInstance().getBL().assinarDocumentoComSenha(cadastrante, lotaTitular, doc, null, cadastrante.getSiglaCompleta(), null,
					false, titular, false, null, false);

			resp.status = "OK";
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}
	

	private void assertAcesso(final ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		if (!Ex.getInstance().getComp().podeAcessarDocumento(titular, lotaTitular, mob)) {
			String s = "";
			s += mob.doc().getListaDeAcessosString();
			s = "(" + s + ")";
			s = " " + mob.doc().getExNivelAcessoAtual().getNmNivelAcesso() + " " + s;

			Map<ExPapel, List<Object>> mapa = mob.doc().getPerfis();
			boolean isInteressado = false;

			for (ExPapel exPapel : mapa.keySet()) {
				Iterator<Object> it = mapa.get(exPapel).iterator();

				if ((exPapel != null) && (exPapel.getIdPapel().equals(ExPapel.PAPEL_INTERESSADO))) {
					while (it.hasNext() && !isInteressado) {
						Object item = it.next();
						isInteressado = item.toString().equals(titular.getSigla()) ? true : false;
					}
				}

			}

			if (mob.doc().isSemEfeito()) {
				if (!mob.doc().getCadastrante().equals(titular) && !mob.doc().getSubscritor().equals(titular)
						&& !isInteressado) {
					throw new AplicacaoException("Documento " + mob.getSigla() + " cancelado ");
				}
			} else {
				throw new AplicacaoException("Documento " + mob.getSigla() + " inacessível ao usuário "
						+ titular.getSigla() + "/" + lotaTitular.getSiglaCompleta() + "." + s);
			}
		}
	}

	@Override
	public String getContext() {
		return "registrar assinatura com senha";
	}

}
