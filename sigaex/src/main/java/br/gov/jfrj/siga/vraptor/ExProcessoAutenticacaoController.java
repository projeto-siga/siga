package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.lowagie.text.pdf.codec.Base64;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.bluc.service.BlucService;
import br.gov.jfrj.siga.bluc.service.HashRequest;
import br.gov.jfrj.siga.bluc.service.HashResponse;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExArquivo;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.unirest.proxy.GoogleRecaptcha;

@Resource
public class ExProcessoAutenticacaoController extends ExController {

	private static final String URL_EXIBIR = "/public/app/processoautenticar";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String APPLICATION_PDF = "application/pdf";

	public ExProcessoAutenticacaoController(HttpServletRequest request, HttpServletResponse response,
			ServletContext context, Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	private void setDefaultResults() {
		result.include("request", getRequest());
	}

	@Get
	@Path("/processoautenticar.action")
	public void redirecionar() throws Exception {
		result.redirectTo(this).processoautenticar(null, null, null, null, null, null);
	}

	@Get
	@Post
	@Path("/public/app/processoautenticar")
	public void processoautenticar(final String n, final String answer, final String ass, final String assinaturaB64,
			final String certificadoB64, final String atributoAssinavelDataHora) throws Exception {
		String recaptchaSiteKey = getRecaptchaSiteKey();
		String recaptchaSitePassword = getRecaptchaSitePassword();
		result.include("recaptchaSiteKey", recaptchaSiteKey);
		result.include("n", n);

		if (n == null || n.trim().length() == 0) {
			setDefaultResults();
			return;
		}

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
		if (!success) {
			setDefaultResults();
			return;
		}
		ExArquivo arq = Ex.getInstance().getBL().buscarPorProtocolo(n);
		

		

		ExMovimentacao mov = null;
		
		if (ass != null && ass.trim().length() != 0) {
			byte[] assinatura = Base64.decode(assinaturaB64 == null ? "" : assinaturaB64);
			byte[] certificado = Base64.decode(certificadoB64 == null ? "" : certificadoB64);
			Date dt = mov.getDtMov();
			if (certificado != null && certificado.length != 0)
				dt = new Date(Long.valueOf(atributoAssinavelDataHora));
			else
				certificado = null;

			try {
				Ex.getInstance().getBL().assinarMovimentacao(null, null, mov, dt, assinatura, certificado,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO);
			} catch (final Exception e) {
				throw new AplicacaoException(e.getMessage());
			}
		}

		setDefaultResults();
		result.include("ass", ass);
		result.include("assinaturaB64", assinaturaB64);
		result.include("certificadoB64", certificadoB64);
		result.include("atributoAssinavelDataHora", atributoAssinavelDataHora);
		result.forwardTo(this).processoArquivoAutenticado(buildJwtToken(n));
	}

	@Get("/public/app/processoArquivoAutenticado_stream")
	public Download processoArquivoAutenticado_stream(final String jwt, final boolean assinado, final Long idMov,
			final String certificadoB64) throws Exception {

		if (jwt == null) {
			setDefaultResults();
			result.redirectTo(URL_EXIBIR);
			return null;
		}
		String n = verifyJwtToken(jwt).get("n").toString();

		ExArquivo arq = Ex.getInstance().getBL().buscarPorProtocolo(n);

		byte[] bytes;
		String fileName;
		String contentType;
		if (idMov != null && idMov != 0) {
			ExMovimentacao mov = dao().consultar(idMov, ExMovimentacao.class, false);

			fileName = arq.getReferencia() + "_" + mov.getIdMov() + ".p7s";
			contentType = mov.getConteudoTpMov();

			bytes = mov.getConteudoBlobMov2();

		} else {
			fileName = arq.getReferenciaPDF();
			contentType = "application/pdf";

			if (assinado)
				bytes = Ex.getInstance().getBL().obterPdfPorProtocolo(n);
			else
				bytes = arq.getPdf();
		}
		if (bytes == null) {
			throw new AplicacaoException("Arquivo não encontrado para Download.");
		}
		final boolean fB64 = getRequest().getHeader("Accept") != null
				&& getRequest().getHeader("Accept").startsWith("text/vnd.siga.b64encoded");
		if (certificadoB64 != null) {
			final Date dt = dao().consultarDataEHoraDoServidor();
			getResponse().setHeader("Atributo-Assinavel-Data-Hora", Long.toString(dt.getTime()));

			// Chamar o BluC para criar o pacote assinavel
			//
			BlucService bluc = Service.getBlucService();
			HashRequest hashreq = new HashRequest();
			hashreq.setCertificate(certificadoB64);
			hashreq.setCrl("true");
			hashreq.setPolicy("AD-RB");
			hashreq.setSha1(bluc.bytearray2b64(bluc.calcSha1(bytes)));
			hashreq.setSha256(bluc.bytearray2b64(bluc.calcSha256(bytes)));
			hashreq.setTime(dt);
			HashResponse hashresp = bluc.hash(hashreq);
			if (hashresp.getErrormsg() != null)
				throw new Exception("BluC não conseguiu produzir o pacote assinável. " + hashresp.getErrormsg());
			byte[] sa = Base64.decode(hashresp.getHash());

			return new InputStreamDownload(makeByteArrayInputStream(sa, fB64), APPLICATION_OCTET_STREAM, null);
		}
		return new InputStreamDownload(makeByteArrayInputStream(bytes, fB64), contentType, fileName);
	}

	private ByteArrayInputStream makeByteArrayInputStream(final byte[] content, final boolean fB64) {
		final byte[] conteudo = (fB64 ? Base64.encodeBytes(content).getBytes() : content);
		return (new ByteArrayInputStream(conteudo));
	}

	// antigo metodo arquivo();
	@Get("/public/app/processoArquivoAutenticado")
	public void processoArquivoAutenticado(final String jwt) throws Exception {
		if (jwt == null) {
			setDefaultResults();
			result.redirectTo(URL_EXIBIR);
			return;
		}
		String n = verifyJwtToken(jwt).get("n").toString();

		ExArquivo arq = Ex.getInstance().getBL().buscarPorProtocolo(n);

		Set<ExMovimentacao> assinaturas = arq.getAssinaturasDigitais();

		/*
		 * ExMovimentacao mov = null; if (arq.isCodigoParaAssinaturaExterna(n)) { mov =
		 * (ExMovimentacao) arq; }
		 */
		setDefaultResults();
		result.include("assinaturas", assinaturas);
		// result.include("mov", mov);
		result.include("n", n);
		result.include("jwt", jwt);
		if (arq instanceof ExDocumento) {
			ExMobil mob = null;
			ExDocumento doc = (ExDocumento) arq;
			if (doc.isFinalizado()) {
				if (doc.isProcesso()) {
					mob = doc.getUltimoVolume();
				} else {
					mob = doc.getPrimeiraVia();
				}
			}

			final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();

			exDocumentoDTO.setSigla(doc.getSigla());
			buscarDocumento(false, exDocumentoDTO);

			if (doc.isFinalizado()) {
				if (doc.isProcesso()) {
					mob = doc.getUltimoVolume();
				} else {
					mob = doc.getPrimeiraVia();
				}
			}

			List<ExMovimentacao> lista = new ArrayList<ExMovimentacao>();
			List<ExMobil> lstMobil = dao().consultarMobilPorDocumento(doc);
			for (ExMobil m : lstMobil)
				lista.addAll(dao().consultarMovimentoPorMobil(m));
			lista.addAll(doc.getAutenticacoesComSenha());
			
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

			final ExDocumentoVO docVO = new ExDocumentoVO(doc, mob, getCadastrante(), p, l, true, true);
			docVO.exibe();
			result.include("movs", lista);
			result.include("msg", exDocumentoDTO.getMsg());
			result.include("docVO", docVO);
			result.include("mob", exDocumentoDTO.getMob());

		}
	}

	private static String getRecaptchaSiteKey() {
		String pwd = null;
		try {
			pwd = System.getProperty("siga.ex.autenticacao.recaptcha.key");
			if (pwd == null)
				throw new AplicacaoException("Erro obtendo propriedade siga.ex.autenticacao.recaptcha.key");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo propriedade siga.ex.autenticacao.recaptcha.key", 0, e);
		}
	}

	private static String getRecaptchaSitePassword() {
		String pwd = null;
		try {
			pwd = System.getProperty("siga.ex.autenticacao.recaptcha.pwd");
			if (pwd == null)
				throw new AplicacaoException("Erro obtendo propriedade siga.ex.autenticacao.recaptcha.pwd");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo propriedade siga.ex.autenticacao.recaptcha.pwd", 0, e);
		}
	}

	private static String getJwtPassword() {
		String pwd = null;
		try {
			pwd = System.getProperty("siga.ex.autenticacao.pwd");
			if (pwd == null)
				throw new AplicacaoException("Erro obtendo propriedade siga.ex.autenticacao.pwd");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo propriedade siga.ex.autenticacao.pwd", 0, e);
		}
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

	/*
	 * Base de historico
	 */

	private void buscarDocumento(final boolean fVerificarAcesso, final ExDocumentoDTO exDocumentoDTO) {
		buscarDocumento(fVerificarAcesso, false, exDocumentoDTO);
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
			final String id = param("exDocumentoDto.id");
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

}
