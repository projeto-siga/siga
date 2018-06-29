package br.gov.jfrj.siga.vraptor;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

import com.auth0.jwt.JWTSigner;

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

		String acl = "PUBLIC;O" + getTitular().getOrgaoUsuario().getId() + ";L"
				+ getTitular().getLotacao().getIdInicial() + ";P"
				+ getTitular().getIdInicial();

		final JWTSigner signer = new JWTSigner(Cp.getInstance().getProp()
				.xjusJwtSecret());
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 60 * 60L; // token expires in 1h
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("acl", acl);
		String token = signer.sign(claims);

		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);
		String response = http.getNaWeb(url, headers, 60000, null);

		HttpResult httpr = result.use(Results.http());
		httpr.addHeader("Content-Type", contentType);
		httpr.body(response).setStatusCode(200);
	}
}