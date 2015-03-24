package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.pdf.codec.Base64;

import nl.captcha.Captcha;
import nl.captcha.noise.StraightLineNoiseProducer;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExArquivo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;

@Resource
public class ExAutenticacaoController extends ExController{
	private static final String URL_EXIBIR = "/app/externo/autenticar";

	public ExAutenticacaoController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}
	
	private void setDefaultResults(){
		result.include("request", getRequest());
	}
	
	@Get("/app/externo/captcha")
	public Download captcha(final String sc, 
							final String ts) throws Exception{
		
		if (sc != null && sc.trim().length() != 0) {
			Captcha captcha = new Captcha.Builder(150, 75)
					.addNoise(new StraightLineNoiseProducer()).addText()
					.addBackground().gimp().addBorder().build();
			getRequest().getSession().setAttribute(Captcha.NAME, captcha);
			ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "png", imgOutputStream);
			byte[] bytes = imgOutputStream.toByteArray();
			final String fileName = "captch.png";
			final String contentType = "image/png";
			return new ByteArrayDownload(bytes,contentType,fileName,true);
		}
		return null;
	}
	
//	antigo metodo exec()
	@Get
	@Post
	@Path("/app/externo/autenticar")
	public void autenticar(final String n, 
						   final String answer,
						   final String ass,
						   final String assinaturaB64,
						   final String certificadoB64,
						   final String atributoAssinavelDataHora) throws Exception{

		Captcha captcha = (Captcha) getRequest().getSession().getAttribute(Captcha.NAME);

		if (captcha == null || n == null || n.trim().length() == 0 || answer == null) {
			setDefaultResults();
			return;
		}

		if (captcha.isCorrect(answer)) {
			ExArquivo arq = Ex.getInstance().getBL().buscarPorNumeroAssinatura(n);
			Set<ExMovimentacao> assinaturas = arq.getAssinaturasDigitais();			
			boolean mostrarBotaoAssinarExterno = arq.isCodigoParaAssinaturaExterna(n);
			
			ExMovimentacao mov = null;
			if (mostrarBotaoAssinarExterno){
				mov = (ExMovimentacao) arq;
			}
			
			if (ass != null && ass.trim().length() != 0) {
				byte[] assinatura = Base64.decode(assinaturaB64 == null ? "" : assinaturaB64);
				byte[] certificado = Base64.decode(certificadoB64 == null ? "" : certificadoB64);
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
			result.include("n",n);
			result.include("answer",answer);
			result.include("assinaturas",assinaturas);
			result.include("mov",mov);
			result.include("mostrarBotaoAssinarExterno",mostrarBotaoAssinarExterno);
			result.include("ass",ass);
			result.include("assinaturaB64",assinaturaB64);
			result.include("certificadoB64",certificadoB64);
			result.include("atributoAssinavelDataHora",atributoAssinavelDataHora);
			result.forwardTo(this).arquivoAutenticado(n, answer);
			return;
		} else {
			setMensagem("Não foi possível fazer o Download do arquivo. Por favor, tente novamente.");
		}
		setDefaultResults();
	}
	
	@Get("/app/externo/arquivoAutenticado_stream")
	public Download arquivoAutenticado_stream(final String n, 
											  final String answer, 
											  final boolean assinado, 
											  final Long idMov) throws Exception{
		
		Captcha captcha = (Captcha) getRequest().getSession().getAttribute(Captcha.NAME);
		if (captcha == null || n == null || n.trim().length() == 0 || answer == null) {
			result.redirectTo(URL_EXIBIR);
			return null;
		}
		
		if (captcha.isCorrect(answer)) {
			ExArquivo arq = Ex.getInstance().getBL().buscarPorNumeroAssinatura(n);
						
			byte[] bytes;
			String fileName;
			String contentType;
			if (idMov != null && idMov != 0) {
				ExMovimentacao mov = dao().consultar(idMov,ExMovimentacao.class, false);

				fileName = arq.getReferencia() + "_" + mov.getIdMov() + ".p7s";
				contentType = mov.getConteudoTpMov();

				bytes = mov.getConteudoBlobMov2();
				
				if (bytes == null){
					throw new AplicacaoException("Arquivo não encontrado para Download.");
				}else{
					return new ByteArrayDownload(bytes,contentType,fileName,false);					
				}

			} else {
				fileName = arq.getReferenciaPDF();
				contentType = "application/pdf";

				if (assinado)
					bytes = Ex.getInstance().getBL().obterPdfPorNumeroAssinatura(n);
				else
					bytes = arq.getPdf();
				
				if (bytes == null){
					throw new AplicacaoException("Arquivo não encontrado para Download.");
				}else{
					return new ByteArrayDownload(bytes,contentType,fileName,false);					
				}		
			}
		}else{
			result.redirectTo(URL_EXIBIR);
			return null;
		}
	}
	
//	antigo metodo arquivo();
	@Get("/app/externo/arquivoAutenticado")
	public void arquivoAutenticado(final String n, 
								   final String answer) throws Exception{
		Captcha captcha = (Captcha) getRequest().getSession().getAttribute(Captcha.NAME);

		if (captcha == null || n == null || n.trim().length() == 0 || answer == null) {
			setDefaultResults();
			result.redirectTo(URL_EXIBIR);
			return;
		}
		
		if (!captcha.isCorrect(answer)) {
			setMensagem("Não foi possível fazer o Download do arquivo. Por favor, tente novamente.");
		}
		
		ExArquivo arq = Ex.getInstance().getBL().buscarPorNumeroAssinatura(n);
		Set<ExMovimentacao> assinaturas = arq.getAssinaturasDigitais();
		
		ExMovimentacao mov = null;
		if (arq.isCodigoParaAssinaturaExterna(n)){
			mov = (ExMovimentacao) arq;
		}
		
		setDefaultResults();
		result.include("assinaturas", assinaturas);
		result.include("mov",mov);
		result.include("n",n);
		result.include("answer",answer);
	}

}
