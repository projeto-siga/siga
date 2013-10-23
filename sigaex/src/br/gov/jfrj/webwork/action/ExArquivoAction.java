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
package br.gov.jfrj.webwork.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;

import com.lowagie.text.pdf.codec.Base64;

public class ExArquivoAction extends ExActionSupport {
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

	public String aExibir() throws Exception {
		try {

			String servernameport = getRequest().getServerName() + ":"
					+ getRequest().getServerPort();
			String contextpath = getRequest().getContextPath();

			// log.info("Iniciando servlet de documentos...");

			@SuppressWarnings("unused")
			ExDao dao = ExDao.getInstance();

			String arquivo = getPar().get("arquivo")[0];

			boolean isPdf = arquivo.endsWith(".pdf");
			boolean isHtml = arquivo.endsWith(".html");
			boolean estampar = !getPar().containsKey("semmarcas");
			boolean completo = getPar().containsKey("completo");
			boolean somenteHash = getPar().containsKey("hash")
					|| getPar().containsKey("HASH_ALGORITHM");
			String hash = null;
			if (somenteHash) {
				hash = getPar().get("hash")[0];
				if (hash == null) {
					hash = getPar().get("HASH_ALGORITHM")[0];
				}
				if (hash != null) {
					if (!(hash.equals("SHA1") || hash.equals("SHA-256")
							|| hash.equals("SHA-512") || hash.equals("MD5")))
						throw new AplicacaoException(
								"Algoritmo de hash inválido. Os permitidos são: SHA1, SHA-256, SHA-512 e MD5.");
				}
				completo = false;
				estampar = false;
			}

			ExMobil mob = Documento.getMobil(arquivo);
			if (mob == null) {
				throw new AplicacaoException(
						"A sigla informada não corresponde a um documento da base de dados.");
			}

			if (!Ex.getInstance().getComp()
					.podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {
				throw new AplicacaoException("Documento " + mob.getSigla()
						+ " inacessível ao usuário " + getTitular().getSigla()
						+ "/" + getLotaTitular().getSiglaCompleta() + ".");
			}

			ExMovimentacao mov = Documento.getMov(mob, arquivo);

			String cacheControl = "private";
			final Integer grauNivelAcesso = mob.doc()
					.getExNivelAcessoDoDocumento().getGrauNivelAcesso();
			if (ExNivelAcesso.NIVEL_ACESSO_PUBLICO == grauNivelAcesso
					|| ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS == grauNivelAcesso)
				cacheControl = "public";

			byte ab[] = null;
			if (isPdf) {
				ab = Documento.getDocumento(mob, mov, completo, estampar, hash);

				String filename = null;
				if (mov != null) {
					filename = mov.getReferencia();
				} else {
					filename = mob.getCodigoCompacto();
				}

				if (hash != null) {
					this.setInputStream(new ByteArrayInputStream(ab));
					this.setContentLength(ab.length);

					setContentDisposition("attachment; filename=" + filename
							+ "." + hash.toLowerCase());
					return "hash";
				}

				setContentDisposition("filename=" + filename + ".pdf");
			}
			if (isHtml) {
				ab = Documento.getDocumentoHTML(mob, mov, completo,
						contextpath, servernameport);
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
			getResponse().setHeader("Cache-Control",
					"must-revalidate, " + cacheControl);
			getResponse().setDateHeader("Expires", 0);
			getResponse().setHeader("ETag", etag);
			getResponse().setHeader("Pragma", null);
			if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
				getResponse().sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return null;
			}

			this.setInputStream(new ByteArrayInputStream(ab));
			this.setContentLength(ab.length);
			return isPdf ? "pdf" : "html";
		} catch (Exception e) {
			throw new ServletException("erro na geração do documento.", e);
		}
	}
	
	public String aDownload() throws Exception {
		try {

			String servernameport = getRequest().getServerName() + ":"
					+ getRequest().getServerPort();
			String contextpath = getRequest().getContextPath();

			// log.info("Iniciando servlet de documentos...");

			@SuppressWarnings("unused")
			ExDao dao = ExDao.getInstance();

			String arquivo = getPar().get("arquivo")[0];

			boolean isZip = arquivo.endsWith(".zip");
			boolean somenteHash = getPar().containsKey("hash")
					|| getPar().containsKey("HASH_ALGORITHM");
			String hash = null;
			if (somenteHash) {
				hash = getPar().get("hash")[0];
				if (hash == null) {
					hash = getPar().get("HASH_ALGORITHM")[0];
				}
				if (hash != null) {
					if (!(hash.equals("SHA1") || hash.equals("SHA-256")
							|| hash.equals("SHA-512") || hash.equals("MD5")))
						throw new AplicacaoException(
								"Algoritmo de hash inválido. Os permitidos são: SHA1, SHA-256, SHA-512 e MD5.");
				}
			}
			
			ExMobil mob = Documento.getMobil(arquivo);
			if (mob == null) {
				throw new AplicacaoException(
						"A sigla informada não corresponde a um documento da base de dados.");
			}

			if (!Ex.getInstance().getComp()
					.podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {
				throw new AplicacaoException("Documento " + mob.getSigla()
						+ " inacessível ao usuário " + getTitular().getSigla()
						+ "/" + getLotaTitular().getSiglaCompleta() + ".");
			}

			ExMovimentacao mov = Documento.getMov(mob, arquivo);

			String cacheControl = "private";
			final Integer grauNivelAcesso = mob.doc()
					.getExNivelAcessoDoDocumento().getGrauNivelAcesso();
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

					setContentDisposition("attachment; filename=" + filename
							+ "." + hash.toLowerCase());
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
			getResponse().setHeader("Cache-Control",
					"must-revalidate, " + cacheControl);
			getResponse().setDateHeader("Expires", 0);
			getResponse().setHeader("ETag", etag);
			getResponse().setHeader("Pragma", null);
			if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
				getResponse().sendError(HttpServletResponse.SC_NOT_MODIFIED);
				return null;
			}

			this.setInputStream(new ByteArrayInputStream(ab));
			this.setContentLength(ab.length);
			return "zip";
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