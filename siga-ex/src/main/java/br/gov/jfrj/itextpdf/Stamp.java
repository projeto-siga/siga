package br.gov.jfrj.itextpdf;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.swetake.util.Qrcode;

import br.gov.jfrj.itextpdf.LocalizaAnotacao.LocalizaAnotacaoResultado;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.base.SigaMessages;

public class Stamp {
	private static final String VALIDAR_ASSINATURA_URL = "/sigaex/app/validar-assinatura?pessoa=";
	private static float QRCODE_LEFT_MARGIN_IN_CM = 0.6f;
	private static float QRCODE_SIZE_IN_CM = 1.5f;
	private static float BARCODE_HEIGHT_IN_CM = 2.0f;
	private static int TEXT_TO_CIRCLE_INTERSPACE = 2;
	private static int TEXT_HEIGHT = 5;
	private static float SAFETY_MARGIN = 0.1f;
	private static float CM_UNIT = 72.0f / 2.54f;
	private static float PAGE_BORDER_IN_CM = 0.5f;
	private static float STAMP_BORDER_IN_CM = 0.2f;

	//Default Com Redimensionamento para folha A4
	public static byte[] stamp(byte[] abPdf, String sigla, boolean rascunho, boolean copia, boolean cancelado,
			boolean semEfeito, boolean internoProduzido, String qrCode, String mensagem, Integer paginaInicial,
			Integer paginaFinal, Integer cOmitirNumeracao, String instancia, String orgaoUsu, String marcaDaguaDoModelo,
			List<Long> idsAssinantes) throws DocumentException, IOException {

		return stamp(abPdf, sigla, rascunho, copia, cancelado, semEfeito, internoProduzido, qrCode, mensagem,
				paginaInicial, paginaFinal, cOmitirNumeracao, instancia, orgaoUsu, marcaDaguaDoModelo, idsAssinantes,
				false, false);
	}

	public static byte[] stamp(byte[] abPdf, String sigla, boolean rascunho, boolean copia, boolean cancelado,
			boolean semEfeito, boolean internoProduzido, String qrCode, String mensagem, Integer paginaInicial,
			Integer paginaFinal, Integer cOmitirNumeracao, String instancia, String orgaoUsu, String marcaDaguaDoModelo,
			List<Long> idsAssinantes, boolean tamanhoOriginal, boolean reduzirVisuAssinPdf) throws DocumentException, IOException {
			
		if (idsAssinantes != null && idsAssinantes.size() > 0 && Prop.getBool("assinatura.estampar"))
			abPdf = estamparAssinaturas(abPdf, idsAssinantes);

		PdfReader pdfIn = new PdfReader(abPdf);
		
		if (!tamanhoOriginal) { 
			Document doc = new Document(PageSize.A4, 0, 0, 0, 0);
			try (ByteArrayOutputStream boA4 = new ByteArrayOutputStream()) {
	
				PdfWriter writer = PdfWriter.getInstance(doc, boA4);
				doc.open();
				PdfContentByte cb = writer.getDirectContent();
				
				// Resize every page to A4 size
				for (int i = 1; i <= pdfIn.getNumberOfPages(); i++) {
					int rot = pdfIn.getPageRotation(i);
					float left = pdfIn.getPageSize(i).getLeft();
					float bottom = pdfIn.getPageSize(i).getBottom();
					float top = pdfIn.getPageSize(i).getTop();
					float right = pdfIn.getPageSize(i).getRight();
	
					PdfImportedPage page = writer.getImportedPage(pdfIn, i);
					float w = page.getWidth();
					float h = page.getHeight();
	
					doc.setPageSize((rot != 0 && rot != 180) ^ (w > h) ? PageSize.A4.rotate() : PageSize.A4);
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
						cb.transform(AffineTransform.getTranslateInstance(pw * SAFETY_MARGIN, ph * SAFETY_MARGIN));
						cb.transform(AffineTransform.getScaleInstance(1.0f - 2 * SAFETY_MARGIN, 1.0f - 2 * SAFETY_MARGIN));
					}
	
					if (rot != 0) {
						double theta = -rot * (Math.PI / 180);
						if (rot == 180) {
							cb.transform(AffineTransform.getRotateInstance(theta, w / 2, h / 2));
						} else {
							cb.transform(AffineTransform.getRotateInstance(theta, h / 2, w / 2));
						}
						if (rot == 90) {
							cb.transform(AffineTransform.getTranslateInstance((w - h) / 2, (w - h) / 2));
						} else if (rot == 270) {
							cb.transform(AffineTransform.getTranslateInstance((h - w) / 2, (h - w) / 2));
						}
					}
	
					// put the page
					cb.addTemplate(page, 0, 0);
					cb.restoreState();
				}
				doc.close();
	
				abPdf = boA4.toByteArray();
			}
		}

		try (ByteArrayOutputStream bo2 = new ByteArrayOutputStream()) {
			final PdfReader reader = new PdfReader(abPdf);

			final int n = reader.getNumberOfPages();
			final PdfStamper stamp = new PdfStamper(reader, bo2);

			// adding content to each page
			int i = 0;
			PdfContentByte under;
			PdfContentByte over;
			final BaseFont helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);

			// Image img = Image.getInstance("watermark.jpg");
			final BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);

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
				code39.setCode(sigla.replace("-", "").replace("/", "").replace(".", ""));
				code39.setStartStopText(false);
				final Image image39 = code39.createImageWithBarcode(over, null, null);
				Rectangle r = stamp.getReader().getPageSizeWithRotation(i);

				image39.setInitialRotation((float) Math.PI / 2.0f);
				image39.setAbsolutePosition(
						r.getWidth() - image39.getHeight() + (STAMP_BORDER_IN_CM - PAGE_BORDER_IN_CM) * CM_UNIT,
						BARCODE_HEIGHT_IN_CM * CM_UNIT);

				image39.setBackgroundColor(Color.green);
				image39.setBorderColor(Color.RED);
				image39.setBorderWidth(0.5f * CM_UNIT);

				image39.setImageMask(mask);

				over.setRGBColorFill(255, 255, 255);
				mask.setAbsolutePosition(r.getWidth() - image39.getHeight() - (PAGE_BORDER_IN_CM) * CM_UNIT,
						(BARCODE_HEIGHT_IN_CM - STAMP_BORDER_IN_CM) * CM_UNIT);
				mask.scaleAbsolute(image39.getHeight() + 2 * STAMP_BORDER_IN_CM * CM_UNIT,
						image39.getWidth() + 2 * STAMP_BORDER_IN_CM * CM_UNIT);
				over.addImage(mask);

				over.setRGBColorFill(0, 0, 0);
				over.addImage(image39);

				// Estampa o logo do Siga-Doc. Atenção, pedimos que esse logo seja preservado em
				// todos os órgãos que utilizarem o Siga-Doc. Não se trata aqui da marca do
				// TRF2,
				// mas sim da identificação do sistema Siga-Doc. É importante para a
				// continuidade
				// do projeto que se faça essa divulgação.

				InputStream stream = Documento.class.getClassLoader()
						.getResourceAsStream("/br/gov/jfrj/itextpdf/logo-siga-novo-166px.png");
				if (stream != null) {
					byte[] ab = IOUtils.toByteArray(stream);
					final Image logo = Image.getInstance(ab);
			
					logo.scaleToFit(image39.getHeight(), image39.getHeight());
					logo.setAbsolutePosition(
							r.getWidth() - image39.getHeight() + (STAMP_BORDER_IN_CM - PAGE_BORDER_IN_CM) * CM_UNIT,
							PAGE_BORDER_IN_CM * CM_UNIT);

					logo.setBackgroundColor(Color.green);
					logo.setBorderColor(Color.RED);
					logo.setBorderWidth(0.5f * CM_UNIT);
					logo.setImageMask(mask);

					over.setRGBColorFill(255, 255, 255);
					mask.setAbsolutePosition(r.getWidth() - image39.getHeight() - (PAGE_BORDER_IN_CM) * CM_UNIT,
							(PAGE_BORDER_IN_CM - STAMP_BORDER_IN_CM) * CM_UNIT);
					mask.scaleAbsolute(image39.getHeight() + 2 * STAMP_BORDER_IN_CM * CM_UNIT,
							image39.getHeight() * logo.getHeight() / logo.getWidth()
									+ 2 * STAMP_BORDER_IN_CM * CM_UNIT);
					over.addImage(mask);

					over.setRGBColorFill(255, 255, 255);
					logo.setAnnotation(new Annotation(0, 0, 0, 0, "https://linksiga.trf2.jus.br"));

					if (Prop.isGovSP()) {
						if (i == 1)
							over.addImage(logo);
					} else {
						over.addImage(logo);
					}
				}

				if (qrCode != null) {
					java.awt.Image imgQRCode = createQRCodeImage(qrCode);
					Image imageQRCode = Image.getInstance(imgQRCode, Color.BLACK, true);
					imageQRCode.scaleAbsolute(QRCODE_SIZE_IN_CM * CM_UNIT, QRCODE_SIZE_IN_CM * CM_UNIT);
					imageQRCode.setAbsolutePosition(QRCODE_LEFT_MARGIN_IN_CM * CM_UNIT, PAGE_BORDER_IN_CM * CM_UNIT);

					over.setRGBColorFill(255, 255, 255);
					mask.setAbsolutePosition((QRCODE_LEFT_MARGIN_IN_CM - STAMP_BORDER_IN_CM) * CM_UNIT,
							(PAGE_BORDER_IN_CM - STAMP_BORDER_IN_CM) * CM_UNIT);
					mask.scaleAbsolute((QRCODE_SIZE_IN_CM + 2 * STAMP_BORDER_IN_CM) * CM_UNIT,
							(QRCODE_SIZE_IN_CM + 2 * STAMP_BORDER_IN_CM) * CM_UNIT);
					over.addImage(mask);

					over.setRGBColorFill(0, 0, 0);
					over.addImage(imageQRCode);
				}

				if (mensagem != null) {
					String msg = gerarReducaoAssinaturas(reduzirVisuAssinPdf, mensagem);
					PdfPTable table = new PdfPTable(1);
					table.setTotalWidth(r.getWidth() - image39.getHeight() - (QRCODE_LEFT_MARGIN_IN_CM
							+ QRCODE_SIZE_IN_CM + 4 * STAMP_BORDER_IN_CM + PAGE_BORDER_IN_CM) * CM_UNIT);
					PdfPCell cell = new PdfPCell(new Paragraph(msg,
							FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, Color.BLACK)));
					cell.setBorderWidth(0);
					table.addCell(cell);

					over.setRGBColorFill(255, 255, 255);
					mask.setAbsolutePosition(
							(QRCODE_LEFT_MARGIN_IN_CM + QRCODE_SIZE_IN_CM + STAMP_BORDER_IN_CM) * CM_UNIT,
							(PAGE_BORDER_IN_CM - STAMP_BORDER_IN_CM) * CM_UNIT);
					mask.scaleAbsolute(2 * STAMP_BORDER_IN_CM * CM_UNIT + table.getTotalWidth(),
							2 * STAMP_BORDER_IN_CM * CM_UNIT + table.getTotalHeight());
					over.addImage(mask);

					over.setRGBColorFill(0, 0, 0);
					table.writeSelectedRows(0, -1,
							(QRCODE_LEFT_MARGIN_IN_CM + QRCODE_SIZE_IN_CM + 2 * STAMP_BORDER_IN_CM) * CM_UNIT,
							table.getTotalHeight() + PAGE_BORDER_IN_CM * CM_UNIT, over);
				}

				if (cancelado) {
					tarjar(SigaMessages.getMessage("marcador.cancelado.label").toUpperCase(), over, helv, r);
				} else if (rascunho && copia) {
					tarjar("CÓPIA DE MINUTA", over, helv, r);
				} else if (rascunho) {
					tarjar("MINUTA", over, helv, r);
				} else if (semEfeito) {
					tarjar(SigaMessages.getMessage("marcador.semEfeito.label").toUpperCase(), over, helv, r);
				} else if (copia) {
					tarjar("CÓPIA", over, helv, r);
				} else if (SigaMessages.isSigaSP() && ("treinamento".equals(Prop.get("/siga.ambiente")))) {
					tarjar("CAPACITAÇÃO", over, helv, r);
				} else if (SigaMessages.isSigaSP() && ("homolog".equals(Prop.get("/siga.ambiente")))) {
					tarjar("HOMOLOGAÇÃO", over, helv, r);
				} else if (!marcaDaguaDoModelo.isEmpty()) {
					tarjar(marcaDaguaDoModelo, over, helv, r);
				} else if (!SigaMessages.isSigaSP() && !"prod".equals(Prop.get("/siga.ambiente"))) {
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
						// tamanho fonte número
						int textHeight = 23;
						// Raio do circulo interno
						float radius = 18f;

						if (SigaMessages.isSigaSP()) {
							// tamanho fonte número
							textHeight = 12;
							// Raio do circulo interno
							radius = 12f;
							// não exibe órgão
							orgaoUsu = "";
						}

						// Distancia entre o circulo interno e o externo
						final float circleInterspace = Math.max(helv.getAscentPoint(instancia, TEXT_HEIGHT),
								helv.getAscentPoint(orgaoUsu, TEXT_HEIGHT))
								- Math.min(helv.getDescentPoint(instancia, TEXT_HEIGHT),
										helv.getDescentPoint(orgaoUsu, TEXT_HEIGHT))
								+ 2 * TEXT_TO_CIRCLE_INTERSPACE;

						// Centro do circulo
						float xCenter = r.getWidth() - 1.8f * (radius + circleInterspace);
						float yCenter = r.getHeight() - 1.8f * (radius + circleInterspace);

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
							float fDescent = helv.getDescentPoint(instancia, TEXT_HEIGHT);
							showTextOnArc(over, instancia, helv, TEXT_HEIGHT, xCenter, yCenter,
									radius - fDescent + TEXT_TO_CIRCLE_INTERSPACE, true);

							// Escreve o texto inferior
							float fAscent = helv.getAscentPoint(orgaoUsu, TEXT_HEIGHT);
							showTextOnArc(over, orgaoUsu, helv, TEXT_HEIGHT, xCenter, yCenter,
									radius + fAscent + TEXT_TO_CIRCLE_INTERSPACE, false);
							over.endText();
							over.restoreState();
						}

						over.beginText();

						// Diminui o tamanho do font ate que o texto caiba dentro do
						// circulo interno
						while (helv.getWidthPoint(sFl, textHeight) > (2 * (radius - TEXT_TO_CIRCLE_INTERSPACE)))
							textHeight--;
						float fAscent = helv.getAscentPoint(sFl, textHeight) + helv.getDescentPoint(sFl, textHeight);
						over.setFontAndSize(helv, textHeight);
						over.showTextAligned(Element.ALIGN_CENTER, sFl, xCenter, yCenter - 0.5f * fAscent, 0);
						over.endText();
						over.restoreState();
					}
				}

			}
			stamp.close();
			byte[] pdf = bo2.toByteArray();
			return pdf;
		}
	}
	
	private static String gerarReducaoAssinaturas(boolean reduzirVisuAssinPdf, String mensagem) {
		Pattern pattern = Pattern.compile("\\b(Assinado|Autenticado)\\b");
		if (reduzirVisuAssinPdf && pattern.matcher(mensagem).find()) {
			pattern = Pattern.compile("\\b(Assinado|Autenticado)\\b|(,| e )|(\\bDocumento Nº: \\b)");
	        Matcher mm = pattern.matcher(mensagem);
			
	        Map<Integer, Integer> assinaturas = new HashMap<Integer, Integer>();
	        int contador=0;
			while(mm.find()) {
				//System.out.println(mm.start() +" "+ mm.group() +" "+ mm.end());
				assinaturas.put(++contador, mm.start());
	        }
			if (!assinaturas.isEmpty() && assinaturas.size() > 2) {
				StringBuilder str = new StringBuilder();
				//print 2 assinaturas
				str.append(mensagem.substring(assinaturas.get(1), assinaturas.get(3)));
				str.append(" +" + (assinaturas.size() - (mensagem.substring(assinaturas.get(assinaturas.size())).contains("Documento Nº:") ? 3 : 2)) 
									+ " Pessoas - Para verificar todas as assinaturas consulte o link de autenticação. \n");
				str.append(mensagem.substring(assinaturas.get(assinaturas.size())));
				return str.toString();
			}
		}
		return mensagem;
	}

	private static byte[] estamparAssinaturas(byte[] pdf, List<Long> idsAssinantes) {
		try {
			PDDocument doc;
			doc = PDDocument.load(pdf);

			List<String> seek = new ArrayList<>();
			for (Long id : idsAssinantes)
				seek.add(VALIDAR_ASSINATURA_URL + id + "&");

			List<LocalizaAnotacaoResultado> l = LocalizaAnotacao.localizar(doc, seek);
			if (l == null)
				return pdf;

			byte[] abStamp = SigaHTTP
					.convertStreamToByteArray(Stamp.class.getResourceAsStream("assinado-digitalmente.png"), 4096);
			PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, abStamp, "assinado digitalmente");

			Set<String> set = new HashSet<>();

			for (LocalizaAnotacaoResultado i : l) {
				System.out.println("achei: " + i.page + ", (" + i.lowerLeftX + ", " + i.upperRightY + ")");
				if (set.contains(i.uri))
					continue;
				set.add(i.uri);
				System.out.println("processando: " + i.page + ", (" + i.lowerLeftX + ", " + i.upperRightY + ")");

				PDPage page = doc.getPage(i.page - 1);

				PDPageContentStream contents = new PDPageContentStream(doc, page, true, true);
				float height = i.height * 1.2f;
				float width = pdImage.getWidth() * (height / pdImage.getHeight());
				float lowerLeftX = (i.lowerLeftX + i.width / 2) - width / 2;
				float upperRightY = i.upperRightY;
				contents.drawImage(pdImage, lowerLeftX, upperRightY, width, height);
				System.out.println("Image inserted");
				contents.close();
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			//* Foi necessário incluir as instruções abaixo porque documentos assinados digitalmente com assinatura atached fora do SIGA
			//  estão vindo com proteção para gravação quando lisdos pelo reader pdf nativo.  Não ocorre no pdfjs.
			if (doc.isEncrypted()) { //remove the security before adding protections
				    doc.setAllSecurityToBeRemoved(true);
			}
			doc.save(baos);
			doc.close();
			baos.close();
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

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
		BufferedImage img = new BufferedImage(matrix.length, matrix.length, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = img.getRaster();
		wr.setDataElements(0, 0, matrix.length, matrix.length, ab);

		// buffered_image.setRGB (0, 0, matrix.length, matrix.length, ab, 0,
		// matrix.length);

		// java.awt.Image img = Toolkit.getDefaultToolkit().createImage(ab,
		// matrix.length, matrix.length);

		return img;
	}

	private static void tarjar(String tarja, PdfContentByte over, final BaseFont helv, Rectangle r) {
		over.saveState();
		final PdfGState gs = new PdfGState();
		gs.setFillOpacity(0.5f);
		over.setGState(gs);
		over.setColorFill(Color.GRAY);
		over.beginText();
		over.setFontAndSize(helv, 72);
		over.showTextAligned(Element.ALIGN_CENTER, tarja, r.getWidth() / 2, r.getHeight() / 2, 45);
		over.endText();
		over.restoreState();
	}

	// Desenha texto ao redor de um circulo, acima ou abaixo
	//
	private static void showTextOnArc(PdfContentByte cb, String text, BaseFont font, float textHeight, float xCenter,
			float yCenter, float radius, boolean top) {
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
				theta = (float) (-1 * (aPos[i] - fTotal / 2) / (radius - fAscent) + Math.PI);
			cb.showTextAligned(Element.ALIGN_CENTER, text.substring(i, i + 1),
					xCenter + radius * (float) Math.sin(theta), yCenter + radius * (float) Math.cos(theta),
					(float) ((-theta + (top ? 0 : Math.PI)) * 180 / Math.PI));
		}
		return;
	}

}
