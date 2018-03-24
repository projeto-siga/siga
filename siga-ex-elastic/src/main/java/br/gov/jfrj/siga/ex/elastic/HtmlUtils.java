package br.gov.jfrj.siga.ex.elastic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftConditionalCommentTagTypes;
import net.htmlparser.jericho.PHPTagTypes;
import net.htmlparser.jericho.Source;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

public class HtmlUtils {
	public static String getText(byte[] html) {
		if (html == null)
			return null;
		try (InputStream input = new ByteArrayInputStream(html)) {
			MicrosoftConditionalCommentTagTypes.register();
			PHPTagTypes.register();
			PHPTagTypes.PHP_SHORT.deregister();
			MasonTagTypes.register();
			Source source = new Source(input);
			return source.getTextExtractor().setIncludeAttributes(true)
					.toString();
		} catch (Exception e) {
			return "";
		}
	}

}
