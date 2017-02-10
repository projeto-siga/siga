package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;
import nl.captcha.noise.StraightLineNoiseProducer;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.bluc.service.BlucService;
import br.gov.jfrj.siga.bluc.service.HashRequest;
import br.gov.jfrj.siga.bluc.service.HashResponse;
import br.gov.jfrj.siga.ex.ExArquivo;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;
import br.gov.jfrj.siga.hibernate.ExDao;

import com.lowagie.text.pdf.codec.Base64;

@Resource
public class ExAutenticacaoController extends ExController {
	private static final String URL_EXIBIR = "/app/externo/autenticar";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String APPLICATION_PDF = "application/pdf";

	public ExAutenticacaoController(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	private void setDefaultResults() {
		result.include("request", getRequest());
	}

	@Get("/app/externo/captcha")
	public Download captcha(final String sc, final String ts) throws Exception {

		if (sc != null && sc.trim().length() != 0) {
			Captcha captcha = new Captcha.Builder(150, 75)
					.addNoise(new StraightLineNoiseProducer()).addText()
					.addBackground().gimp().addBorder().build();
			getRequest().getSession().setAttribute(Captcha.NAME, captcha);
			try (ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream()) {
				ImageIO.write(captcha.getImage(), "png", imgOutputStream);
				byte[] bytes = imgOutputStream.toByteArray();
				final String fileName = "captch.png";
				final String contentType = "image/png";
				return new ByteArrayDownload(bytes, contentType, fileName, true);
			}
		}
		return null;
	}

	@Get
	@Path("/autenticar.action")
	public void redirecionar() throws Exception {
		result.redirectTo(this).autenticar(null, null, null, null, null, null);
	}

	// antigo metodo exec()
	@Get
	@Post
	@Path("/app/externo/autenticar")
	public void autenticar(final String n, final String answer,
			final String ass, final String assinaturaB64,
			final String certificadoB64, final String atributoAssinavelDataHora)
			throws Exception {

		Captcha captcha = (Captcha) getRequest().getSession().getAttribute(
				Captcha.NAME);

		if (captcha == null || n == null || n.trim().length() == 0
				|| answer == null) {
			setDefaultResults();
			return;
		}

		if (!captcha.isCorrect(answer)) {
			setMensagem("Caracteres digitados não conferem com a imagem apresentada. Por favor, tente novamente.");
			setDefaultResults();
			return;
		}

		ExArquivo arq = Ex.getInstance().getBL().buscarPorNumeroAssinatura(n);
		Set<ExMovimentacao> assinaturas = arq.getAssinaturasDigitais();
		boolean mostrarBotaoAssinarExterno = arq
				.isCodigoParaAssinaturaExterna(n);

		ExMovimentacao mov = null;
		if (mostrarBotaoAssinarExterno) {
			mov = (ExMovimentacao) arq;
		}

		if (ass != null && ass.trim().length() != 0) {
			byte[] assinatura = Base64.decode(assinaturaB64 == null ? ""
					: assinaturaB64);
			byte[] certificado = Base64.decode(certificadoB64 == null ? ""
					: certificadoB64);
			Date dt = mov.getDtMov();
			if (certificado != null && certificado.length != 0)
				dt = new Date(Long.valueOf(atributoAssinavelDataHora));
			else
				certificado = null;

			try {
				Ex.getInstance()
						.getBL()
						.assinarMovimentacao(
								null,
								null,
								mov,
								dt,
								assinatura,
								certificado,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO);
			} catch (final Exception e) {
				throw new AplicacaoException(e.getMessage());
			}
		}

		setDefaultResults();
		result.include("n", n);
		result.include("answer", answer);
		result.include("assinaturas", assinaturas);
		result.include("mov", mov);
		result.include("mostrarBotaoAssinarExterno", mostrarBotaoAssinarExterno);
		result.include("ass", ass);
		result.include("assinaturaB64", assinaturaB64);
		result.include("certificadoB64", certificadoB64);
		result.include("atributoAssinavelDataHora", atributoAssinavelDataHora);
		result.forwardTo(this).arquivoAutenticado(n, answer);
	}

	@Get("/app/externo/arquivoAutenticado_stream")
	public Download arquivoAutenticado_stream(final String n,
			final String answer, final boolean assinado, final Long idMov, 
			final String certificadoB64)
			throws Exception {

		Captcha captcha = (Captcha) getRequest().getSession().getAttribute(
				Captcha.NAME);
		if (captcha == null || n == null || n.trim().length() == 0
				|| answer == null) {
			result.redirectTo(URL_EXIBIR);
			return null;
		}

		if (captcha.isCorrect(answer)) {
			ExArquivo arq = Ex.getInstance().getBL()
					.buscarPorNumeroAssinatura(n);

			byte[] bytes;
			String fileName;
			String contentType;
			if (idMov != null && idMov != 0) {
				ExMovimentacao mov = dao().consultar(idMov,
						ExMovimentacao.class, false);

				fileName = arq.getReferencia() + "_" + mov.getIdMov() + ".p7s";
				contentType = mov.getConteudoTpMov();

				bytes = mov.getConteudoBlobMov2();

			} else {
				fileName = arq.getReferenciaPDF();
				contentType = "application/pdf";

				if (assinado)
					bytes = Ex.getInstance().getBL()
							.obterPdfPorNumeroAssinatura(n);
				else
					bytes = arq.getPdf();
			}
			if (bytes == null) {
				throw new AplicacaoException(
						"Arquivo não encontrado para Download.");
			}
			final boolean fB64 = getRequest().getHeader("Accept") != null && getRequest().getHeader("Accept").startsWith("text/vnd.siga.b64encoded");
			if (certificadoB64 != null){
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
			
			return new InputStreamDownload(makeByteArrayInputStream(bytes, fB64), APPLICATION_PDF, null);
			
		} else {
			result.redirectTo(URL_EXIBIR);
			return null;
		}
	}
	
	private ByteArrayInputStream makeByteArrayInputStream(final byte[] content, final boolean fB64) {
		final byte[] conteudo = (fB64 ? Base64.encodeBytes(content).getBytes() : content);
		return (new ByteArrayInputStream(conteudo));
	}

	// antigo metodo arquivo();
	@Get("/app/externo/arquivoAutenticado")
	public void arquivoAutenticado(final String n, final String answer)
			throws Exception {
		Captcha captcha = (Captcha) getRequest().getSession().getAttribute(
				Captcha.NAME);

		if (captcha == null || n == null || n.trim().length() == 0
				|| answer == null) {
			setDefaultResults();
			result.redirectTo(URL_EXIBIR);
			return;
		}

		if (!captcha.isCorrect(answer)) {
			setMensagem("Caracteres digitados não conferem com a imagem apresentada. Por favor, tente novamente.");
 }

		ExArquivo arq = Ex.getInstance().getBL().buscarPorNumeroAssinatura(n);
		Set<ExMovimentacao> assinaturas = arq.getAssinaturasDigitais();

		ExMovimentacao mov = null;
		if (arq.isCodigoParaAssinaturaExterna(n)) {
			mov = (ExMovimentacao) arq;
		}

		setDefaultResults();
		result.include("assinaturas", assinaturas);
		result.include("mov", mov);
		result.include("n", n);
		result.include("answer", answer);

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

			final ExDocumentoVO docVO = new ExDocumentoVO(doc, mob,
					doc.getSubscritor(), doc.getLotaSubscritor(), true, false);

			docVO.exibe();

			result.include("docVO", docVO);
		}
	}
}
