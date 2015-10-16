package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.GenericoSelecao;

@Resource
public class BuscaTextualController extends SigaController {

	public BuscaTextualController(HttpServletRequest request, Result result,
			CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("app/busca")
	public void buscaTextual(String q) throws Exception {
		result.include("q", q);
		result.include("gsaUrl", Cp.getInstance().getProp().gsaUrl());
	}

	@Get("app/buscargsa")
	public void buscarNoGSA() throws Exception {
		final SigaHTTP http = new SigaHTTP();
		String url = Cp.getInstance().getProp().gsaUrl();
		url += request.getQueryString();
		String response = http.get(url, getRequest(), null);
		result.use(Results.http())
				.addHeader("Content-Type", "application/json").body(response)
				.setStatusCode(200);
		;
	}

}