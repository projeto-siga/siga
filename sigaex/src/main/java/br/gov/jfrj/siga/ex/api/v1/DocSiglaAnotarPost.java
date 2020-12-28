package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAnotarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaAnotarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaAnotarPost;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocSiglaAnotarPost implements IDocSiglaAnotarPost {

	@Override
	public void run(DocSiglaAnotarPostRequest req,
			DocSiglaAnotarPostResponse resp) throws Exception {
		
		SwaggerHelper.buscarEValidarUsuarioLogado();
		CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		
		SigaObjects so = SwaggerHelper.getSigaObjects();
		so.assertAcesso("DOC:Módulo de Documentos;" + "");

		try {
			DpPessoa cadastrante = so.getCadastrante();
			DpLotacao lotaCadastrante = cadastrante.getLotacao();
			DpPessoa titular = cadastrante;
			DpLotacao lotaTitular = cadastrante.getLotacao();

			ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
			flt.setSigla(req.sigla);
			ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);

			Utils.assertAcesso(mob, titular, lotaTitular);

			Ex.getInstance()
					.getBL()
					.anotar(cadastrante, lotaCadastrante, mob, null, null,
							null, null, cadastrante, req.anotacao, null);
			resp.status = "OK";
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}


	@Override
	public String getContext() {
		return "Anotar documento";
	}
}
