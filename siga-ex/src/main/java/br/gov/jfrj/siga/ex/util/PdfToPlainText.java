package br.gov.jfrj.siga.ex.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

public class PdfToPlainText {
	public static String getText(byte[] pdf) throws Exception {
		try (InputStream input = new ByteArrayInputStream(pdf)) {
			StringBuilder sb = new StringBuilder();
			PdfReader reader = new PdfReader(input);
			PdfTextExtractor extractor = new PdfTextExtractor(reader);
			int pages = reader.getNumberOfPages();
			for (int i = 1; i <= pages; i++) {
				sb.append(extractor.getTextFromPage(i));
				sb.append(" ");
			}
			String s = sb.toString();
			if (s == null || s.trim().length() == 0)
				return null;
			return s.trim();
		}
	}
}
