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
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cd.service.CdService;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.util.GeradorRTF;
import br.gov.jfrj.siga.hibernate.ExDao;

import com.lowagie.text.pdf.codec.Base64;

@Resource
public class ExArquivoController extends ExController {
	
	public ExArquivoController(HttpServletRequest request, Result result, SigaObjects so) {
		super(request, result, ExDao.getInstance(), so);
	}
	
	private InputStream inputStream;
	
	private String contentDisposition;
	
	private Integer contentLength;
	
	public void setContentLength(Integer contentLength) {
		this.contentLength = contentLength;
	}
	
	public Integer getContentLength() {
		return contentLength;
	}
	
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}
	
	public String getContentDisposition() {
		return contentDisposition;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	@Get("/app/arquivo/exibir")
	public Download aExibir(String sigla, boolean popup, String arquivo, byte[] certificado, String hash,
			String HASH_ALGORITHM, String certificadoB64, boolean completo, boolean semmarcas) throws Exception {
		try {
			String servernameport = getRequest().getServerName() + ":" + getRequest().getServerPort();
			String contextpath = getRequest().getContextPath();
			@SuppressWarnings("unused")
			ExDao dao = ExDao.getInstance();
			boolean pacoteAssinavel = (certificadoB64 != null);
			boolean fB64 = getRequest().getHeader("Accept") != null
					&& getRequest().getHeader("Accept").startsWith("text/vnd.siga.b64encoded");
			boolean isPdf = arquivo.endsWith(".pdf");
			boolean isHtml = arquivo.endsWith(".html");
			boolean estampar = !semmarcas;
			boolean somenteHash = (hash != null) || (HASH_ALGORITHM != null);
			if (somenteHash) {
				if (hash == null)
					hash = HASH_ALGORITHM;
				if (hash != null) {
					if (!(hash.equals("SHA1") || hash.equals("SHA-256") || hash.equals("SHA-512") || hash.equals("MD5")))
						throw new AplicacaoException(
								"Algoritmo de hash inválido. Os permitidos são: SHA1, SHA-256, SHA-512 e MD5.");
				}
				completo = false;
				estampar = false;
			}
			if (pacoteAssinavel) {
				certificado = Base64.decode(certificadoB64);
				completo = false;
				estampar = false;
			}
			ExMobil mob = Documento.getMobil(arquivo);
			if (mob == null) {
				throw new AplicacaoException("A sigla informada não corresponde a um documento da base de dados.");
			}
			if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {
				throw new AplicacaoException("Documento " + mob.getSigla() + " inacessível ao usuário "
						+ getTitular().getSigla() + "/" + getLotaTitular().getSiglaCompleta() + ".");
			}
			ExMovimentacao mov = Documento.getMov(mob, arquivo);
			boolean imutavel = (mov != null) && !completo && !estampar && !somenteHash && !pacoteAssinavel;
			String cacheControl = "private";
			final Integer grauNivelAcesso = mob.doc().getExNivelAcessoDoDocumento().getGrauNivelAcesso();
			if (ExNivelAcesso.NIVEL_ACESSO_PUBLICO == grauNivelAcesso
					|| ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS == grauNivelAcesso)
				cacheControl = "public";
			byte ab[] = null;
			if (isPdf) {
				if (mov != null && !completo && !estampar && hash == null)
					ab = mov.getConteudoBlobpdf();
				else
					ab = Documento.getDocumento(mob, mov, completo, estampar, hash, null);
				if (ab == null)
					throw new Exception("PDF inválido!");
				String filename = null;
				if (mov != null) {
					filename = mov.getReferencia();
				} else {
					filename = mob.getCodigoCompacto();
				}
				if (pacoteAssinavel) {
					setContentDisposition("attachment; filename=" + filename + ".sa");
					CdService client = Service.getCdService();
					final Date dt = dao().consultarDataEHoraDoServidor();
					// getResponse().setHeader("Atributo-Assinavel-Data-Hora",
					// Long.toString(dt.getTime()));
					byte[] sa = client.produzPacoteAssinavel(certificado, certificado, ab, true, dt);
					return new InputStreamDownload(makeByteArrayInputStream(sa, fB64), "application/octet-stream",
							arquivo);
				}
				if (hash != null) {
					setContentDisposition("attachment; filename=" + filename + "." + hash.toLowerCase());
					this.setContentLength(ab.length);
					return new InputStreamDownload(makeByteArrayInputStream(ab, fB64), "application/octet-stream",
							arquivo);
				}
				setContentDisposition("filename=" + filename + ".pdf");
			}
			if (isHtml) {
				ab = Documento.getDocumentoHTML(mob, mov, completo, contextpath, servernameport);
				if (ab == null)
					throw new Exception("HTML inválido!");
			}
			if (imutavel) {
				// getResponse().setHeader("Cache-Control", cacheControl);
				// getResponse().setDateHeader("Expires", new Date().getTime() +
				// (365 * 24 * 3600 * 1000L));
			} else {
				MessageDigest md = MessageDigest.getInstance("MD5");
				int m = match(ab);
				if (m != -1)
					md.update(ab, 0, m);
				else
					md.update(ab);
				String etag = Base64.encodeBytes(md.digest());
				String ifNoneMatch = getRequest().getHeader("If-None-Match");
				// getResponse().setHeader("Cache-Control",
				// "must-revalidate, " + cacheControl);
				// getResponse().setDateHeader("Expires", 0);
				// getResponse().setHeader("ETag", etag);
				
				if ((etag).equals(ifNoneMatch) && ifNoneMatch != null) {
					// getResponse()
					// .sendError(HttpServletResponse.SC_NOT_MODIFIED);
					return new InputStreamDownload(makeByteArrayInputStream((new byte[0]), false), "text/plain",
							"arquivo inválido");
				}
			}
			return new InputStreamDownload(makeByteArrayInputStream(ab, fB64), checkDownloadType(ab, isPdf, fB64),
					arquivo);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals("ClientAbortException"))
				return new InputStreamDownload(makeByteArrayInputStream((new byte[0]), false), "text/plain",
						"arquivo inválido");
			throw new ServletException("erro na geração do documento.", e);
		}
	}
	
	private ByteArrayInputStream makeByteArrayInputStream(byte[] content, boolean fB64) {
		if (fB64)
			content = Base64.encodeBytes(content).getBytes();
		return (new ByteArrayInputStream(content));
	}
	
	private String checkDownloadType(byte[] content, boolean isPdf, boolean isFB64) {
		String contentType;
		if (isFB64) {
			contentType = "text/plain";
		} else {
			if (isPdf)
				contentType = "application/pdf";
			else
				contentType = "text/html";
		}
		return contentType;
	}
	
	// Esta rotina foi criada para verificar se utilizar o StreamResult do
	// WebWork estava causando uma instabilidade no sistema. Ou seja, se havia
	// algum memory leak na rotina de enviar uma stream como resultado. Assim
	// que fique comprovado que não há interferência, essa rotina deve ser
	// desativada.
	private String writeByteArray(byte[] ab, String contentType, boolean fB64) throws IOException {
		if (ab == null)
			throw new RuntimeException("Conteúdo inválido!");
		
		if (fB64) {
			ab = Base64.encodeBytes(ab).getBytes();
			contentType = "text/plain";
		}
		
		this.setContentLength(ab.length);
		
		// getResponse().setStatus(200);
		// getResponse().setContentLength(getContentLength());
		// getResponse().setContentType(contentType);
		// if (!getPar().get("arquivo")[0].endsWith(".html"))
		// getResponse().setHeader("Content-Disposition",
		// getContentDisposition());
		// getResponse().getOutputStream().write(ab);
		// getResponse().getOutputStream().flush();
		// getResponse().getOutputStream().close();
		return "donothing";
	}
	
	public String aDownload() throws Exception {
		try {
			
			String servernameport = getRequest().getServerName() + ":" + getRequest().getServerPort();
			String contextpath = getRequest().getContextPath();
			
			// log.info("Iniciando servlet de documentos...");
			
			@SuppressWarnings("unused")
			ExDao dao = ExDao.getInstance();
			
			String arquivo = getPar().get("arquivo")[0];
			
			boolean isZip = arquivo.endsWith(".zip");
			boolean isRtf = arquivo.endsWith(".rtf");
			boolean somenteHash = getPar().containsKey("hash") || getPar().containsKey("HASH_ALGORITHM");
			String hash = null;
			if (somenteHash) {
				hash = getPar().get("hash")[0];
				if (hash == null) {
					hash = getPar().get("HASH_ALGORITHM")[0];
				}
				if (hash != null) {
					if (!(hash.equals("SHA1") || hash.equals("SHA-256") || hash.equals("SHA-512") || hash.equals("MD5")))
						throw new AplicacaoException(
								"Algoritmo de hash inválido. Os permitidos são: SHA1, SHA-256, SHA-512 e MD5.");
				}
			}
			
			ExMobil mob = Documento.getMobil(arquivo);
			if (mob == null) {
				throw new AplicacaoException("A sigla informada não corresponde a um documento da base de dados.");
			}
			
			if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {
				throw new AplicacaoException("Documento " + mob.getSigla() + " inacessível ao usuário "
						+ getTitular().getSigla() + "/" + getLotaTitular().getSiglaCompleta() + ".");
			}
			
			ExMovimentacao mov = Documento.getMov(mob, arquivo);
			
			String cacheControl = "private";
			final Integer grauNivelAcesso = mob.doc().getExNivelAcessoDoDocumento().getGrauNivelAcesso();
			if (ExNivelAcesso.NIVEL_ACESSO_PUBLICO == grauNivelAcesso
					|| ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS == grauNivelAcesso)
				cacheControl = "public";
			
			byte ab[] = null;
			if (isZip) {
				ab = mov.getConteudoBlobMov2();
				
				String filename = mov.getNmArqMov();
				
				if (hash != null) {
					this.setInputStream(new ByteArrayInputStream(ab));
					this.setContentLength(ab.length);
					
					setContentDisposition("attachment; filename=" + filename + "." + hash.toLowerCase());
					return "hash";
				}
				
				setContentDisposition("filename=" + filename);
			}
			
			if (isRtf) {
				GeradorRTF gerador = new GeradorRTF();
				ab = gerador.geraRTFFOP(mob.getDoc());
				
				String filename = mob.doc().getCodigo() + ".rtf";
				
				if (hash != null) {
					this.setInputStream(new ByteArrayInputStream(ab));
					this.setContentLength(ab.length);
					
					setContentDisposition("attachment; filename=" + filename + "." + hash.toLowerCase());
					return "hash";
				}
				
				setContentDisposition("filename=" + filename);
			}
			
			// Calcula o hash do documento, mas não leva em consideração
			// para fins de hash os últimos bytes do arquivos, pois lá
			// fica armazanada a ID e as datas de criação e modificação
			// e estas são sempre diferente de um pdf para o outro.
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			int m = match(ab);
			if (m != -1)
				md.update(ab, 0, m);
			else
				md.update(ab);
			String etag = Base64.encodeBytes(md.digest());
			
			String ifNoneMatch = getRequest().getHeader("If-None-Match");
			// getResponse().setHeader("Cache-Control", "must-revalidate, " +
			// cacheControl);
			// getResponse().setDateHeader("Expires", 0);
			// getResponse().setHeader("ETag", etag);
			// getResponse().setHeader("Pragma", "");
			if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
				// getResponse().sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return null;
			}
			
			this.setInputStream(new ByteArrayInputStream(ab));
			this.setContentLength(ab.length);
			
			if (isZip)
				return "zip";
			else
				return "rtf";
			
		} catch (Exception e) {
			throw new ServletException("erro na geração do documento.", e);
		}
	}
	
	static private byte[] idPattern = "/ModDate(D:20".getBytes();
	static private int[] failure = computeFailure();
	
	/**
	 * Finds the first occurrence of the pattern in the text.
	 */
	static public int match(byte[] text) {
		
		int j = 0;
		if (text.length == 0)
			return -1;
		
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
	
	/**
	 * Computes the failure function using a boot-strapping process, where the
	 * pattern is matched against itself.
	 */
	static private int[] computeFailure() {
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
	
}