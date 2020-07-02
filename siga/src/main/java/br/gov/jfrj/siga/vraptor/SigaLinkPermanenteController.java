package br.gov.jfrj.siga.vraptor;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.unirest.proxy.GoogleRecaptcha;

@Controller
public class SigaLinkPermanenteController extends SigaController {
	HttpServletResponse response;
	String END_POINT_SIGALINK_DOCPDF = "/sigaex/public/app/arquivo/obterPdfDocumento";

	/**
	 * @deprecated CDI eyes only
	 */
	public SigaLinkPermanenteController() {
		super();
	}

	@Inject
	public SigaLinkPermanenteController(HttpServletRequest request, HttpServletResponse response, Result result, CpDao dao,
			SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
		this.response = response;
	}
	
	
	private static String getRecaptchaSiteKey() {
		String pwd = null;
		try {
			pwd = System.getProperty("siga.ex.autenticacao.recaptcha.key");
			if (pwd == null)
				throw new AplicacaoException(
						"Erro obtendo propriedade siga.ex.autenticacao.recaptcha.key");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro obtendo propriedade siga.ex.autenticacao.recaptcha.key",
					0, e);
		}
	}

	private static String getRecaptchaSitePassword() {
		String pwd = null;
		try {
			pwd = System.getProperty("siga.ex.autenticacao.recaptcha.pwd");
			if (pwd == null)
				throw new AplicacaoException(
						"Erro obtendo propriedade siga.ex.autenticacao.recaptcha.pwd");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro obtendo propriedade siga.ex.autenticacao.recaptcha.pwd",
					0, e);
		}
	}

	@Get
	@Path("/public/app/sigalink/{tipoLink}/{token}")
	public void publicPermanenteURL(@PathParam("tipoLink") String tipoLink,@PathParam("token") String token) throws Exception {
		
	
		String recaptchaSiteKey = getRecaptchaSiteKey();
		String recaptchaSitePassword = getRecaptchaSitePassword();
		result.include("recaptchaSiteKey", recaptchaSiteKey);
		result.include("tipoLink", tipoLink);
		result.include("token", token);
		

		if (token == null || token.trim().length() == 0) {
			result.include("request", getRequest());
			return;
		}

		
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		boolean success = false;
		if (gRecaptchaResponse != null) {
			JsonNode body = null;
			if (GoogleRecaptcha.isProxySetted()) {
				body = GoogleRecaptcha.validarRecaptcha(recaptchaSitePassword, gRecaptchaResponse, request.getRemoteAddr());
			} else {
    			HttpResponse<JsonNode> result = Unirest
    					.post("https://www.google.com/recaptcha/api/siteverify")
    					.header("accept", "application/json")
    					.header("Content-Type", "application/json")
    					.queryString("secret", getRecaptchaSitePassword())
    					.queryString("response", gRecaptchaResponse)
    					.queryString("remoteip", request.getRemoteAddr()).asJson();
	
				body = result.getBody();
			}
			String hostname = request.getServerName();
			if (body.getObject().getBoolean("success")) {
				String retHostname = body.getObject().getString("hostname");
				success = retHostname.equals(hostname);
			}
		}
		if (!success) {
			result.include("request", getRequest());
			return;
		}
		
		
		CpToken cpToken = new CpToken();
		cpToken = dao().obterCpTokenPorTipoToken(Long.valueOf(tipoLink), token);
		if (cpToken != null) {
			if ("1".equals(tipoLink)) {
				ExService exService = Service.getExService();
				String siglaDocumento = exService.obterSiglaMobilPorIdDoc(cpToken.getIdRef());

				result.forwardTo(this).publicPermanenteURLPdfView(buildJwtToken(tipoLink,token,siglaDocumento));			
			}
		} else {
			throw new AplicacaoException("Endereço permamente inválido");
		}
		return;
		
	}
	
	
	@Post
	@Path("/public/app/sigalinkPdfView")
	public void publicPermanenteURLPdfView(@PathParam("jwt") String jwt) throws Exception {
		String sigla = verifyJwtToken(jwt).get("sigla").toString();
		result.include("sigla", sigla);
		result.include("jwt", jwt);
	}
	
	
	@Get
	@Path("/public/app/sigalinkStream/{jwt}") /* Desacoplar tela de visualização de PDF*/
	public Download publicPermanenteURLStream(@PathParam("jwt") String jwt, boolean completo, boolean estampar) throws Exception {
		estampar = true; //default
		String tipoLink = verifyJwtToken(jwt).get("tipoLink").toString();
		String sigla = verifyJwtToken(jwt).get("sigla").toString();
		
		if ("1".equals(tipoLink)) {
			if (!"".equals(sigla)) {
				/* Reaproveitado estrutura da ExArquivoController - Analisar levar para WebService */ 
				String endPoint = Contexto.urlBase(request) + END_POINT_SIGALINK_DOCPDF + "?semmarcas="+ estampar +"&completo="+ completo +"&t="+jwt;
				InputStream stream = new URL(endPoint).openStream();
				
				String fileName = sigla.replace("-", "").replace("/", "");
				if (completo) 
					fileName = fileName + "_completo.pdf";
				else
					fileName = fileName + ".pdf";
				
				return new InputStreamDownload(stream, "application/pdf",fileName);
			}
		}
		
		return null;
		
	}
	

	private static String getJwtPassword() {
		String pwd = null;
		try {
			pwd = System.getProperty("siga.ex.autenticacao.pwd");
			if (pwd == null)
				throw new AplicacaoException(
						"Erro obtendo propriedade siga.ex.autenticacao.pwd");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro obtendo propriedade siga.ex.autenticacao.pwd", 0, e);
		}
	}
	
	
	private static String buildJwtToken(final String tipoLink, final String token, final String sigla) {
		String jwt;

		final JWTSigner signer = new JWTSigner(getJwtPassword());
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 1 * 60 * 60L; // token expires in 1 hours
		claims.put("exp", exp);
		claims.put("iat", iat);

		claims.put("tipoLink", tipoLink);
		claims.put("token", token);
		claims.put("sigla", sigla);
		
		jwt = signer.sign(claims);

		return jwt;
	}

	private static Map<String, Object> verifyJwtToken(String token) {
		final JWTVerifier verifier = new JWTVerifier(getJwtPassword());
		try {
			Map<String, Object> map = verifier.verify(token);
			return map;
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao verificar token JWT", 0, e);
		}
	}
	
	
}