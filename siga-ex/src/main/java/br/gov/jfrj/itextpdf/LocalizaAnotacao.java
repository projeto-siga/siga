package br.gov.jfrj.itextpdf;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class LocalizaAnotacao {

	public static List<LocalizaAnotacaoResultado> localizar(PDDocument doc, List<String> seek) {
		List<LocalizaAnotacaoResultado> l = new ArrayList<>();
		int pageNum = 0;
		try {
			for (PDPage page : doc.getPages()) {
				pageNum++;
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				List<PDAnnotation> annotations = page.getAnnotations();
				// first setup text extraction regions
				for (int j = 0; j < annotations.size(); j++) {
					PDAnnotation annot = annotations.get(j);
					if (annot instanceof PDAnnotationLink) {
						PDAnnotationLink link = (PDAnnotationLink) annot;
						PDRectangle rect = link.getRectangle();
						// need to reposition link rectangle to match text space
						float x = rect.getLowerLeftX();
						float y = rect.getUpperRightY();
						float width = rect.getWidth();
						float height = rect.getHeight();
						int rotation = page.getRotation();
						if (rotation == 0) {
							PDRectangle pageSize = page.getMediaBox();
							y = pageSize.getHeight() - y;
						} else if (rotation == 90) {
							// do nothing
						}
						PDAction action = link.getAction();
						if (action instanceof PDActionURI && rect.getWidth() > 0f) {
							String uri = ((PDActionURI) action).getURI();
							for (String s : seek) {
								if (uri.contains(s)) {
									LocalizaAnotacaoResultado r = new LocalizaAnotacaoResultado();
									r.uri = uri;
									r.seek = s;
									r.page = pageNum;
									r.lowerLeftX = rect.getLowerLeftX();
									r.upperRightY = rect.getUpperRightY();
									r.width = rect.getWidth();
									r.height = rect.getHeight();
									r.rotation = page.getRotation();
									l.add(r);
								}
							}
							System.out.println("Page " + pageNum + ":" + uri);
						}

						// System.out.println("Page " + pageNum + ":'" + urlText.trim() + "'=" +
						// uri.getURI());
						Rectangle2D.Float awtRect = new Rectangle2D.Float(x, y, width, height);
						stripper.addRegion("" + j, awtRect);
					}
				}
			}
			return l;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static class LocalizaAnotacaoResultado implements Comparable<LocalizaAnotacaoResultado> {
		public int rotation;
		public float height;
		public float width;
		public String uri;
		String seek;
		int page;
		float lowerLeftX;
		float upperRightY;

		@Override
		public int compareTo(LocalizaAnotacaoResultado o) {
			return this.uri.compareTo(o.uri);
		}
	}

}
