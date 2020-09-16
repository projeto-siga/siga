package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaPesquisarSiglaGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaPesquisarSiglaGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaPesquisarSiglaGet;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocSiglaPesquisarSiglaGet implements IDocSiglaPesquisarSiglaGet {

	@Override
	public void run(DocSiglaPesquisarSiglaGetRequest req,
			DocSiglaPesquisarSiglaGetResponse resp) throws Exception {
		CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));

		SwaggerHelper.buscarEValidarUsuarioLogado();
		SigaObjects so = SwaggerHelper.getSigaObjects();
		so.assertAcesso("DOC:Módulo de Documentos;" + "");


		DpPessoa cadastrante = so.getCadastrante();
		ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
		flt.setSigla(req.sigla);
		if (flt.getIdOrgaoUsu() == null)
			flt.setIdOrgaoUsu(cadastrante.getOrgaoUsuario().getIdOrgaoUsu());
		ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);
		if (mob != null) {
			resp.sigla = mob.getSigla();
			resp.codigo = mob.getCodigoCompacto();
		}

	}

	@Override
	public String getContext() {
		return "pesquisar mobil por sigla";
	}
}
