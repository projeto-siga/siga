package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.lowagie.text.pdf.codec.Base64;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExArquivo;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.base.util.GoogleRecaptcha;

@Controller
public class ExProcessoConsultaPublicaController extends ExController {
	private static final String URL_EXIBIR = "/public/app/processoconsultarpublico";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String APPLICATION_PDF = "application/pdf";
	
	/**
	 * @deprecated CDI eyes only
	 */
	public ExProcessoConsultaPublicaController() {
		super();
	}

	@Inject
	public ExProcessoConsultaPublicaController(HttpServletRequest request, HttpServletResponse response,
			ServletContext context, Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	private void setDefaultResults() {
		result.include("request", getRequest());
	}

	@Get
	@Path("/processoconsultarpublico.action")
	public void redirecionar() throws Exception {
		result.redirectTo(this).processoconsultarpublico(null, null, null, null, null, null);
	}

	@Get
	@Post
	@Path("/public/app/processoconsultarpublico")
	public void processoconsultarpublico(final String n, final String answer, final String ass, final String assinaturaB64,
			final String certificadoB64, final String atributoAssinavelDataHora) throws Exception {
	
		String recaptchaSiteKey = getRecaptchaSiteKey();
		result.include("recaptchaSiteKey", recaptchaSiteKey);
		result.include("n", n);

		if (n == null || n.trim().length() == 0) {
			setDefaultResults();
			return;
		}

		if (!isCaptchaValido()) {
			setDefaultResults();
			return;
		}

		setDefaultResults();
		result.forwardTo(this).processoPublicoConsultado(buildJwtToken(n));
	}

	@Get("/public/app/processoPublicoConsultado")
	public void processoPublicoConsultado(final String jwt) throws Exception {
		
		if (jwt == null) {
			setDefaultResults();
			result.redirectTo(URL_EXIBIR);
			return;
		}
		
		String n = verifyJwtToken(jwt).get("n").toString();

		final ExDocumentoDTO exDocumentoDTO = consultarDocumento(n);
		
		verificarSePodeApresentarDocumento(exDocumentoDTO);
		
		ExDocumento doc = exDocumentoDTO.getDoc();

		ExMobil mob = doc.isProcesso() ? doc.getUltimoVolume() : doc.getPrimeiraVia();

		
		List<ExMobil> lstMobil = dao().consultarMobilPorDocumento(doc);
		List<ExMovimentacao> lista = dao().consultarMovimentoIncluindoJuntadaPorMobils(lstMobil);
		DpPessoa p = new DpPessoa();
		DpLotacao l = new DpLotacao();
		p = doc.getSubscritor();
		l = doc.getLotaSubscritor();
		if (p == null && !lista.isEmpty()) {
			p = lista.get(0).getSubscritor();
		}

		if (l == null && !lista.isEmpty()) {
			l = lista.get(0).getLotaSubscritor();
		}

		final ExDocumentoVO docVO = new ExDocumentoVO(doc, mob, getCadastrante(), p, l, true, true, false, true);

		result.include("movs", lista);
		result.include("sigla", exDocumentoDTO.getDoc().getSigla());
		result.include("msg", exDocumentoDTO.getMsg());
		result.include("docVO", docVO);
		result.include("mob", exDocumentoDTO.getMob());
		result.include("jwt", jwt);
	}

	private ExDocumentoDTO consultarDocumento(String n) {
		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();
		
		exDocumentoDTO.setSigla(n);
		
		buscarDocumento(false, true, exDocumentoDTO);
		return exDocumentoDTO;
	}

	private static String getJwtPassword() {
		return Prop.get("/siga.autenticacao.senha");
	}
	
	private static String buildJwtToken(String n) {
		String token;

		final JWTSigner signer = new JWTSigner(getJwtPassword());
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 1 * 60 * 60L; // token expires in 1 hours
		claims.put("exp", exp);
		claims.put("iat", iat);

		claims.put("n", n);
		token = signer.sign(claims);

		return token;
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


	private void buscarDocumento(final boolean fVerificarAcesso, final boolean fPodeNaoExistir,
			final ExDocumentoDTO exDocumentoDto) {
		if (exDocumentoDto.getMob() == null && exDocumentoDto.getSigla() != null
				&& exDocumentoDto.getSigla().length() != 0) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(exDocumentoDto.getSigla());
			exDocumentoDto.setMob((ExMobil) dao().consultarPorSigla(filter));
			if (exDocumentoDto.getMob() != null) {
				exDocumentoDto.setDoc(exDocumentoDto.getMob().getExDocumento());
			}
		} else if (exDocumentoDto.getMob() == null && exDocumentoDto.getDocumentoViaSel().getId() != null) {
			exDocumentoDto.setIdMob(exDocumentoDto.getDocumentoViaSel().getId());
			exDocumentoDto.setMob(dao().consultar(exDocumentoDto.getIdMob(), ExMobil.class, false));
		} else if (exDocumentoDto.getMob() == null && exDocumentoDto.getIdMob() != null
				&& exDocumentoDto.getIdMob() != 0) {
			exDocumentoDto.setMob(dao().consultar(exDocumentoDto.getIdMob(), ExMobil.class, false));
		}
		if (exDocumentoDto.getMob() != null) {
			exDocumentoDto.setDoc(exDocumentoDto.getMob().doc());
		}
		if (exDocumentoDto.getDoc() == null) {
			final String id = param("exDocumentoDTO.id");
			if (id != null && id.length() != 0) {
				exDocumentoDto.setDoc(daoDoc(Long.parseLong(id)));
			}
		}
		if (exDocumentoDto.getDoc() != null && exDocumentoDto.getMob() == null) {
			exDocumentoDto.setMob(exDocumentoDto.getDoc().getMobilGeral());
		}

		if (!fPodeNaoExistir && exDocumentoDto.getDoc() == null) {
			throw new AplicacaoException("Documento não informado");
		}
		if (fVerificarAcesso && exDocumentoDto.getMob() != null && exDocumentoDto.getMob().getIdMobil() != null) {
			verificaNivelAcesso(exDocumentoDto.getMob());
		}
	}

	
//	------------------------------ CAPTCHA
	private boolean isCaptchaValido() throws UnirestException, JSONException {
		
		String recaptchaSiteKey = getRecaptchaSiteKey();
		String recaptchaSitePassword = getRecaptchaSitePassword();
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");		

		boolean success = false;
		if (gRecaptchaResponse != null) {
			JsonNode body = null;
			if (GoogleRecaptcha.isProxySetted()) {
				body = GoogleRecaptcha.validarRecaptcha(recaptchaSitePassword, gRecaptchaResponse,
						request.getRemoteAddr());
			} else {
				HttpResponse<JsonNode> result = Unirest.post("https://www.google.com/recaptcha/api/siteverify")
						.header("accept", "application/json").header("Content-Type", "application/json")
						.queryString("secret", getRecaptchaSitePassword()).queryString("response", gRecaptchaResponse)
						.queryString("remoteip", request.getRemoteAddr()).asJson();

				body = result.getBody();
			}
			String hostname = request.getServerName();
			if (body.getObject().getBoolean("success")) {
				String retHostname = body.getObject().getString("hostname");
				success = retHostname.equals(hostname);
			}
		}
		
		return success;
	}

	
	private static String getRecaptchaSiteKey() {
		String pwd = null;
		try {
			pwd = System.getProperty("siga.recaptcha.key");
			if (pwd == null)
				throw new AplicacaoException("Erro obtendo propriedade siga.recaptcha.key");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo propriedade siga.recaptcha.key", 0, e);
		}
	}

	
	private static String getRecaptchaSitePassword() {
		String pwd = null;
		try {
			pwd = System.getProperty("siga.recaptcha.pwd");
			if (pwd == null)
				throw new AplicacaoException("Erro obtendo propriedade siga.recaptcha.pwd");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo propriedade siga.recaptcha.pwd", 0, e);
		}
	}
	
	
	@Get("/public/app/arquivoConsultado_stream")
	public Download arquivoConsultado_stream(final String jwt, final String sigla) throws Exception {
		if (jwt == null) {
			
			setDefaultResults();
			
			result.redirectTo(URL_EXIBIR);
			
			return null;
		}
		
		String n = verifyJwtToken(jwt).get("n").toString();
		
		final ExDocumentoDTO exDocumentoDTO = consultarDocumento(sigla);
		
		verificarSePodeApresentarDocumento(exDocumentoDTO);
		
		ExArquivo arq = exDocumentoDTO.getDoc();
		
		String fileName =  arq.getReferenciaPDF();
		
		String contentType  = "application/pdf";

		Documento documento = new Documento();

		byte[] bytes = documento.getDocumento(((ExDocumento) arq).getMobilGeral(), null);
		 
		if (bytes == null) {

			throw new AplicacaoException(	"Arquivo não encontrado para Download.");
		}
		
		final boolean fB64 = getRequest().getHeader("Accept") != null
				&& getRequest().getHeader("Accept").startsWith(
						"text/vnd.siga.b64encoded");
		
		return new InputStreamDownload(makeByteArrayInputStream(bytes, fB64),	contentType, fileName);
	}

	private void verificarSePodeApresentarDocumento(final ExDocumentoDTO exDocumentoDTO) {
		
		if (exDocumentoDTO.getDoc() == null || exDocumentoDTO.getDoc().isPendenteDeAssinatura()) {
			
			throw new AplicacaoException("Documento não encontrado.");
		}

		// consultapublica.maxNivelacesso.tramitacao
		if (Integer.parseInt(exDocumentoDTO.getDoc().getNivelAcesso()) > Prop.getInt("consultapublica.exibe.tramitacao.ate.nivelacesso")) {

			throw new AplicacaoException("O documento possui um nível de sigilo que impede a visualização de sua tramitação.");
		}
	}
	
	private ByteArrayInputStream makeByteArrayInputStream(final byte[] content,	final boolean fB64) {
		
		final byte[] conteudo = (fB64 ? Base64.encodeBytes(content).getBytes() 	: content	);
		
		return (new ByteArrayInputStream(conteudo));
	}
}
