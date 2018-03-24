package br.gov.jfrj.siga.ex.elastic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

public class PdfUtils {
	public static String getText(byte[] pdf) {
		if (pdf == null)
			return null;
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
			return s;
		} catch (Exception e) {
			return "";
		}
	}

}
