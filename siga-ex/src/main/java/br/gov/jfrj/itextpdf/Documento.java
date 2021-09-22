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
package br.gov.jfrj.itextpdf;

import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO;
import static br.gov.jfrj.siga.ex.util.ProcessadorHtml.novoHtmlPersonalizado;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.ex.ExArquivoNumerado;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.ext.AbstractConversorHTMLFactory;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

/**
 * Hello World example as a Servlet.
 * 
 * @author blowagie
 */
public class Documento {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6008800739543368811L;

	private static final Pattern pattern = Pattern
		.compile("^([0-9A-Z\\-\\/]+(?:\\.[0-9]+)?(?:V[0-9]+)?)(:[0-9]+)?(?:\\.pdf|\\.html|\\.zip|\\.rtf)?$");

	private static Log log = LogFactory.getLog(Documento.class);

	public static ExMobil getMobil(String requestURI) throws SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, Exception {
		ExMobil mob = null;
		String sigla = "";
		
		final Matcher m = pattern.matcher(requestURI);
		if (m.find()) {
			sigla = m.group(1);
			final ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
			flt.setSigla(sigla);
			mob = (ExMobil) ExDao.getInstance().consultarPorSigla(flt);
		}
		// expDAO.consultarConteudoBlob(docvia.getExDocumento());
		return mob;
	}

	public static ExMovimentacao getMov(ExMobil mob, String requestURI)
			throws AplicacaoException, SQLException {
		String sMovId = null;
		ExMovimentacao mov = null;

		final Matcher m = pattern.matcher(requestURI);
		if (m.find()) {
			sMovId = m.group(2);
			if (sMovId == null || sMovId.length() <= 1)
				return null;
			final long l = Long.parseLong(sMovId.substring(1));
			for (ExMovimentacao movAux : mob.getExMovimentacaoSet()) {
				if (movAux.getIdMov() == l)
					mov = movAux;
			}
		}
		if (mov == null)
			return null;
		mov = ExDao.getInstance().consultar(mov.getIdMov(),
				ExMovimentacao.class, false);
		return mov;
	}

	private String getDocHTML(ExMobil mob, HttpServletRequest request)
			throws Exception {
		ExDocumento doc;
		doc = mob.getExDocumento();
		String sHtml;
		if (doc.getExTipoDocumento().getIdTpDoc() == 1) {
			sHtml = doc.getConteudoBlobHtmlString();
			sHtml = (new ProcessadorHtml()).canonicalizarHtml(sHtml, true,
					false, true, false, true);
		} else {
			sHtml = Ex.getInstance().getBL()
					.processarModelo(doc, "processar_modelo", null, null);
		}

		return sHtml;
	}

	private byte[] getDocPDF(ExMobil mob, ExMovimentacao mov,
			HttpServletRequest request) throws Exception {
		byte pdf[] = null;
		if (mov == null) {
			ExDocumento doc;
			doc = mob.getExDocumento();
			pdf = doc.getConteudoBlobPdf();
		} else {
			pdf = mov.getConteudoBlobpdf();
		}
		if (pdf != null)
			return pdf;
		else
			throw new AplicacaoException(
					"Não existe arquivo pdf para esse documento.");

		// String sHtml = getDocHTML(docvia, requestId);
		// return generatePdf(sHtml);
	}

	public static int getNumberOfPages(byte[] abPdf) throws IOException {
		final PdfReader reader = new PdfReader(abPdf);
		final int n = reader.getNumberOfPages();
		reader.close();
		return n;
	}

	public static ArrayList<String> getAssinantesStringLista(
			Set<ExMovimentacao> movsAssinatura, Date dtDoc) {
		ArrayList<String> assinantes = new ArrayList<String>();
		for (ExMovimentacao movAssinatura : movsAssinatura) {
			StringBuilder s = new StringBuilder();
			Date dataDeInicioDeObrigacaoExibirRodapeDeAssinatura=null;
			if (movAssinatura.getExTipoMovimentacao().getId().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOLICITACAO_DE_ASSINATURA)) {
				s.append(Texto.maiusculasEMinusculas(movAssinatura.getCadastrante().getNomePessoa()));
			} else {
				dataDeInicioDeObrigacaoExibirRodapeDeAssinatura = Prop.getData("rodape.data.assinatura.ativa");
				s.append(movAssinatura.getDescrMov().trim().toUpperCase().split(":")[0]);
				

				/*** Exibe para Documentos Capturados a Funcao / Unidade ***/
				if (movAssinatura.getExDocumento().isInternoCapturado()
						&& (movAssinatura.getIdTpMov().equals(TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA)
								|| movAssinatura.getIdTpMov().equals(TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO))) {
					/* Interno Exibe Personalização se realizada */
					s.append(Ex.getInstance().getBL().extraiPersonalizacaoAssinatura(movAssinatura,true));
				} else if (movAssinatura.getExDocumento().isExternoCapturado()
						|| movAssinatura.getExDocumento().isInternoCapturado()) {
					s.append(" - ");
					s.append(movAssinatura.getTitular() != null ? movAssinatura.getTitular().getFuncaoString() : movAssinatura.getCadastrante().getFuncaoString());
					s.append(" / ");
					s.append(movAssinatura.getTitular() != null ? movAssinatura.getTitular().getLotacao().getSigla() : movAssinatura.getCadastrante().getLotacao().getSigla());
				}
				/**** ****/
				
				if(Prop.isGovSP() || (dataDeInicioDeObrigacaoExibirRodapeDeAssinatura != null && !dataDeInicioDeObrigacaoExibirRodapeDeAssinatura.after(dtDoc))) {
					s.append(" - ");
					s.append(Data.formatDDMMYYYY_AS_HHMMSS(movAssinatura.getData()));
				}				 
			}
			if (!assinantes.contains(s.toString())) {
				assinantes.add(s.toString());
			}
		}
		return assinantes;
	}
	
	public static ArrayList<String> getAssinantesStringListaComMatricula(
			Set<ExMovimentacao> movsAssinatura, Date dtDoc)  {
		ArrayList<String> assinantes = new ArrayList<String>();
		for (ExMovimentacao movAssinatura : movsAssinatura) {
			String s;
			Date dataDeInicioDeObrigacaoExibirRodapeDeAssinatura=null;
			if (movAssinatura.getExTipoMovimentacao().getId().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOLICITACAO_DE_ASSINATURA)) {
				s = Texto.maiusculasEMinusculas(movAssinatura.getCadastrante().getNomePessoa());
			} else {
				dataDeInicioDeObrigacaoExibirRodapeDeAssinatura = Prop.getData("rodape.data.assinatura.ativa");
				s = movAssinatura.getDescrMov().trim().toUpperCase();
				s = s.replace(":", " - ");
				s = s.replace("EM SUBSTITUIÇÃO A", "em substituição a");
				s = s.intern();				
				if (Prop.isGovSP()
					|| (dataDeInicioDeObrigacaoExibirRodapeDeAssinatura != null && !dataDeInicioDeObrigacaoExibirRodapeDeAssinatura.after(dtDoc)
							)	) {
					s +=" - " + Data.formatDDMMYY_AS_HHMMSS(movAssinatura.getData());
				}				
			}
			if (!assinantes.contains(s)) {
				assinantes.add(s);
			}
		}
		return assinantes;
	}

	public static String getAssinantesString(Set<ExMovimentacao> movsAssinatura, Date dtDoc) {
		ArrayList<String> als = getAssinantesStringLista(movsAssinatura,dtDoc);
		String retorno = "";
		if (als.size() > 0) {
			for (int i = 0; i < als.size(); i++) {
				String nome = als.get(i);
				if (i > 0) {
					if (i == als.size() - 1) {
						retorno += " e ";
					} else {
						retorno += ", ";
					}
				}
				retorno += nome;
			}
		}
		return retorno;
	}
	
	public static String getAssinantesStringComMatricula(Set<ExMovimentacao> movsAssinatura, Date dtDoc) {
		ArrayList<String> als = getAssinantesStringListaComMatricula(movsAssinatura,dtDoc);
		String retorno = "";
		if (als.size() > 0) {
			for (int i = 0; i < als.size(); i++) {
				String nome = als.get(i);
				if (i > 0) {
					if (i == als.size() - 1) {
						retorno += " e ";
					} else {
						retorno += ", ";
					}
				}
				retorno += nome;
			}
		}
		return retorno;
	}

	public static String getAssinantesPorString(Set<ExMovimentacao> movsAssinaturaPor, Date dtDoc,
			Set<ExMovimentacao> movsAssinatura) {
		ArrayList<String> als = getAssinantesStringListaComMatricula(movsAssinaturaPor,dtDoc);
		String retorno = "";
		if (als.size() > 0) {
			for (int i = 0; i < als.size(); i++) {
				String nome = als.get(i);
				for (ExMovimentacao mov : movsAssinatura) {
					if (mov.getCadastrante().getSigla().equals(nome.split(" - ")[1].split(" ")[0])) {
						if (mov.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA
							|| mov.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA) {
							nome = "Assinado com senha por " + nome;
							break;
						}
						if (mov.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO
							|| mov.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO) {
							nome = "Assinado digitalmente por " + nome;
							break;
						}
					}
				}
				if (i > 0) {
					if (i == als.size() - 1) {
						retorno += " e ";
					} else {
						retorno += ", ";
					}
				}
				retorno += nome;
			}
		}
		return retorno;
	}

	
	// private byte[] getPdfOld(byte[] pdf, ExDocumentoVia docvia,
	// ExMovimentacao mov, Integer paginaInicial, Integer paginaFinal,
	// HttpServletRequest request) throws Exception {
	//
	// ExArquivo arq = mov != null ? mov : docvia.getExDocumento();
	// String sigla = docvia.getSigla();
	// ExDocumento doc = docvia.getExDocumento();
	//
	// return stamp(pdf, sigla, doc.isRascunho(), doc.isCancelado(), arq
	// .getQRCode(), arq.getMensagem(), paginaInicial, paginaFinal,
	// "Justiça Federal", doc.getOrgaoUsuario().getDescricao(),
	// request);
	// }

	// private byte[] getPdf(ExDocumentoVia docViaPrincipal, ExArquivo arq,
	// Integer numVia, Integer paginaInicial, Integer paginaFinal,
	// HttpServletRequest request) throws Exception {
	//
	// String sigla = docViaPrincipal.getSigla();
	// if (arq instanceof ExMovimentacao) {
	// ExMovimentacao mov = (ExMovimentacao) arq;
	// if (mov.getExTipoMovimentacao().getId() ==
	// ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
	// sigla = mov.getExDocumentoVia().getSigla();
	// } else {
	// ExDocumentoVia dv = new ExDocumentoVia();
	// dv.setExDocumento((ExDocumento) arq);
	// dv.setNumVia(numVia);
	// sigla = dv.getSigla();
	// }
	//
	// return stamp(arq.getPdf(), sigla, arq.isRascunho(), arq.isCancelado(),
	// arq.getQRCode(), arq.getMensagem(), paginaInicial, paginaFinal,
	// "Justiça Federal", docViaPrincipal.getExDocumento()
	// .getOrgaoUsuario().getDescricao(), request);
	// }

	private class Bookmarker extends PdfPageEventHelper {
		@Override
		public void onStartPage(PdfWriter writer, Document arg1) {
			PdfContentByte cb = writer.getDirectContent();
			PdfDestination destination = new PdfDestination(PdfDestination.FIT);
			new PdfOutline(cb.getRootOutline(), destination, "teste ");
		}
	}

	public byte[] getDocumento(ExMobil mob, ExMovimentacao mov)
			throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		getDocumento(baos, null, mob, mov, false, true, false, null, null);
		return baos.toByteArray();
	}

	public static boolean getDocumento(OutputStream os, String uuid, ExMobil mob, ExMovimentacao mov,
			boolean completo, boolean estampar, boolean volumes, String hash, byte[] certificado)
			throws Exception {
		PdfReader reader;
		int n;
		int pageOffset = 0;
		ArrayList master = new ArrayList();
		Document document = null;
		PdfCopy writer = null;
		int nivelInicial = 0;

		// if (request.getRequestURI().indexOf("/completo/") == -1) {
		// return getPdf(docvia, mov != null ? mov : docvia.getExDocumento(),
		// mov != null ? mov.getNumVia() : docvia.getNumVia(), null,
		// null, request);
		// }

		Status status = null;
		if (uuid != null)
			status = Status.update(uuid, "Obtendo a lista de documentos", 0, 100, 0L);

		List<ExArquivoNumerado> ans = getArquivosNumerados(mob, mov, uuid, completo, volumes);

		if (!completo && !estampar && ans.size() == 1) {
			if (hash != null) {
				// Calcula o hash do documento
				String alg = hash;
				MessageDigest md = MessageDigest.getInstance(alg);
				md.update(ans.get(0).getArquivo().getPdf());
				os.write(md.digest());
				return true;
			} else {
				os.write(ans.get(0).getArquivo().getPdf());
				return true;
			}
		}

		int f = 0;
		long bytes = 0;
		try {
			for (ExArquivoNumerado an : ans) {
				if (uuid != null)
					status = Status.update(uuid, "Agregando documento " + (f + 1) + "/" + ans.size(),
							f * 2 + 1, ans.size() * 2 + 1, bytes);

				// byte[] ab = getPdf(docvia, an.getArquivo(), an.getNumVia(),
				// an
				// .getPaginaInicial(), an.getPaginaFinal(), request);

				String sigla = mob.getSigla();
				if (an.getArquivo() instanceof ExMovimentacao) {
					ExMovimentacao m = (ExMovimentacao) an.getArquivo();
					if (m.getExTipoMovimentacao().getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
						sigla = m.getExMobil().getSigla();
				} else {
					sigla = an.getMobil().getSigla();
				}

				byte[] ab = !estampar ? an.getArquivo().getPdf() : Stamp.stamp(an
						.getArquivo().getPdf(), sigla, an.getArquivo()
						.isRascunho(), an.isCopia(), an.getArquivo().isCancelado(), an
						.getArquivo().isSemEfeito(), an.getArquivo()
						.isInternoProduzido(), an.getArquivo().getQRCode(), an
						.getArquivo().getMensagem(), an.getPaginaInicial(),
						an.getPaginaFinal(), an.getOmitirNumeracao(),
						Prop.get("carimbo.texto.superior"), 
						mob.getExDocumento().getOrgaoUsuario().getDescricao(), 
						mob.getExDocumento().getMarcaDagua(), 
						an.getMobil().getDoc().getIdsDeAssinantes());	
				

				bytes += ab.length;

				// we create a reader for a certain document

				reader = new PdfReader(ab);
				reader.consolidateNamedDestinations();
				// we retrieve the total number of pages
				n = reader.getNumberOfPages();
				// List bookmarks = SimpleBookmark.getBookmark(reader);
				// master.add(new Bookmark)
				// if (bookmarks != null) {
				// if (pageOffset != 0)
				// SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset,
				// null);
				// master.addAll(bookmarks);
				// }

				if (f == 0) {
					// step 1: creation of a document-object
					document = new Document(reader.getPageSizeWithRotation(1));
					// step 2: we create a writer that listens to the
					// document
					writer = new PdfCopy(document, os);
					writer.setFullCompression();

					// writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);

					// step 3: we open the document
					document.open();

					nivelInicial = an.getNivel();
				}

				// PdfOutline root = writer.getDirectContent().getRootOutline();
				// PdfContentByte cb = writer.getDirectContent();
				// PdfDestination destination = new
				// PdfDestination(PdfDestination.FITH, position);
				// step 4: we add content
				PdfImportedPage page;
				for (int j = 0; j < n;) {
					++j;
					page = writer.getImportedPage(reader, j);
					writer.addPage(page);
					if (j == 1) {
						// PdfContentByte cb = writer.getDirectContent();
						// PdfOutline root = cb.getRootOutline();
						// PdfOutline oline1 = new PdfOutline(root,
						// PdfAction.gotoLocalPage("1", false),"Chapter 1");

						HashMap map = new HashMap();
						map.put("Title", an.getNome());
						map.put("Action", "GoTo");
						map.put("Page", j + pageOffset + "");
						map.put("Kids", new ArrayList());

						ArrayList mapPai = master;
						for (int i = 0; i < an.getNivel() - nivelInicial; i++) {
							mapPai = ((ArrayList) ((HashMap) mapPai.get(mapPai
									.size() - 1)).get("Kids"));
						}
						mapPai.add(map);
					}

				}
				PRAcroForm form = reader.getAcroForm();
				if (form != null)
					writer.copyAcroForm(reader);

				pageOffset += n;
				f++;
			}
			if (!master.isEmpty())
				writer.setOutlines(master);

			// PdfDictionary info = writer.getInfo();
			// info.put(PdfName.MODDATE, null);
			// info.put(PdfName.CREATIONDATE, null);
			// info.put(PdfName.ID, null);

			document.close();

			if (uuid != null)
				status = Status.update(uuid, "PDF completo gerado", ans.size() * 2 + 1, ans.size() * 2 + 1,
					bytes);

		} catch (Exception ex) {
			if (uuid != null) { 
				status.ex = ex;
				Status.update(uuid, status);
			}
			throw new RuntimeException(ex);
		}
		return true;
	}

	private static List<ExArquivoNumerado> getArquivosNumerados(ExMobil mob, ExMovimentacao mov, String uuid, boolean completo, boolean volumes) throws Exception {
		List<ExArquivoNumerado> ans = null;
		if (volumes && completo && mob.getDoc().isProcesso()) {
			ExDocumento doc = mob.getDoc();
			ans = new ArrayList<>();
			Set<ExMobil> vols = doc.getVolumes();
			for (ExMobil m : vols) {
				if (uuid != null)
					Status.update(uuid, "Obtendo a lista de documentos - Volume " + m.getNumSequencia() + "/" +  vols.size(), 0, 100, 0L);
				ans.addAll(m.filtrarArquivosNumerados(null,	completo));
			}
		} else
			ans = mob.filtrarArquivosNumerados(mov,	completo);
		return ans;
	}

	public static byte[] generatePdf(String sHtml) throws Exception {
		return generatePdf(sHtml, AbstractConversorHTMLFactory.getInstance()
				.getConversorPadrao());
	}

	public static byte[] generatePdf(String sHtml, ConversorHtml parser)
			throws Exception {
		sHtml = (new ProcessadorHtml()).canonicalizarHtml(sHtml, true, false,
				true, false, true);
		
		sHtml = incluirLinkNasAssinaturas(sHtml);

		sHtml = sHtml.replace("contextpath", realPath());
		
		return parser.converter(sHtml, ConversorHtml.PDF);

	}
	
	private static String incluirLinkNasAssinaturas(String sHtml) {
		sHtml = sHtml.replaceAll("<!-- INICIO SUBSCRITOR (\\d+) -->(<!-- SIGLA (\\S+) -->)?", "<a class=\"doc-sign\" href=\"contextpath/sigaex/app/validar-assinatura?pessoa=$1&sigla=$3\">");
		sHtml = sHtml.replaceAll("<!-- FIM SUBSCRITOR (\\d+) -->", "</a>");
		return sHtml;
	}

	public static void getDocumentoHTML(OutputStream os, String uuid, ExMobil mob, ExMovimentacao mov,
			boolean completo, boolean volumes, String contextpath, String servernameport)
			throws Exception {
		Status status = null;
		if (uuid != null)
			status = Status.update(uuid, "Obtendo a lista de documentos", 0, 100, 0L);
		List<ExArquivoNumerado> ans = getArquivosNumerados(mob, mov, uuid, completo, volumes);

		boolean fFirst = true;
		// TAH: infelizmente o IE não funciona bem com background-color:
		// transparent.
		// sb.append("<html class=\"fisico\"><body style=\"margin:2px; padding:0pt; background-color: #E2EAEE;overflow:visible;\">");
		try (PrintWriter sb = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8)))) {
			sb.append("<html><head><base target=\"_parent\"/><link rel=\"stylesheet\" href=\"/siga/css/style_siga.css\" type=\"text/css\" media=\"screen, projection\"></head><body style=\"margin:2px; padding:0pt; background-color: "
					+ (mob.getDoc().isEletronico() ? "#E2EAEE" : "#f1e9c6")
					+ ";overflow:visible;\">");
			int f = 0;
			for (ExArquivoNumerado an : ans) {
				String numeracao = null;
				if (uuid != null)
					status = Status.update(uuid, "Agregando documento " + (f + 1) + "/" + ans.size(),
							f * 2 + 1, ans.size() * 2 + 1, 0L);
			// if (fFirst)
				// fFirst = false;
				// else
				// sb
				// .append("<div style=\"margin:10px; padding:10px; width:100%;
				// border: medium double green;\" class=\"total\">");
	
				sb.append("<div style=\"margin-bottom:6pt; padding:0pt; width:100%; clear:both; background-color: #fff; border: 1px solid #ccc; border-radius: 5px;\" class=\"documento\">");
				sb.append("<table width=\"100%\" style=\"padding:3pt;\" border=0><tr><td>");
				if (an.getPaginaInicial() != null) {
					numeracao = an.getPaginaInicial().toString();
					if (!an.getPaginaFinal().equals(an.getPaginaInicial()))
						numeracao += " - " + an.getPaginaFinal();
					sb.append("<div style=\"margin:3pt; padding:3pt; float:right; border: 1px solid #ccc; border-radius: 5px;\" class=\"numeracao\">");
					sb.append(numeracao);
					sb.append("</div>");
				}
				if (an.getArquivo().getHtml() != null) {
					String sHtml = fixHtml(contextpath, an);				
					sHtml = novoHtmlPersonalizado(sHtml).comBody().comBootstrap().comCSSInterno().obter();
							
					// sb
					// .append("<div style=\"margin:3pt; padding:3pt; border: thin
					// solid brown;\" class=\"dochtml\">");
					sb.append(sHtml);
					// sb.append("</div>");
				} else {
					sb.append("<div style=\"margin:3pt; padding:3pt;\" class=\"anexo\">");
					sb.append("<img src=\"/siga/css/famfamfam/icons/page_white_acrobat.png\"/> <a href=\""
							//+ "http://"
							//+ servernameport
							+ contextpath
							+ "/app/arquivo/exibir?arquivo="
							+ an.getArquivo().getReferenciaPDF()
							+ "\" target=\"_blank\">");
					sb.append(an.getNome());
					sb.append("</a>");
					if (an.getArquivo() instanceof ExMovimentacao) {
						if (((ExMovimentacao) an.getArquivo()).getDescrMov() != null)
							sb.append(": "
									+ ((ExMovimentacao) an.getArquivo())
											.getDescrMov());
					} else {
						if (((ExDocumento) an.getArquivo()).getDescrDocumento() != null)
							sb.append(": "
									+ ((ExDocumento) an.getArquivo()).getDescrDocumento());
					}
	
					sb.append("</div>");
				}
	
				if (an.getArquivo().getMensagem() != null
						&& an.getArquivo().getMensagem().trim().length() > 0) {
					sb.append("</td></tr><tr><td>");
					sb.append("<div style=\"margin:3pt; padding:3pt; border: 1px solid #ccc; border-radius: 5px; background-color:lightgreen;\" class=\"anexo\">");
					sb.append(an.getArquivo().getMensagem());
					sb.append("</div>");
				}
				sb.append("</td></tr></table></div>");
				f++;
			}
			sb.append("</body></html>");
			
			if (uuid != null)
				status = Status.update(uuid, "PDF completo gerado", ans.size() * 2 + 1, ans.size() * 2 + 1, 0L);
		}
	}

	public static String fixHtml(String contextpath, ExArquivoNumerado an)
			throws Exception {
		String sHtml = an.getArquivo().getHtmlComReferencias();
		sHtml = sHtml.replace("<!-- INICIO PRIMEIRO CABECALHO",
				"<!-- INICIO PRIMEIRO CABECALHO -->");
		sHtml = sHtml.replace("FIM PRIMEIRO CABECALHO -->",
				"<!-- FIM PRIMEIRO CABECALHO -->");
		sHtml = sHtml.replace("<!-- INICIO PRIMEIRO RODAPE",
				"<!-- INICIO PRIMEIRO RODAPE -->");
		sHtml = sHtml.replace("<!-- div style=\"font-size:11pt;\" class=\"footnotes\"",
				"<div style=\"font-size:11pt;\" class=\"footnotes\">");
		sHtml = sHtml.replace("FIM PRIMEIRO RODAPE -->",
				"<!-- FIM PRIMEIRO RODAPE-->");
		// s = s.replace("http://localhost:8080/siga/", "/siga/");
		sHtml = sHtml.replace("contextpath", contextpath);
		return sHtml;
	}
	
	public static String realPath() {
		
		RequestInfo ri = CurrentRequest.get();		
		String realPath = Contexto.urlBase(ri.getRequest()) + ri.getRequest().getContextPath();
		
		if (realPath.endsWith("/siga-le"))
			realPath = realPath.replace("/siga-le", "/sigaex");
		
		return realPath;
	}



}
