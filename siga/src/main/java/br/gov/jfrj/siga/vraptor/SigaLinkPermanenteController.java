package br.gov.jfrj.siga.vraptor;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

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
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.util.SigaUtil;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.unirest.proxy.GoogleRecaptcha;

@Controller
public class SigaLinkPermanenteController extends SigaController {
	HttpServletResponse response;
	String END_POINT_SIGALINK_DOC = "/sigaex/public/app/arquivo/obterDownloadDocumento";

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
			pwd = Prop.get("/siga.recaptcha.key");
			if (pwd == null)
				throw new AplicacaoException(
						"Erro obtendo propriedade siga.recaptcha.key");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro obtendo propriedade siga.recaptcha.key",
					0, e);
		}
	}

	private static String getRecaptchaSitePassword() {
		String pwd = null;
		try {
			pwd = Prop.get("/siga.recaptcha.pwd");
			if (pwd == null)
				throw new AplicacaoException(
						"Erro obtendo propriedade siga.recaptcha.pwd");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro obtendo propriedade siga.recaptcha.pwd",
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

				result.forwardTo(this).publicPermanenteURLPdfView(SigaUtil.buildJwtToken(tipoLink,token,siglaDocumento));			
			}
		} else {
			throw new RuntimeException("Endereço permamente inválido");
		}
		return;
		
	}
	
	
	@Post
	@Path("/public/app/sigalinkPdfView")
	public void publicPermanenteURLPdfView(@PathParam("jwt") String jwt) throws Exception {
	
		String sigla = SigaUtil.verifyGetJwtToken(jwt).get("sigla").toString();
		result.include("sigla", sigla);
		result.include("jwt", jwt);

	}
	
	
	@Get
	@Path("/public/app/sigalinkStream/{jwt}") /* Desacoplar tela de visualização de PDF*/
	public Download publicPermanenteURLStream(@PathParam("jwt") String jwt, boolean completo, boolean estampar, boolean volumes) throws Exception {
		estampar = true; //default
		InputStream stream = null;
		
		Map<String, Object> token = SigaUtil.verifyGetJwtToken(jwt);
		String tipoLink = token.get("tipoLink").toString();
		String sigla = token.get("sigla").toString();
		try {

			if ("1".equals(tipoLink)) {
				if (!"".equals(sigla)) {
					/* Reaproveitado estrutura da ExArquivoController - Analisar levar para WebService */ 
					/* Identidicar parametro interno de contexto JBoss */
					String endPoint = System.getProperty("exservice.endpoint").replace("/sigaex/servicos/ExService?wsdl", "") + END_POINT_SIGALINK_DOC + "?semmarcas="+ estampar +"&completo="+ completo +"&t="+jwt +"&mime=pdf";
					stream = new URL(endPoint).openStream();
					
					String fileName = sigla.replace("-", "").replace("/", "");
					if (completo) 
						fileName = fileName + "_completo.pdf";
					else
						fileName = fileName + ".pdf";

					return new InputStreamDownload(stream, "application/pdf",fileName);
				}
			}
		} catch (final Exception e) {
			result.include("mensagemCabec", "Ocorreu um erro na geração ou documento não está mais disponível.");
			result.include("msgCabecClass", "alert-danger");
			return null;
		} 

		return null;
	
	}
	
}