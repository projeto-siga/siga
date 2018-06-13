package br.gov.jfrj.siga.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Resource
public class XjusController extends SigaController {

	public XjusController(HttpServletRequest request, Result result, CpDao dao,
			SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("app/xjus")
	public void pesquisa(String q) throws Exception {
		result.include("q", q);
		result.include("xjusUrl", Cp.getInstance().getProp().xjusUrl());
	}

	@Get("app/xjus/query")
	public void buscarNoXjus() throws Exception {
		final SigaHTTP http = new SigaHTTP();
		String url = Cp.getInstance().getProp().xjusUrl();
		url += "?" + request.getQueryString();
		String contentType = "application/json";

		String response = http.get(url);

		HttpResult httpr = result.use(Results.http());
		httpr.addHeader("Content-Type", contentType);
		httpr.body(response).setStatusCode(200);
	}
}