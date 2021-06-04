package br.gov.jfrj.siga.ex.api.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.pdf.codec.Base64;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.bluc.service.BlucService;
import br.gov.jfrj.siga.bluc.service.HashRequest;
import br.gov.jfrj.siga.bluc.service.HashResponse;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;

public class SigaDocPdfUtils {
	private static final String TEXT_HTML = "text/html";
	private static final String APPLICATION_PDF = "application/pdf";
	private static final String TEXT_PLAIN = "text/plain";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static byte[] idPattern = "/ModDate(D:20".getBytes();
	private static int[] failure = computeFailure();

	public static InputStreamDownload exibir(final String sigla,
			final boolean popup, final String arquivo, byte[] certificado,
			String hash, final String HASH_ALGORITHM,
			final String certificadoB64, boolean completo,
			final boolean semmarcas, DpPessoa titular, DpLotacao lotaTitular,
			Date dataEHoraDoServidor, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			final String servernameport = request.getServerName() + ":"
					+ request.getServerPort();
			final String contextpath = request.getContextPath();
			final boolean pacoteAssinavel = (certificadoB64 != null);
			final boolean fB64 = request.getHeader("Accept") != null
					&& request.getHeader("Accept").startsWith(
							"text/vnd.siga.b64encoded");
			final boolean isPdf = arquivo.endsWith(".pdf");
			final boolean isHtml = arquivo.endsWith(".html");
			boolean estampar = !semmarcas;
			final boolean somenteHash = (hash != null)
					|| (HASH_ALGORITHM != null);
			if (somenteHash) {
				if (hash == null)
					hash = HASH_ALGORITHM;
				if (hash != null) {
					if (!(hash.equals("SHA1") || hash.equals("SHA-256")
							|| hash.equals("SHA-512") || hash.equals("MD5"))) {
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
			if (mob == null) {
				throw new AplicacaoException(
						"A sigla informada não corresponde a um documento da base de dados.");
			}
			if (!Ex.getInstance().getComp()
					.podeAcessarDocumento(titular, lotaTitular, mob)) {
				throw new AplicacaoException("Documento " + mob.getSigla()
						+ " inacessível ao usuário " + titular.getSigla() + "/"
						+ lotaTitular.getSiglaCompleta() + ".");
			}
			final ExMovimentacao mov = Documento.getMov(mob, arquivo);
			final boolean isArquivoAuxiliar = mov != null
					&& mov.getExTipoMovimentacao()
							.getId()
							.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR);
			final boolean imutavel = (mov != null) && !completo && !estampar
					&& !somenteHash && !pacoteAssinavel;
			String cacheControl = "private";
			final Integer grauNivelAcesso = mob.doc().getExNivelAcesso()
					.getGrauNivelAcesso();
			if (ExNivelAcesso.NIVEL_ACESSO_PUBLICO == grauNivelAcesso
					|| ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS == grauNivelAcesso) {
				cacheControl = "public";
			}
			byte ab[] = null;
			if (isArquivoAuxiliar) {
				ab = mov.getConteudoBlobMov2();
				return new InputStreamDownload(makeByteArrayInputStream(ab,
						fB64), APPLICATION_OCTET_STREAM, mov.getNmArqMov(),
						(!fB64) ? ab.length : null);
			}
			if (isPdf) {
				if (mov != null && !completo && !estampar && hash == null) {
					ab = mov.getConteudoBlobpdf();
				} else {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					Documento.getDocumento(baos, null, mob, null, completo, semmarcas, false, null, null);
					ab = baos.toByteArray();
				}
				if (ab == null) {
					throw new Exception("PDF inválido!");
				}
				if (pacoteAssinavel) {
					response.setHeader("Atributo-Assinavel-Data-Hora",
							Long.toString(dataEHoraDoServidor.getTime()));

					// Chamar o BluC para criar o pacote assinavel
					//
					BlucService bluc = Service.getBlucService();
					HashRequest hashreq = new HashRequest();
					hashreq.setCertificate(certificadoB64);
					hashreq.setCrl("true");
					hashreq.setPolicy("AD-RB");
					hashreq.setSha1(BlucService.bytearray2b64(BlucService.calcSha1(ab)));
					hashreq.setSha256(BlucService.bytearray2b64(BlucService.calcSha256(ab)));
					hashreq.setTime(dataEHoraDoServidor);
					HashResponse hashresp = bluc.hash(hashreq);
					if (hashresp.getErrormsg() != null)
						throw new Exception(
								"BluC não conseguiu produzir o pacote assinável. "
										+ hashresp.getErrormsg());
					byte[] sa = Base64.decode(hashresp.getHash());

					return new InputStreamDownload(makeByteArrayInputStream(sa,
							fB64), APPLICATION_OCTET_STREAM, arquivo,
							(!fB64) ? sa.length : null);
				}
				if (hash != null) {
					return new InputStreamDownload(makeByteArrayInputStream(ab,
							fB64), APPLICATION_OCTET_STREAM, arquivo,
							(!fB64) ? ab.length : null);
				}
			}
			if (isHtml) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Documento.getDocumentoHTML(baos, null, mob, mov, completo, false, contextpath, servernameport);
				ab = baos.toByteArray();

				if (ab == null) {
					throw new Exception("HTML inválido!");
				}
			}
			if (imutavel) {
				response.setHeader("Cache-Control", cacheControl);
				response.setDateHeader("Expires", new Date().getTime()
						+ (365 * 24 * 3600 * 1000L));
			} else {
				final MessageDigest md = MessageDigest.getInstance("MD5");
				final int m = match(ab);
				if (m != -1) {
					md.update(ab, 0, m);
				} else {
					md.update(ab);
				}
				final String etag = Base64.encodeBytes(md.digest());
				final String ifNoneMatch = request.getHeader("If-None-Match");
				response.setHeader("Cache-Control", "must-revalidate, "
						+ cacheControl);
				response.setDateHeader("Expires",
						(new Date()).getTime() + 30000);
				response.setHeader("ETag", etag);

				if ((etag).equals(ifNoneMatch) && ifNoneMatch != null) {
					response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
					return new InputStreamDownload(makeByteArrayInputStream(
							(new byte[0]), false), TEXT_PLAIN,
							"arquivo inválido", 0);
				}
			}
			response.setHeader("Pragma", "");
			return new InputStreamDownload(makeByteArrayInputStream(ab, fB64),
					checkDownloadType(ab, isPdf, fB64), arquivo,
					(!fB64) ? ab.length : null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals("ClientAbortException")) {
				return new InputStreamDownload(makeByteArrayInputStream(
						(new byte[0]), false), TEXT_PLAIN, "arquivo inválido",
						0);
			}
			throw new RuntimeException("erro na geração do documento.", e);
		}
	}

	private static ByteArrayInputStream makeByteArrayInputStream(
			final byte[] content, final boolean fB64) {
		final byte[] conteudo = (fB64 ? Base64.encodeBytes(content).getBytes()
				: content);
		return (new ByteArrayInputStream(conteudo));
	}

	public static class InputStreamDownload {
		InputStream is;
		String contentyType;
		String fileName;
		Integer contentLength;

		public InputStreamDownload(InputStream is, String contentType,
				String fileName, Integer contentLength) {
			super();
			this.is = is;
			this.contentyType = contentType;
			this.fileName = fileName;
			this.contentLength = contentLength;
		}
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

	private static String checkDownloadType(final byte[] content,
			final boolean isPdf, final boolean isFB64) {
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
}
