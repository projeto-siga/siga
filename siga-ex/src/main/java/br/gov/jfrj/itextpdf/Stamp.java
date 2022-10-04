package br.gov.jfrj.itextpdf;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private static float QRCODE_LEFT_MARGIN_IN_CM = 3.0f;
	private static float QRCODE_SIZE_IN_CM = 1.5f;
	private static float BARCODE_HEIGHT_IN_CM = 2.0f;
	private static int TEXT_TO_CIRCLE_INTERSPACE = 2;
	private static int TEXT_HEIGHT = 5;
	private static float SAFETY_MARGIN = 0.1f;
	private static float CM_UNIT = 72.0f / 2.54f;
	private static float PAGE_BORDER_IN_CM = 0.8f;
	private static float STAMP_BORDER_IN_CM = 0.2f;
	private static PdfContentByte cb;
	private static PdfWriter writer;
	private static Document doc;
	private static PdfImportedPage page;
	private static int pageNumber = 1;

	static {
		if (SigaMessages.isSigaSP()) { // Adequa marcas para SP
			QRCODE_LEFT_MARGIN_IN_CM = 0.6f;
			BARCODE_HEIGHT_IN_CM = 2.0f;
			PAGE_BORDER_IN_CM = 0.5f;
			STAMP_BORDER_IN_CM = 0.2f;
		}

	}
	
    public static byte[] stamp(byte[] abPdf, String sigla, boolean rascunho, boolean copia, boolean cancelado,
			boolean semEfeito, boolean internoProduzido, String qrCode, String mensagem, Integer paginaInicial,
			Integer paginaFinal, Integer cOmitirNumeracao, String instancia, String orgaoUsu, String marcaDaguaDoModelo,
			List<Long> idsAssinantes) throws DocumentException, IOException {
        
        PdfReader pdfIn0 = new PdfReader(abPdf);
        float width = pdfIn0.getPageSize(1).getWidth();
        float height = pdfIn0.getPageSize(1).getHeight();    
        pdfIn0.close();
        
        if(width >= 1189 && height >= 841) {
            return stamp(abPdf, sigla, rascunho,  copia,  cancelado,
                    semEfeito,  internoProduzido,  qrCode,  mensagem,  paginaInicial,
                    paginaFinal,  cOmitirNumeracao,  instancia,  orgaoUsu,  marcaDaguaDoModelo,
                    idsAssinantes,  PageSize.A0);
        }
        
        else {
            pageNumber++;
    		return stamp(abPdf, sigla, rascunho,  copia,  cancelado,
    	             semEfeito,  internoProduzido,  qrCode,  mensagem,  paginaInicial,
    	             paginaFinal,  cOmitirNumeracao,  instancia,  orgaoUsu,  marcaDaguaDoModelo,
    	             idsAssinantes,  PageSize.A4);
        }
	}

	public static byte[] stamp(byte[] abPdf, String sigla, boolean rascunho, boolean copia, boolean cancelado,
			boolean semEfeito, boolean internoProduzido, String qrCode, String mensagem, Integer paginaInicial,
			Integer paginaFinal, Integer cOmitirNumeracao, String instancia, String orgaoUsu, String marcaDaguaDoModelo,
			List<Long> idsAssinantes, Rectangle size) throws DocumentException, IOException {
	  
		if (idsAssinantes != null && idsAssinantes.size() > 0 && Prop.getBool("assinatura.estampar"))
			abPdf = estamparAssinaturas(abPdf, idsAssinantes);

		PdfReader pdfIn1 = new PdfReader(abPdf);		
		Document doc = new Document(size, 0, 0, 0, 0);
		// final SimpleDateFormat sdf = new SimpleDateFormat(
		// "EEE MMM dd HH:mm:ss zzz yyyy");
		// doc.add(new Meta("creationdate", sdf.format(new Date(0L))));

			try (ByteArrayOutputStream boA4 = new ByteArrayOutputStream()) {
				/*-- Alterado de PdfWriter p/ PdfCopy(Essa classe permite manter os "stamps" originais do arquivo importado) 
				por Marcos(CMSP) em 21/02/19 --*/
				// PdfCopy writer = new PdfCopy(doc, boA4);
				/*-- Alerado de volta pois ficou desabilitado o redimensionamento do PDF de modo
				 *   a que os códigos de barra 2D e 3D não ficassem por cima do texto. Por Renato em 25/04/2019 --*/
				PdfWriter writer = PdfWriter.getInstance(doc, boA4);
				doc.open();
				PdfContentByte cb = writer.getDirectContent();
	
				// Resize every page to A4 size
				//
				// double thetaRotation = 0.0;
				for (int i = 1; i <= pdfIn1.getNumberOfPages(); i++) {		  				    				    
					int rot = pdfIn1.getPageRotation(i);
					float left = pdfIn1.getPageSize(i).getLeft();
					float bottom = pdfIn1.getPageSize(i).getBottom();
					float top = pdfIn1.getPageSize(i).getTop();
					float right = pdfIn1.getPageSize(i).getRight();
	
					PdfImportedPage page = writer.getImportedPage(pdfIn1, i);
					float w = page.getWidth();
					float h = page.getHeight();
	
					// Logger.getRootLogger().error("----- dimensoes: " + rot + ", " + w
					// + ", " + h);
	
					doc.setPageSize((rot != 0 && rot != 180) ^ (w > h) ? size.rotate() : size);
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
	
					// Logger.getRootLogger().error(
					// "----- dimensoes: " + rot + ", " + w + ", " + h);
					// Logger.getRootLogger().error("----- page: " + pw + ", " + ph);
	
					// cb.transform(AffineTransform.getTranslateInstance(
					// ((pw / scale) - w) / 2, ((ph / scale) - h) / 2));
	
					// put the page
					cb.addTemplate(page, 0, 0);
					//-- Adicionado devido ao PdfCopy - por Marcos(CMSP) em 21/02/19 --/
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
		return abPdf;
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
	
	private static void SetOnePageA0(PdfReader pdf, Rectangle tamanhoPagina, int numberPage, boolean internoProduzido) { //setar uma página pra A0 quando for estampada
		int rot = pdf.getPageRotation(numberPage);
		float left = pdf.getPageSize(numberPage).getLeft();
		float bottom = pdf.getPageSize(numberPage).getBottom();
		float top = pdf.getPageSize(numberPage).getTop();
		float right = pdf.getPageSize(numberPage).getRight();
			

		page = writer.getImportedPage(pdf, numberPage);
		float w = page.getWidth();
		float h = page.getHeight();
		writer.close();

		// Logger.getRootLogger().error("----- dimensoes: " + rot + ", " + w
		// + ", " + h);

		doc.setPageSize((rot != 0 && rot != 180) ^ (w > h) ? tamanhoPagina.rotate() : tamanhoPagina);
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
		doc.close();
	}
}
