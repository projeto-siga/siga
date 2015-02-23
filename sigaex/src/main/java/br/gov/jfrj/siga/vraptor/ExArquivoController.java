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
import java.security.MessageDigest;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
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
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;

import com.lowagie.text.pdf.codec.Base64;

@Resource
public class ExArquivoController extends ExController {

	private static final String TEXT_HTML = "text/html";
	private static final String APPLICATION_PDF = "application/pdf";
	private static final String TEXT_PLAIN = "text/plain";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static byte[] idPattern = "/ModDate(D:20".getBytes();
	private static int[] failure = computeFailure();

	public ExArquivoController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("/app/arquivo/exibir")
	public Download aExibir(final String sigla, final boolean popup, final String arquivo, byte[] certificado, String hash, final String HASH_ALGORITHM,
			final String certificadoB64, boolean completo, final boolean semmarcas) {
		try {
			final String servernameport = getRequest().getServerName() + ":" + getRequest().getServerPort();
			final String contextpath = getRequest().getContextPath();
			final boolean pacoteAssinavel = (certificadoB64 != null);
			final boolean fB64 = getRequest().getHeader("Accept") != null && getRequest().getHeader("Accept").startsWith("text/vnd.siga.b64encoded");
			final boolean isPdf = arquivo.endsWith(".pdf");
			final boolean isHtml = arquivo.endsWith(".html");
			boolean estampar = !semmarcas;
			final boolean somenteHash = (hash != null) || (HASH_ALGORITHM != null);
			if (somenteHash) {
				if (hash == null)
					hash = HASH_ALGORITHM;
				if (hash != null) {
					if (!(hash.equals("SHA1") || hash.equals("SHA-256") || hash.equals("SHA-512") || hash.equals("MD5"))) {
						throw new AplicacaoException("Algoritmo de hash inválido. Os permitidos são: SHA1, SHA-256, SHA-512 e MD5.");
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
			if (mob == null) {
				throw new AplicacaoException("A sigla informada não corresponde a um documento da base de dados.");
			}
			if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {
				throw new AplicacaoException("Documento " + mob.getSigla() + " inacessível ao usuário " + getTitular().getSigla() + "/"
						+ getLotaTitular().getSiglaCompleta() + ".");
			}
			final ExMovimentacao mov = Documento.getMov(mob, arquivo);
			final boolean imutavel = (mov != null) && !completo && !estampar && !somenteHash && !pacoteAssinavel;
			String cacheControl = "private";
			final Integer grauNivelAcesso = mob.doc().getExNivelAcessoDoDocumento().getGrauNivelAcesso();
			if (ExNivelAcesso.NIVEL_ACESSO_PUBLICO == grauNivelAcesso || ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS == grauNivelAcesso) {
				cacheControl = "public";
			}
			byte ab[] = null;
			if (isPdf) {
				if (mov != null && !completo && !estampar && hash == null) {
					ab = mov.getConteudoBlobpdf();
				} else {
					ab = Documento.getDocumento(mob, mov, completo, estampar, hash, null);
				}
				if (ab == null) {
					throw new Exception("PDF inválido!");
				}
				if (pacoteAssinavel) {
					CdService client = Service.getCdService();
					final Date dt = dao().consultarDataEHoraDoServidor();
					getResponse().setHeader("Atributo-Assinavel-Data-Hora", Long.toString(dt.getTime()));
					byte[] sa = client.produzPacoteAssinavel(certificado, certificado, ab, true, dt);
					return new InputStreamDownload(makeByteArrayInputStream(sa, fB64), APPLICATION_OCTET_STREAM, arquivo);
				}
				if (hash != null) {
					return new InputStreamDownload(makeByteArrayInputStream(ab, fB64), APPLICATION_OCTET_STREAM, arquivo);
				}
			}
			if (isHtml) {
				ab = Documento.getDocumentoHTML(mob, mov, completo, contextpath, servernameport);
				if (ab == null) {
					throw new Exception("HTML inválido!");
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
					return new InputStreamDownload(makeByteArrayInputStream((new byte[0]), false), TEXT_PLAIN, "arquivo inválido");
				}
			}
			getResponse().setHeader("Pragma", "");
			return new InputStreamDownload(makeByteArrayInputStream(ab, fB64), checkDownloadType(ab, isPdf, fB64), arquivo);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals("ClientAbortException")) {
				return new InputStreamDownload(makeByteArrayInputStream((new byte[0]), false), TEXT_PLAIN, "arquivo inválido");
			}
			throw new RuntimeException("erro na geração do documento.", e);
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

}