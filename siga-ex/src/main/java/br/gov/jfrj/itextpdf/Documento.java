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

import static br.gov.jfrj.siga.ex.util.ProcessadorHtml.novoHtmlPersonalizado;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.ex.ExArquivoNumerado;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.ex.ext.AbstractConversorHTMLFactory;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

import com.lowagie.text.Chunk;
import com.lowagie.text.Annotation;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.swetake.util.Qrcode;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.ex.ExArquivoNumerado;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
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
	
	private static float QRCODE_LEFT_MARGIN_IN_CM = 3.0f;
	private static float QRCODE_SIZE_IN_CM = 1.5f;
	private static float BARCODE_HEIGHT_IN_CM = 2.0f;
	private static int TEXT_TO_CIRCLE_INTERSPACE = 2;
	private static int TEXT_HEIGHT = 5;
	private static float SAFETY_MARGIN = 0.1f;
	private static float CM_UNIT = 72.0f / 2.54f;
	private static float PAGE_BORDER_IN_CM = 0.8f;
	private static float STAMP_BORDER_IN_CM = 0.2f;
	

	


	static {
		if (SigaMessages.isSigaSP()){ //Adequa marcas para SP
			QRCODE_LEFT_MARGIN_IN_CM = 0.6f;
			BARCODE_HEIGHT_IN_CM = 2.0f;
			PAGE_BORDER_IN_CM = 0.5f;
			STAMP_BORDER_IN_CM = 0.2f;
		}

	}

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
			Set<ExMovimentacao> movsAssinatura) {
		ArrayList<String> assinantes = new ArrayList<String>();
		for (ExMovimentacao movAssinatura : movsAssinatura) {
			if(movAssinatura.getCadastrante().getId().equals(movAssinatura.getSubscritor().getId())) {
				String s;
				if (movAssinatura.getExTipoMovimentacao().getId().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOLICITACAO_DE_ASSINATURA)) {
					s = Texto.maiusculasEMinusculas(movAssinatura.getCadastrante().getNomePessoa());
				} else {
					s = movAssinatura.getDescrMov().trim().toUpperCase();
					s = s.split(":")[0];
					s = s.intern();
				}
				if (!assinantes.contains(s)) {
					assinantes.add(s);
				}
			}
		}
		return assinantes;
	}
	
	public static ArrayList<String> getAssinantesStringListaComMatricula(
			Set<ExMovimentacao> movsAssinatura) {
		ArrayList<String> assinantes = new ArrayList<String>();
		for (ExMovimentacao movAssinatura : movsAssinatura) {
			String s;
			if (movAssinatura.getExTipoMovimentacao().getId().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOLICITACAO_DE_ASSINATURA)) {
				s = Texto.maiusculasEMinusculas(movAssinatura.getCadastrante().getNomePessoa());
			} else {
				s = movAssinatura.getDescrMov().trim().toUpperCase();
				s = s.replace(":", " - ");
				s = s.replace("EM SUBSTITUIÇÃO A", "em substituição a");
				s = s.intern();
			}
			if (!assinantes.contains(s)) {
				assinantes.add(s);
			}
		}
		return assinantes;
	}

	public static String getAssinantesString(Set<ExMovimentacao> movsAssinatura) {
		ArrayList<String> als = getAssinantesStringLista(movsAssinatura);
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
	
	public static String getAssinantesStringComMatricula(Set<ExMovimentacao> movsAssinatura) {
		ArrayList<String> als = getAssinantesStringListaComMatricula(movsAssinatura);
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

	public static byte[] stamp(byte[] abPdf, String sigla, boolean rascunho, boolean copia,
			boolean cancelado, boolean semEfeito, boolean internoProduzido,
			String qrCode, String mensagem, Integer paginaInicial,
			Integer paginaFinal, Integer cOmitirNumeracao, String instancia,
			String orgaoUsu, String marcaDaguaDoModelo) throws DocumentException, IOException {

		PdfReader pdfIn = new PdfReader(abPdf);
		Document doc = new Document(PageSize.A4, 0, 0, 0, 0);
		// final SimpleDateFormat sdf = new SimpleDateFormat(
		// "EEE MMM dd HH:mm:ss zzz yyyy");
		// doc.add(new Meta("creationdate", sdf.format(new Date(0L))));
		try (ByteArrayOutputStream boA4 = new ByteArrayOutputStream()) {
			/*-- Alterado de PdfWriter p/ PdfCopy(Essa classe permite manter os "stamps" originais do arquivo importado) 
			por Marcos(CMSP) em 21/02/19 --*/
			//PdfCopy writer = new PdfCopy(doc, boA4);
			/*-- Alerado de volta pois ficou desabilitado o redimensionamento do PDF de modo
			 *   a que os códigos de barra 2D e 3D não ficassem por cima do texto. Por Renato em 25/04/2019 --*/
			PdfWriter writer = PdfWriter.getInstance(doc, boA4);
			doc.open();
			PdfContentByte cb = writer.getDirectContent();
	
			// Resize every page to A4 size
			//
			// double thetaRotation = 0.0;
			for (int i = 1; i <= pdfIn.getNumberOfPages(); i++) {
				int rot = pdfIn.getPageRotation(i);
				float left = pdfIn.getPageSize(i).getLeft();
				float bottom = pdfIn.getPageSize(i).getBottom();
				float top = pdfIn.getPageSize(i).getTop();
				float right = pdfIn.getPageSize(i).getRight();
	
				PdfImportedPage page = writer.getImportedPage(pdfIn, i);
				float w = page.getWidth();
				float h = page.getHeight();
	
				// Logger.getRootLogger().error("----- dimensoes: " + rot + ", " + w
				// + ", " + h);
	
				doc.setPageSize((rot != 0 && rot != 180) ^ (w > h) ? PageSize.A4.rotate()
						: PageSize.A4);
				doc.newPage();							
	
				cb.saveState();
	
				if (rot != 0 && rot != 180) {
					float swap = w;
					w = h;
					h = swap;
				}
	
				float pw = doc.getPageSize().getWidth();
				float ph = doc.getPageSize().getHeight();
				double scale = Math.min(pw / w, ph / h);
	
				// do my transformations :
				cb.transform(AffineTransform.getScaleInstance(scale, scale));
	
				if (!internoProduzido) {
					cb.transform(AffineTransform.getTranslateInstance(pw
							* SAFETY_MARGIN, ph * SAFETY_MARGIN));
					cb.transform(AffineTransform.getScaleInstance(
							1.0f - 2 * SAFETY_MARGIN, 1.0f - 2 * SAFETY_MARGIN));
				}
	
				if (rot != 0) {
					double theta = -rot * (Math.PI / 180);
					if (rot == 180){
						cb.transform(AffineTransform.getRotateInstance(theta, w / 2,
								h / 2));
					}else{
						cb.transform(AffineTransform.getRotateInstance(theta, h / 2,
								w / 2));
					}
					if (rot == 90) {
						cb.transform(AffineTransform.getTranslateInstance(
								(w - h) / 2, (w - h) / 2));
					} else if (rot == 270) {
						cb.transform(AffineTransform.getTranslateInstance(
								(h - w) / 2, (h - w) / 2));
					}
				}
	
				// Logger.getRootLogger().error(
				// "----- dimensoes: " + rot + ", " + w + ", " + h);
				// Logger.getRootLogger().error("----- page: " + pw + ", " + ph);
	
				// cb.transform(AffineTransform.getTranslateInstance(
				// ((pw / scale) - w) / 2, ((ph / scale) - h) / 2));
	
				// put the page
				cb.addTemplate(page, 0, 0);
				/*-- Adicionado devido ao PdfCopy - por Marcos(CMSP) em 21/02/19 --*/
				// writer.addPage(page);
	
				// draw a red rectangle at the page borders
				//
				// cb.saveState();
				// cb.setColorStroke(Color.red);
				// cb.rectangle(pdfIn.getPageSize(i).getLeft(), pdfIn.getPageSize(i)
				// .getBottom(), pdfIn.getPageSize(i).getRight(), pdfIn
				// .getPageSize(i).getTop());
				// cb.stroke();
				// cb.restoreState();
	
				cb.restoreState();
			}
			doc.close();
	
			abPdf = boA4.toByteArray();
		}

		try (ByteArrayOutputStream bo2 = new ByteArrayOutputStream()) {
			final PdfReader reader = new PdfReader(abPdf);
	
			final int n = reader.getNumberOfPages();
			final PdfStamper stamp = new PdfStamper(reader, bo2);
	
			// adding content to each page
			int i = 0;
			PdfContentByte under;
			PdfContentByte over;
			final BaseFont helv = BaseFont.createFont("Helvetica",
					BaseFont.WINANSI, false);
	
			// Image img = Image.getInstance("watermark.jpg");
			final BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.EMBEDDED);
	
			byte maskr[] = { (byte) 0xff };
			Image mask = Image.getInstance(1, 1, 1, 1, maskr);
			mask.makeMask();
			mask.setInverted(true);
	
			while (i < n) {
				i++;
				// watermark under the existing page
				under = stamp.getUnderContent(i);
				over = stamp.getOverContent(i);
	
				final Barcode39 code39 = new Barcode39();
				// code39.setCode(doc.getCodigo());
				code39.setCode(sigla.replace("-", "").replace("/", "")
						.replace(".", ""));
				code39.setStartStopText(false);
				final Image image39 = code39.createImageWithBarcode(over, null,
						null);
				Rectangle r = stamp.getReader().getPageSizeWithRotation(i);
	
				image39.setInitialRotation((float) Math.PI / 2.0f);
				image39.setAbsolutePosition(r.getWidth() - image39.getHeight()
						+ (STAMP_BORDER_IN_CM - PAGE_BORDER_IN_CM) * CM_UNIT,
						BARCODE_HEIGHT_IN_CM * CM_UNIT);
	
				image39.setBackgroundColor(Color.green);
				image39.setBorderColor(Color.RED);
				image39.setBorderWidth(0.5f * CM_UNIT);
	
				image39.setImageMask(mask);
	
				over.setRGBColorFill(255, 255, 255);
				mask.setAbsolutePosition(r.getWidth() - image39.getHeight()
						- (PAGE_BORDER_IN_CM) * CM_UNIT,
						(BARCODE_HEIGHT_IN_CM - STAMP_BORDER_IN_CM) * CM_UNIT);
				mask.scaleAbsolute(image39.getHeight() + 2 * STAMP_BORDER_IN_CM
						* CM_UNIT, image39.getWidth() + 2 * STAMP_BORDER_IN_CM
						* CM_UNIT);
				over.addImage(mask);
	
				over.setRGBColorFill(0, 0, 0);
				over.addImage(image39);
				
				// Estampa o logo do Siga-Doc. Atenção, pedimos que esse logo seja preservado em 
				// todos os órgãos que utilizarem o Siga-Doc. Não se trata aqui da marca do TRF2,
				// mas sim da identificação do sistema Siga-Doc. É importante para a continuidade
				// do projeto que se faça essa divulgação.
				
				InputStream stream = Documento.class.getClassLoader()
						.getResourceAsStream("/br/gov/jfrj/itextpdf/logo-siga-novo-166px.png");
				byte[] ab = IOUtils.toByteArray(stream);
				final Image logo = Image.getInstance(ab);
//				
				logo.scaleToFit(image39.getHeight(), image39.getHeight());
				logo.setAbsolutePosition(r.getWidth() - image39.getHeight()
						+ (STAMP_BORDER_IN_CM - PAGE_BORDER_IN_CM) * CM_UNIT,
						PAGE_BORDER_IN_CM * CM_UNIT);
	
				logo.setBackgroundColor(Color.green);
				logo.setBorderColor(Color.RED);
				logo.setBorderWidth(0.5f * CM_UNIT);
				logo.setImageMask(mask);
	
				over.setRGBColorFill(255, 255, 255);
				mask.setAbsolutePosition(r.getWidth() - image39.getHeight()
						- (PAGE_BORDER_IN_CM) * CM_UNIT,
						(PAGE_BORDER_IN_CM - STAMP_BORDER_IN_CM) * CM_UNIT);
				mask.scaleAbsolute(image39.getHeight() + 2 * STAMP_BORDER_IN_CM
						* CM_UNIT, image39.getHeight() * logo.getHeight() / logo.getWidth() + 2 * STAMP_BORDER_IN_CM
						* CM_UNIT);
				over.addImage(mask);
	
				over.setRGBColorFill(255, 255, 255);
				logo.setAnnotation(new Annotation(0, 0, 0, 0, 
						"https://linksiga.trf2.jus.br")); 
				over.addImage(logo);
				
	
				// over.addImage(mask, mask.getScaledWidth() * 8, 0, 0,
				// mask.getScaledHeight() * 8, 100, 450);
	
				if (qrCode != null) {
					java.awt.Image imgQRCode = createQRCodeImage(qrCode);
					Image imageQRCode = Image.getInstance(imgQRCode, Color.BLACK,
							true);
					imageQRCode.scaleAbsolute(QRCODE_SIZE_IN_CM * CM_UNIT,
							QRCODE_SIZE_IN_CM * CM_UNIT);
					imageQRCode.setAbsolutePosition(QRCODE_LEFT_MARGIN_IN_CM
							* CM_UNIT, PAGE_BORDER_IN_CM * CM_UNIT);
	
					over.setRGBColorFill(255, 255, 255);
					mask.setAbsolutePosition(
							(QRCODE_LEFT_MARGIN_IN_CM - STAMP_BORDER_IN_CM)
									* CM_UNIT,
							(PAGE_BORDER_IN_CM - STAMP_BORDER_IN_CM) * CM_UNIT);
					mask.scaleAbsolute((QRCODE_SIZE_IN_CM + 2 * STAMP_BORDER_IN_CM)
							* CM_UNIT, (QRCODE_SIZE_IN_CM + 2 * STAMP_BORDER_IN_CM)
							* CM_UNIT);
					over.addImage(mask);
	
					over.setRGBColorFill(0, 0, 0);
					over.addImage(imageQRCode);
				}
	
				if (mensagem != null) {
					PdfPTable table = new PdfPTable(1);
					table.setTotalWidth(r.getWidth()
							- image39.getHeight()
							- (QRCODE_LEFT_MARGIN_IN_CM + QRCODE_SIZE_IN_CM + 4
									* STAMP_BORDER_IN_CM + PAGE_BORDER_IN_CM)
							* CM_UNIT);
					PdfPCell cell = new PdfPCell(new Paragraph(mensagem,
							FontFactory.getFont(FontFactory.HELVETICA, 8,
									Font.NORMAL, Color.BLACK)));
					cell.setBorderWidth(0);
					table.addCell(cell);
	
					over.setRGBColorFill(255, 255, 255);
					mask.setAbsolutePosition((QRCODE_LEFT_MARGIN_IN_CM
							+ QRCODE_SIZE_IN_CM + STAMP_BORDER_IN_CM)
							* CM_UNIT, (PAGE_BORDER_IN_CM - STAMP_BORDER_IN_CM)
							* CM_UNIT);
					mask.scaleAbsolute(
							2 * STAMP_BORDER_IN_CM * CM_UNIT
									+ table.getTotalWidth(), 2 * STAMP_BORDER_IN_CM
									* CM_UNIT + table.getTotalHeight());
					over.addImage(mask);
	
					over.setRGBColorFill(0, 0, 0);
					table.writeSelectedRows(0, -1, (QRCODE_LEFT_MARGIN_IN_CM
							+ QRCODE_SIZE_IN_CM + 2 * STAMP_BORDER_IN_CM)
							* CM_UNIT, table.getTotalHeight() + PAGE_BORDER_IN_CM
							* CM_UNIT, over);
				}
	
				if (cancelado) {
					tarjar("CANCELADO", over, helv, r);
				} else if (rascunho && copia) {
					tarjar("CÓPIA DE MINUTA", over, helv, r);
				} else if (rascunho) {
					tarjar("MINUTA", over, helv, r);
				} else if (semEfeito) {
					tarjar("SEM EFEITO", over, helv, r);
				} else if (copia) {
					tarjar("CÓPIA", over, helv, r);
				} else if (SigaMessages.isSigaSP() && ("treinamento".equals(SigaExProperties.getAmbiente())) ) {
					tarjar("CAPACITAÇÃO", over, helv, r);
				} else if (SigaMessages.isSigaSP() && ("homolog".equals(SigaExProperties.getAmbiente())) ) {
					tarjar("HOMOLOGAÇÃO", over, helv, r);
				} else if (!marcaDaguaDoModelo.isEmpty()) {
					tarjar(marcaDaguaDoModelo, over, helv, r);
				} else if (!SigaMessages.isSigaSP() && !SigaExProperties.isAmbienteProducao()) {
					tarjar("INVÁLIDO", over, helv, r);
				}				
	
				// Imprime um circulo com o numero da pagina dentro.

				if (paginaInicial != null) {
					String sFl = String.valueOf(paginaInicial + i - 1);
					// Se for a ultima pagina e o numero nao casar, acrescenta "-" e
					// pagina final
					if (n == i) {
						if (paginaFinal != paginaInicial + n - 1) {
							sFl = sFl + "-" + String.valueOf(paginaFinal);
						}
					}
					if (i > cOmitirNumeracao) {
						//tamanho fonte número
						int textHeight = 23;
						// Raio do circulo interno
						float radius = 18f;
						
						if (SigaMessages.isSigaSP()) {
							//tamanho fonte número
							textHeight = 12;
							// Raio do circulo interno
							radius = 12f;
							//não exibe órgão
							orgaoUsu = "";
						} 
						
						// Distancia entre o circulo interno e o externo
						final float circleInterspace = Math.max(
								helv.getAscentPoint(instancia, TEXT_HEIGHT),
								helv.getAscentPoint(orgaoUsu, TEXT_HEIGHT))
								- Math.min(helv.getDescentPoint(instancia,
										TEXT_HEIGHT), helv.getDescentPoint(
										orgaoUsu, TEXT_HEIGHT))
								+ 2
								* TEXT_TO_CIRCLE_INTERSPACE;
	
						// Centro do circulo
						float xCenter = r.getWidth() - 1.8f
								* (radius + circleInterspace);
						float yCenter = r.getHeight() - 1.8f
								* (radius + circleInterspace);
						
						over.saveState();
						final PdfGState gs = new PdfGState();
						gs.setFillOpacity(1f);
						over.setGState(gs);
						over.setColorFill(Color.BLACK);
	
						over.saveState();
						over.setColorStroke(Color.black);
						over.setLineWidth(1f);
						over.setColorFill(Color.WHITE);
	
						// Circulo externo
						over.circle(xCenter, yCenter, radius + circleInterspace);
						over.fill();
						over.circle(xCenter, yCenter, radius + circleInterspace);
						over.stroke();
	
						// Circulo interno
						over.circle(xCenter, yCenter, radius);
						over.stroke();
						over.restoreState();
	
						{
							over.saveState();
							over.beginText();
							over.setFontAndSize(helv, TEXT_HEIGHT);
	
							// Escreve o texto superior do carimbo
							float fDescent = helv.getDescentPoint(instancia,
									TEXT_HEIGHT);
							showTextOnArc(over, instancia, helv, TEXT_HEIGHT,
									xCenter, yCenter, radius - fDescent
											+ TEXT_TO_CIRCLE_INTERSPACE, true);
	
							// Escreve o texto inferior
							float fAscent = helv.getAscentPoint(orgaoUsu,
									TEXT_HEIGHT);
							showTextOnArc(over, orgaoUsu, helv, TEXT_HEIGHT,
									xCenter, yCenter, radius + fAscent
											+ TEXT_TO_CIRCLE_INTERSPACE, false);
							over.endText();
							over.restoreState();
						}
	
						over.beginText();
							
						// Diminui o tamanho do font ate que o texto caiba dentro do
						// circulo interno
						while (helv.getWidthPoint(sFl, textHeight) > (2 * (radius - TEXT_TO_CIRCLE_INTERSPACE)))
							textHeight--;
						float fAscent = helv.getAscentPoint(sFl, textHeight)
								+ helv.getDescentPoint(sFl, textHeight);
						over.setFontAndSize(helv, textHeight);
						over.showTextAligned(Element.ALIGN_CENTER, sFl, xCenter,
								yCenter - 0.5f * fAscent, 0);
						over.endText();
						over.restoreState();
					}
				}
	
			}
			stamp.close();
			return bo2.toByteArray();
		}
	}

	private static void tarjar(String tarja, PdfContentByte over, final BaseFont helv, Rectangle r) {
		over.saveState();
		final PdfGState gs = new PdfGState();
		gs.setFillOpacity(0.5f);
		over.setGState(gs);
		over.setColorFill(Color.GRAY);
		over.beginText();
		over.setFontAndSize(helv, 72);
		over.showTextAligned(Element.ALIGN_CENTER, tarja,
				r.getWidth() / 2, r.getHeight() / 2, 45);
		over.endText();
		over.restoreState();
	}

	// Desenha texto ao redor de um circulo, acima ou abaixo
	//
	private static void showTextOnArc(PdfContentByte cb, String text,
			BaseFont font, float textHeight, float xCenter, float yCenter,
			float radius, boolean top) {
		float fTotal = 0;
		float aPos[] = new float[text.length()];
		for (int i = 0; i < text.length(); i++) {
			float f = font.getWidthPoint(text.substring(i, i + 1), textHeight);
			aPos[i] = f / 2 + fTotal;
			fTotal += f;
		}
		float fAscent = font.getAscentPoint(text, textHeight);

		for (int i = 0; i < text.length(); i++) {
			float theta;
			if (top)
				theta = (float) ((aPos[i] - fTotal / 2) / radius);
			else
				theta = (float) (-1 * (aPos[i] - fTotal / 2)
						/ (radius - fAscent) + Math.PI);
			cb.showTextAligned(Element.ALIGN_CENTER, text.substring(i, i + 1),
					xCenter + radius * (float) Math.sin(theta), yCenter
							+ radius * (float) Math.cos(theta),
					(float) ((-theta + (top ? 0 : Math.PI)) * 180 / Math.PI));
		}
		return;
	}

	public static java.awt.Image createQRCodeImage(String url) {
		Qrcode x = new Qrcode();

		x.setQrcodeErrorCorrect('M'); // 15%
		x.setQrcodeEncodeMode('B'); // Bynary
		boolean[][] matrix = x.calQrcode(url.getBytes());

		// Canvas canvas = new Canvas();
		// java.awt.Image img = canvas.createImage(matrix.length,
		// matrix.length);
		// Graphics g = img.getGraphics();
		// g.setColor(Color.BLACK);
		// img.getGraphics().clearRect(0, 0, matrix.length, matrix.length);
		byte ab[] = new byte[matrix.length * matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[j][i]) {
					// img.getGraphics().drawLine(j, i, j, i);
					ab[i * matrix.length + j] = 0;
				} else {
					ab[i * matrix.length + j] = -1;
				}
			}
		}
		BufferedImage img = new BufferedImage(matrix.length, matrix.length,
				BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = img.getRaster();
		wr.setDataElements(0, 0, matrix.length, matrix.length, ab);

		// buffered_image.setRGB (0, 0, matrix.length, matrix.length, ab, 0,
		// matrix.length);

		// java.awt.Image img = Toolkit.getDefaultToolkit().createImage(ab,
		// matrix.length, matrix.length);

		return img;
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
		return getDocumento(mob, mov, false, true, null, null);
	}

	public static byte[] getDocumento(ExMobil mob, ExMovimentacao mov,
			boolean completo, boolean estampar, String hash, byte[] certificado)
			throws Exception {
		final ByteArrayOutputStream bo2 = new ByteArrayOutputStream();
		PdfReader reader;
		int n;
		int pageOffset = 0;
		ArrayList master = new ArrayList();
		int f = 0;
		Document document = null;
		PdfCopy writer = null;
		int nivelInicial = 0;

		// if (request.getRequestURI().indexOf("/completo/") == -1) {
		// return getPdf(docvia, mov != null ? mov : docvia.getExDocumento(),
		// mov != null ? mov.getNumVia() : docvia.getNumVia(), null,
		// null, request);
		// }

		List<ExArquivoNumerado> ans = mob.filtrarArquivosNumerados(mov,
				completo);

		if (!completo && !estampar && ans.size() == 1) {
			if (hash != null) {
				// Calcula o hash do documento
				String alg = hash;
				MessageDigest md = MessageDigest.getInstance(alg);
				md.update(ans.get(0).getArquivo().getPdf());
				return md.digest();
			} else {
				return ans.get(0).getArquivo().getPdf();
			}
		}

		try {
			for (ExArquivoNumerado an : ans) {

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

				byte[] ab = !estampar ? an.getArquivo().getPdf() : stamp(an
						.getArquivo().getPdf(), sigla, an.getArquivo()
						.isRascunho(), an.isCopia(), an.getArquivo().isCancelado(), an
						.getArquivo().isSemEfeito(), an.getArquivo()
						.isInternoProduzido(), an.getArquivo().getQRCode(), an
						.getArquivo().getMensagem(), an.getPaginaInicial(),
						an.getPaginaFinal(), an.getOmitirNumeracao(),
						SigaExProperties.getTextoSuperiorCarimbo(), mob
								.getExDocumento().getOrgaoUsuario()
								.getDescricao(), mob.getExDocumento().getMarcaDagua());							

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
					writer = new PdfCopy(document, bo2);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bo2.toByteArray();
	}

	public static byte[] generatePdf(String sHtml) throws Exception {
		return generatePdf(sHtml, AbstractConversorHTMLFactory.getInstance()
				.getConversorPadrao());
	}

	public static byte[] generatePdf(String sHtml, ConversorHtml parser)
			throws Exception {
		sHtml = (new ProcessadorHtml()).canonicalizarHtml(sHtml, true, false,
				true, false, true);

		sHtml = sHtml.replace("contextpath", realPath());

		return parser.converter(sHtml, ConversorHtml.PDF);

	}

	public static byte[] getDocumentoHTML(ExMobil mob, ExMovimentacao mov,
			boolean completo, String contextpath, String servernameport)
			throws Exception {
		List<ExArquivoNumerado> ans = mob.filtrarArquivosNumerados(mov,
				completo);

		StringBuilder sb = new StringBuilder();
		boolean fFirst = true;
		// TAH: infelizmente o IE não funciona bem com background-color:
		// transparent.
		// sb.append("<html class=\"fisico\"><body style=\"margin:2px; padding:0pt; background-color: #E2EAEE;overflow:visible;\">");
		sb.append("<html><head><base target=\"_parent\"/></head><body style=\"margin:2px; padding:0pt; background-color: "
				+ (mob.getDoc().isEletronico() ? "#E2EAEE" : "#f1e9c6")
				+ ";overflow:visible;\">");
		for (ExArquivoNumerado an : ans) {
			String numeracao = null;
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
		}
		sb.append("</body></html>");

		return sb.toString().getBytes("utf-8");
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
