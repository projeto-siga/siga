package br.gov.jfrj.siga.vraptor;

import java.net.URI;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class BuscaTextualController extends SigaController {

	private static final String IDP_DOMAIN = "IDP-DOMAIN";
	private static final String IDP_JSESSIONID = "IDP-JSESSIONID";
	private static final String GSA_SESSION_ID = "GSA_SESSION_ID";


	/**
	 * @deprecated CDI eyes only
	 */
	public BuscaTextualController() {
		super();
	}

	@Inject
	public BuscaTextualController(HttpServletRequest request, Result result,
			CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("app/busca")
	public void buscaTextual(String q) throws Exception {
		result.include("q", q);
		result.include("gsaUrl", Prop.get("/siga.gsa.url"));
	}

	@Get("app/buscargsa")
	public void buscarNoGSA() throws Exception {
		final SigaHTTP http = new SigaHTTP();
		String url = Prop.get("/siga.gsa.url");
		url += "?" + request.getQueryString();
		if (url.contains("type=suggest"))
			url = url.replace("search", "suggest");
		String contentType = "application/json";

		if (url.contains("q=cache:"))
			contentType = "text/html";
		CookieStore cookieStore = new BasicCookieStore();
		final String gsaSession = request.getHeader(GSA_SESSION_ID);
		if (gsaSession != null) {
			BasicClientCookie cookie = new BasicClientCookie(GSA_SESSION_ID,
					gsaSession);
			URI uri = new URI(url);
			String domain = uri.getHost();
			cookie.setDomain(domain);
			cookieStore.addCookie(cookie);
		}
		final String idpDomain = request.getHeader(IDP_DOMAIN);
		final String idpSession = request.getHeader(IDP_JSESSIONID);
		String response = http.get(url, cookieStore, idpDomain, idpSession);

		// Corrige um problema no JSON do GSA
		response = response.replace("\\", "\\\\");

		HttpResult httpr = result.use(Results.http());
		httpr.addHeader("Content-Type", contentType);
		for (Cookie cookie : cookieStore.getCookies()) {
			if (cookie.getName().equals(GSA_SESSION_ID)
					&& cookie.getDomain() != null
					&& !cookie.getValue().equals(gsaSession)) {
				httpr.addHeader("Set-Cookie",
						GSA_SESSION_ID + "=" + cookie.getValue());

			}
		}
		httpr.body(response).setStatusCode(200);
	}
}