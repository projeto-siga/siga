package br.gov.jfrj.siga.ex.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfToPlainText {
    public static String getText(byte[] pdf) throws Exception {
        try (PDDocument doc = PDDocument.load(pdf)) {
            return new PDFTextStripper().getText(doc);
        }

//        try (InputStream input = new ByteArrayInputStream(pdf)) {
//            StringBuilder sb = new StringBuilder();
//            PdfReader reader = new PdfReader(input);
//            PdfTextExtractor extractor = new PdfTextExtractor(reader);
//            int pages = reader.getNumberOfPages();
//            for (int i = 1; i <= pages; i++) {
//                try {
//                    sb.append(extractor.getTextFromPage(i));
//                } catch (ArrayIndexOutOfBoundsException ex) {
//                    // engolir a exceção quando não consegue localizar a página;
//                }
//                sb.append(" ");
//            }
//            String s = sb.toString();
//            if (s == null || s.trim().length() == 0)
//                return null;
//            return s.trim();
//        }
    }
}
