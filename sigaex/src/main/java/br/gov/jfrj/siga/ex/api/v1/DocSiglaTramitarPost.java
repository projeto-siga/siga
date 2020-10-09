package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaTramitarPost;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocSiglaTramitarPost implements IDocSiglaTramitarPost {

	@SuppressWarnings("resource")
	@Override
	public void run(DocSiglaTramitarPostRequest req,
			DocSiglaTramitarPostResponse resp) throws Exception {
		CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));

		SwaggerHelper.buscarEValidarUsuarioLogado();
		
		SigaObjects so = SwaggerHelper.getSigaObjects();
//		so.assertAcesso("DOC:Módulo de Documentos;" + "");


		try {
			DpPessoa cadastrante = so.getCadastrante();
			DpLotacao lotaCadastrante = cadastrante.getLotacao();
			DpPessoa titular = cadastrante;
			DpLotacao lotaTitular = cadastrante.getLotacao();

			ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
			flt.setSigla(req.sigla);
			ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);

			DpLotacao lot = new DpLotacao();
			if (req.lotacao != null) {
				lot.setSigla(req.lotacao);
				lot = ExDao.getInstance().consultarPorSigla(lot);
			}

			DpPessoa pes = new DpPessoa();
			if (req.matricula != null) {
				pes.setSigla(req.matricula);
				pes = ExDao.getInstance().consultarPorSigla(pes);
			}

			Utils.assertAcesso(mob, titular, lotaTitular);

			if (!Ex.getInstance().getComp()
					.podeTransferir(titular, lotaTitular, mob))
				throw new PresentableUnloggedException("O documento " + req.sigla
						+ " não pode ser tramitado por "
						+ titular.getSiglaCompleta() + "/"
						+ lotaTitular.getSiglaCompleta());

			Ex.getInstance()
					.getBL()
					.transferir(null, null, cadastrante, lotaCadastrante,
							mob, null, null, null, lot, pes, null, null,
							null, titular, null, true, null, null, null,
							false, false);
			resp.status = "OK";
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}

	}

	@Override
	public String getContext() {
		return "tramitar documento";
	}

}
