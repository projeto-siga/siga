/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Criado em  13/09/2005
 *
 */
package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.lowagie.text.pdf.codec.Base64;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.bluc.service.BlucService;
import br.gov.jfrj.siga.bluc.service.HashRequest;
import br.gov.jfrj.siga.bluc.service.HashResponse;
import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.api.v1.DocumentosSiglaArquivoGet;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeAcessarDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.vraptor.builder.ExDownloadRTF;
import br.gov.jfrj.siga.vraptor.builder.ExDownloadZip;
import br.gov.jfrj.siga.vraptor.builder.ExInputStreamDownload;

@Controller
public class ExArquivoController extends ExController {

	private static final String TEXT_HTML = "text/html";
	private static final String APPLICATION_PDF = "application/pdf";
	private static final String TEXT_PLAIN = "text/plain";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static byte[] idPattern = "/ModDate(D:20".getBytes();
	private static int[] failure = computeFailure();

	/**
	 * @deprecated CDI eyes only
	 */
	public ExArquivoController() {
		super();
	}

	@Inject
	public ExArquivoController(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@TrackRequest
	@Get("/app/arquivo/exibir")
	public Download aExibir(final String sigla, final boolean popup, final String arquivo, byte[] certificado,
			String hash, final String HASH_ALGORITHM, final String certificadoB64, boolean completo,
			final boolean semmarcas, final boolean volumes, final Long idVisualizacao, boolean exibirReordenacao, 
							boolean iframe, final String nomeAcao, boolean tamanhoOriginal, final Integer paramoffset) throws Exception {
		try {						
			final String servernameport = getRequest().getServerName() + ":" + getRequest().getServerPort();
			final String contextpath = getRequest().getContextPath();
			final boolean pacoteAssinavel = (certificadoB64 != null);
			final boolean fB64 = getRequest().getHeader("Accept") != null
					&& getRequest().getHeader("Accept").startsWith("text/vnd.siga.b64encoded");
			final boolean fJSON = getRequest().getHeader("Accept") != null
					&& getRequest().getHeader("Accept").startsWith("application/json");
			final boolean isPdf = arquivo.endsWith(".pdf");
			final boolean isHtml = arquivo.endsWith(".html");
			boolean estampar = !semmarcas;
			final boolean somenteHash = (hash != null) || (HASH_ALGORITHM != null);
			if (somenteHash) {
				if (hash == null)
					hash = HASH_ALGORITHM;
				if (hash != null) {
					if (!(hash.equals("SHA1") || hash.equals("SHA-256") || hash.equals("SHA-512")
							|| hash.equals("MD5"))) {
						throw new AplicacaoException(
								"Algoritmo de hash inválido. Os permitidos são: SHA1, SHA-256, SHA-512 e MD5.");
					}
				}
				completo = false;
				estampar = false;
			}
			if (pacoteAssinavel) {
				certificado = Base64.decode(certificadoB64);
				completo = false;
				estampar = false;
			}
			final ExMobil mob = Documento.getMobil(arquivo);	
				
			if (mob != null) {
				mob.getMobilPrincipal().indicarSeDeveExibirDocumentoCompletoReordenado(exibirReordenacao);

				if (sigla != null && !sigla.isEmpty()) {
					ExMobil mobilDoDocumentoPrincipal = Documento.getMobil(sigla);
					if (mobilDoDocumentoPrincipal != null) {
						mob.getMobilPrincipal().getDoc()
								.setIdDocPrincipal(mobilDoDocumentoPrincipal.getDoc().getIdDoc());
					}
				}

			} else {
				throw new AplicacaoException("A sigla informada não corresponde a um documento da base de dados.");
			}
			if (!Ex.getInstance().getComp().pode(ExPodeAcessarDocumento.class, getTitular(), getLotaTitular(), mob)
					&& !podeVisualizarDocumento(mob, getTitular(), idVisualizacao)) {
				throw new AplicacaoException("Documento " + mob.getSigla() + " inacessível ao usuário "
						+ getTitular().getSigla() + "/" + getLotaTitular().getSiglaCompleta() + ".");
			}
			final ExMovimentacao mov = Documento.getMov(mob, arquivo);
			final boolean isArquivoAuxiliar = mov != null && mov.getExTipoMovimentacao()
					.equals(ExTipoDeMovimentacao.ANEXACAO_DE_ARQUIVO_AUXILIAR);
			final boolean isArquivoDOE= mov != null && mov.getExTipoMovimentacao()
					.equals(ExTipoDeMovimentacao.AGENDAR_PUBLICACAO_DOE);
			
			final boolean imutavel = (mov != null) && !completo && !estampar && !somenteHash && !pacoteAssinavel;
			String cacheControl = "private";
			final Integer grauNivelAcesso = mob.doc().getExNivelAcesso().getGrauNivelAcesso();
			if (ExNivelAcesso.NIVEL_ACESSO_PUBLICO == grauNivelAcesso
					|| ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS == grauNivelAcesso) {
				cacheControl = "public";
			}
			byte ab[] = null;
			if (isArquivoAuxiliar || isArquivoDOE) {
				ab = mov.getConteudoBlobMov2();
				return new InputStreamDownload(makeByteArrayInputStream(ab, fB64), APPLICATION_OCTET_STREAM,
						mov.getNmArqMov().replaceAll(",", "").replaceAll(";", ""));
			}

			if ((isPdf || isHtml) && completo && mob != null && mov == null) {
				DocumentosSiglaArquivoGet act = new DocumentosSiglaArquivoGet();
				DocumentosSiglaArquivoGet.Request req = new DocumentosSiglaArquivoGet.Request();
				DocumentosSiglaArquivoGet.Response resp = new DocumentosSiglaArquivoGet.Response();
				req.sigla = mob.getSigla();
				req.contenttype = isPdf ? "application/pdf" : "text/html";
				req.estampa = estampar;
				req.completo = completo;
				req.volumes = volumes;
				req.exibirReordenacao = exibirReordenacao;
				req.tamanhoOriginal = tamanhoOriginal;
				
				if (Documento.exibirPaginacaoPdfDocCompleto()) 
					req.paramoffset = paramoffset;
				
				String filename = isPdf ? (volumes ? mob.doc().getReferenciaPDF() : mob.getReferenciaPDF())
						: (volumes ? mob.doc().getReferenciaHtml() : mob.getReferenciaHtml());
				boolean reduzirVisuAssinPdf = Documento.isReduzirVisuAssinPdf(getTitular(), getLotaTitular(), mob.doc());
				
				DocumentosSiglaArquivoGet.iniciarGeracaoDePdf(req, resp, ContextoPersistencia.getUserPrincipal(),
						filename, contextpath, servernameport, reduzirVisuAssinPdf);
				result.redirectTo("/app/arquivo/status/" + mob.getCodigoCompacto() + "/" + resp.uuid + "/"
						+ resp.jwt + "/" + filename);
				return null;
			}

			if (isPdf) {
				if (mov != null && !completo && !estampar && hash == null) {
					ab = mov.getConteudoBlobpdf();
				} else {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					boolean reduzirVisuAssinPdf = Documento.isReduzirVisuAssinPdf(getTitular(), getLotaTitular(), mob.doc());
					Documento.getDocumento(baos, null, mob, mov, completo, estampar, volumes, hash, null, tamanhoOriginal, null, reduzirVisuAssinPdf);
					ab = baos.toByteArray();
				}
				if (ab == null) {
					throw new Exception("PDF inválido!");
				}
				if (pacoteAssinavel) {
					final Date dt = dao().consultarDataEHoraDoServidor();
					getResponse().setHeader("Atributo-Assinavel-Data-Hora", Long.toString(dt.getTime()));

					// Chamar o BluC para criar o pacote assinavel
					//
					BlucService bluc = Service.getBlucService();
					HashRequest hashreq = new HashRequest();
					hashreq.setCertificate(certificadoB64);
					hashreq.setCrl("true");
					hashreq.setPolicy("AD-RB");
					hashreq.setSha1(bluc.bytearray2b64(bluc.calcSha1(ab)));
					hashreq.setSha256(bluc.bytearray2b64(bluc.calcSha256(ab)));
					hashreq.setTime(dt);
					HashResponse hashresp = bluc.hash(hashreq);
					if (hashresp.getErrormsg() != null)
						throw new AplicacaoException(
								"BluC não conseguiu produzir o pacote assinável. " + hashresp.getErrormsg());
					byte[] sa = Base64.decode(hashresp.getHash());

					return new InputStreamDownload(makeByteArrayInputStream(sa, fB64), APPLICATION_OCTET_STREAM,
							arquivo);
				}
				if (hash != null) {
					return new InputStreamDownload(makeByteArrayInputStream(ab, fB64), APPLICATION_OCTET_STREAM,
							arquivo);
				}
			}
			if (isHtml) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Documento.getDocumentoHTML(baos, null, mob, mov, completo, volumes, contextpath, servernameport);
				ab = baos.toByteArray();
				if (ab == null) {
					throw new AplicacaoException("HTML inválido!");
				}
			}
			if (imutavel) {
				getResponse().setHeader("Cache-Control", cacheControl);
				getResponse().setDateHeader("Expires", new Date().getTime() + (365 * 24 * 3600 * 1000L));
			} else {
				final MessageDigest md = MessageDigest.getInstance("MD5");
				final int m = match(ab);
				if (m != -1) {
					md.update(ab, 0, m);
				} else {
					md.update(ab);
				}
				final String etag = Base64.encodeBytes(md.digest());
				final String ifNoneMatch = getRequest().getHeader("If-None-Match");
				getResponse().setHeader("Cache-Control", "must-revalidate, " + cacheControl);
				getResponse().setDateHeader("Expires", (new Date()).getTime() + 30000);
				getResponse().setHeader("ETag", etag);

				if ((etag).equals(ifNoneMatch) && ifNoneMatch != null) {
					getResponse().sendError(HttpServletResponse.SC_NOT_MODIFIED);
					return new InputStreamDownload(makeByteArrayInputStream((new byte[0]), false), TEXT_PLAIN,
							"arquivo inválido");
				}
			}
			getResponse().setHeader("Pragma", "");
			return new InputStreamDownload(makeByteArrayInputStream(ab, fB64), checkDownloadType(ab, isPdf, fB64),
					arquivo);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals("ClientAbortException")) {
				return new InputStreamDownload(makeByteArrayInputStream((new byte[0]), false), TEXT_PLAIN,
						"arquivo inválido");
			}
			
			result.include("iframe", iframe ? "sim" : "");
			throw e;
		}
	}


	@Get("/app/arquivo/status/{sigla}/{uuid}/{jwt}/{filename}")
	public void status(String sigla, String uuid, String jwt, String filename) {
		result.include("sigla", sigla);
		result.include("uuid", uuid);
		result.include("jwt", jwt);
		result.include("filename", filename);
	}
	
	@Get("/app/arquivo/obterTamanhoArquivosDocs")
	public void obterTamanhoArquivosDocs(final String arquivo, boolean completo, final boolean volumes)  throws Exception {
		String json = Documento.obterTamanhoArquivosDocs(arquivo, completo, volumes);
		setMensagem(json);
		result.use(Results.page()).forwardTo("/WEB-INF/page/textoAjax.jsp");
	}
	
	@Get("/public/app/arquivo/obterDownloadDocumento")
	public Download aObterDownloadDocumento(final String t, boolean completo, final boolean semmarcas, final boolean volumes, final String mime, final boolean tamanhoOriginal) throws Exception  {
		try {

			boolean isPdf = "PDF".equalsIgnoreCase(mime);
			boolean isHtml = "HTML".equalsIgnoreCase(mime); /*TODO: implementar*/
			
			final String servernameport = getRequest().getServerName() + ":" + getRequest().getServerPort();
			final String contextpath = getRequest().getContextPath();
			
			String token = verifyJwtToken(t).get("token").toString();
			
			CpToken cpToken = new CpToken();
			cpToken = dao().obterCpTokenPorTipoToken(CpToken.TOKEN_URLPERMANENTE, token);
			
			ExDocumento doc = Ex.getInstance().getBL().buscarDocumentoPorLinkPermanente(cpToken);
	
			final ExMobil mob = doc.getPrimeiroMobil();	
			if (mob == null) {				
				throw new RuntimeException("A sigla informada não corresponde a um documento da base de dados.");
			}
			
			/* Caso documento tenha URL permanente mas não tenha acesso público*/
			if ( doc.getExNivelAcessoAtual().getGrauNivelAcesso() != ExNivelAcesso.NIVEL_ACESSO_PUBLICO ) {
				throw new RuntimeException("Documento não está disponível para acesso público.");
			}
			
			
			if ((isPdf || isHtml) && completo && mob != null) {
				DocumentosSiglaArquivoGet act = new DocumentosSiglaArquivoGet();
				DocumentosSiglaArquivoGet.Request req = new DocumentosSiglaArquivoGet.Request();
				DocumentosSiglaArquivoGet.Response resp = new DocumentosSiglaArquivoGet.Response();
				req.sigla = mob.getSigla();
				req.contenttype = isPdf ? "application/pdf" : "text/html";
				req.estampa = true;
				req.completo = completo;
				req.volumes = volumes;
				req.exibirReordenacao = false;
				String filename = isPdf ? (volumes ? mob.doc().getReferenciaPDF() : mob.getReferenciaPDF())
						: (volumes ? mob.doc().getReferenciaHtml() : mob.getReferenciaHtml());
				boolean reduzirVisuAssinPdf = Documento.isReduzirVisuAssinPdf(getTitular(), getLotaTitular(), mob.doc());
				
				DocumentosSiglaArquivoGet.iniciarGeracaoDePdf(req, resp, null,
						filename, contextpath, servernameport, reduzirVisuAssinPdf);
			
				result.forwardTo(this).status(mob.getCodigoCompacto(), resp.uuid, resp.jwt, filename);

				return null;
			}
			
		
			
			byte ab[] = null;
	
			if (isPdf) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				boolean reduzirVisuAssinPdf = Documento.isReduzirVisuAssinPdf(getTitular(), getLotaTitular(), mob.doc());
				Documento.getDocumento(baos, null, mob, null, completo, semmarcas, volumes, null, null, tamanhoOriginal, null, reduzirVisuAssinPdf);
				ab = baos.toByteArray();
				
				if (ab == null) {
					throw new Exception("Arquivo PDF inválido!");
				}
	
				String fileName = mob.getSigla().replace("-", "").replace("/", "");
				fileName = fileName + ".pdf";
				
				return new InputStreamDownload(makeByteArrayInputStream(ab, false), checkDownloadType(ab, isPdf, false), fileName);
			} 

			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}

	@Get("/app/arquivo/download")
	public Download download(String arquivo, String hash, HttpServletResponse response) throws Exception {
		boolean isZip = arquivo.endsWith(".zip");
		boolean somenteHash = hash != null || getPar().containsKey("HASH_ALGORITHM");
		String algoritmoHash = getAlgoritmoHash(hash);
		ExMobil mob = Documento.getMobil(arquivo);
		ExMovimentacao mov = Documento.getMov(mob, arquivo);

		validarDownload(somenteHash, algoritmoHash, mob);

		if (isZip) {
			if (algoritmoHash != null) {
				return new ExDownloadZip(mov, algoritmoHash);
			}
			return iniciarDownload(mob, new ExDownloadZip(mov, algoritmoHash, ExInputStreamDownload.MEDIA_TYPE_ZIP));
		} else {
			if (algoritmoHash != null) {
				return new ExDownloadRTF(mob, algoritmoHash);
			}
			return iniciarDownload(mob, new ExDownloadRTF(mob, algoritmoHash, ExInputStreamDownload.MEDIA_TYPE_RTF));
		}
	}

	private Download iniciarDownload(ExMobil mob, ExInputStreamDownload exDownload) {
		try {
			// Calcula o hash do documento, mas não leva em consideração
			// para fins de hash os últimos bytes do arquivos, pois lá
			// fica armazanada a ID e as datas de criação e modificação
			// e estas são sempre diferente de um pdf para o outro.
			MessageDigest md = MessageDigest.getInstance("MD5");

			byte ab[] = exDownload.getBytes();
			int m = match(ab);
			if (m != -1)
				md.update(ab, 0, m);
			else
				md.update(ab);

			String etag = Base64.encodeBytes(md.digest());
			String ifNoneMatch = getRequest().getHeader("If-None-Match");
			getResponse().setHeader("Cache-Control", "must-revalidate, " + getCacheControl(mob));
			getResponse().setDateHeader("Expires", 0);
			getResponse().setHeader("ETag", etag);
			getResponse().setHeader("Pragma", "");

			if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
				getResponse().sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return null;
			}
			return exDownload;
		} catch (Exception e) {
			throw new AplicacaoException("erro na geração do documento.");
		}
	}

	private String getAlgoritmoHash(String hash) {
		String[] value = getPar().get("HASH_ALGORITHM");
		if (value != null && value.length > 0) {
			return value[0];
		}
		return hash;
	}

	private String getCacheControl(ExMobil mob) {
		String cacheControl = "private";
		final Integer grauNivelAcesso = mob.doc().getExNivelAcesso().getGrauNivelAcesso();
		if (ExNivelAcesso.NIVEL_ACESSO_PUBLICO == grauNivelAcesso
				|| ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS == grauNivelAcesso)
			cacheControl = "public";
		return cacheControl;
	}

	private void validarDownload(boolean somenteHash, String algoritmoHash, ExMobil mob) {
		if (somenteHash) {
			if (algoritmoHash != null) {
				if (!(algoritmoHash.equals("SHA1") || algoritmoHash.equals("SHA-256") || algoritmoHash.equals("SHA-512")
						|| algoritmoHash.equals("MD5")))
					throw new AplicacaoException(
							"Algoritmo de hash inválido. Os permitidos são: SHA1, SHA-256, SHA-512 e MD5.");
			}
		}

		if (mob == null) {
			throw new AplicacaoException("A sigla informada não corresponde a um documento da base de dados.");
		}

		if (!Ex.getInstance().getComp().pode(ExPodeAcessarDocumento.class, getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Documento " + mob.getSigla() + " inacessível ao usuário "
					+ getTitular().getSigla() + "/" + getLotaTitular().getSiglaCompleta() + ".");
		}
	}

	private ByteArrayInputStream makeByteArrayInputStream(final byte[] content, final boolean fB64) {
		final byte[] conteudo = (fB64 ? Base64.encodeBytes(content).getBytes() : content);
		return (new ByteArrayInputStream(conteudo));
	}

	private String checkDownloadType(final byte[] content, final boolean isPdf, final boolean isFB64) {
		String contentType;
		if (isFB64) {
			contentType = TEXT_PLAIN;
		} else {
			if (isPdf) {
				contentType = APPLICATION_PDF;
			} else {
				contentType = TEXT_HTML;
			}
		}
		return contentType;
	}

	private static int match(byte[] text) {
		int j = 0;
		if (text.length == 0) {
			return -1;
		}

		for (int i = 0; i < text.length; i++) {
			while (j > 0 && idPattern[j] != text[i]) {
				j = failure[j - 1];
			}
			if (idPattern[j] == text[i]) {
				j++;
			}
			if (j == idPattern.length) {
				return i - idPattern.length + 1;
			}
		}
		return -1;
	}

	private static int[] computeFailure() {
		failure = new int[idPattern.length];
		int j = 0;
		for (int i = 1; i < idPattern.length; i++) {
			while (j > 0 && idPattern[j] != idPattern[i]) {
				j = failure[j - 1];
			}
			if (idPattern[j] == idPattern[i]) {
				j++;
			}
			failure[i] = j;
		}
		return failure;
	}
	
	
	private static String getJwtPassword() {
		String pwd = null;
		try {
			pwd = Prop.get("/siga.autenticacao.senha");
			if (pwd == null)
				throw new AplicacaoException("Erro obtendo propriedade siga.autenticacao.senha");
			return pwd;
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo propriedade siga.autenticacao.senha", 0, e);
		}
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
	
	@TrackRequest
	@Get("/app/arquivo/downloadFormatoLivre")
	public void downloadFormatoLivre(final String sigla) throws Exception {
		ExMobil mob = Documento.getMobil(sigla);
		validarDownload(true, null, mob);

		CpArquivo cpArq = mob.getDoc().getCpArquivoFormatoLivre();
		if (cpArq == null) {
			result.include("mensagemCabec", "Arquivo não existente ou não autorizado para download.");
			result.include("msgCabecClass", "alert-danger mt-2");
			return;
		}
		
		final JWTSigner signer = new JWTSigner(System.getProperty("siga.jwt.secret"));
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("iat", System.currentTimeMillis() / 1000L);
		claims.put("nomeArqS3", cpArq.getCaminho());
		claims.put("nomeArq", cpArq.getNomeArquivo());
		claims.put("hash", cpArq.getHashSha256());
        String tk = signer.sign(claims);
		
		result.include("token", tk);
		result.redirectTo(Prop.get("/sigaarq.url") + "/api/v1/download?tokenArquivo=" + tk);
	}
	

}