package br.gov.jfrj.siga.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

public class FreemarkerIndent {
	static Pattern patternBody = Pattern.compile(
			"<body[^>]*>\n(\\s*+.*?)\\s*+</body>", Pattern.CASE_INSENSITIVE
					+ Pattern.DOTALL);

	static Pattern patternRemoveBodyIndent = Pattern.compile("^    (.*)$",
			Pattern.MULTILINE);

	static Pattern patternFMM = Pattern.compile(
			"\\{\\{fm\\}\\}(.*?)\\{\\{\\/fm\\}\\}", Pattern.DOTALL);

	static Pattern patternUnmarshallOpen = Pattern
			.compile("<div\\s+class=\"ftl\">\\s*<div\\s+data-ftl=\"([^\"]*)\">\\s*<span>0</span>\\s*</div>");

	static Pattern patternUnmarshallClose = Pattern
			.compile("\\s*<div\\s+data-ftl=\"([^\"]*)\">\\s*<span>0</span>\\s*</div>\\n(\\s*)<\\/div>");

	static Pattern patternUnmarshallSelfContained = Pattern
			.compile("<div\\s+data-ftl=\"([^\"]*)\">\\s*<span>[01]</span>\\s*</div>");

	public static String bodyOnly(String s) {
		Matcher m = patternBody.matcher(s);
		if (m.find()) {
			String body = m.group(1);

			Matcher matcher = patternRemoveBodyIndent.matcher(body);
			StringBuffer output = new StringBuffer();
			while (matcher.find()) {
				String rep = matcher.group(1);
				matcher.appendReplacement(output, rep.replace("$", "\\$"));
			}
			matcher.appendTail(output);
			return output.toString();
		}
		return null;
	}

	public static String convertFtl2Html(String input, List<String> lftl) {
		String fmm = new FreemarkerMarker(input).run();

		StringBuffer output = new StringBuffer();
		Matcher matcher = patternFMM.matcher(fmm);
		while (matcher.find()) {
			String ftl = matcher.group(1);
			lftl.add(ftl);
			System.out.println(ftl);
			boolean open = (ftl.startsWith("[#") || ftl.startsWith("[@"))
					&& !ftl.endsWith("/]");
			boolean close = ftl.startsWith("[/");
			boolean unindent = false;
			String rep = "";

			if (open) {
				if (ftl.startsWith("[#else") || ftl.startsWith("[#case")
						|| ftl.startsWith("[#default")) {
					open = false;
					unindent = true;
				}
				if (ftl.startsWith("[#break"))
					open = false;
				if (ftl.startsWith("[#--"))
					open = false;
			}

			if (open)
				rep += "<div class=\"ftl\">";

			rep += "<" + (open || close ? "div" : "div") + " data-ftl=\""
					+ lftl.size() + "\"><span>" + (unindent ? "1" : "0")
					+ "</span></div>";

			if (close)
				rep += "</div>";

			// System.out.println("rep: " + rep);
			matcher.appendReplacement(output, rep);
		}
		matcher.appendTail(output);
		return output.toString();
	}

	private static String unmarshalOpen(String input, List<String> lftl) {
		StringBuffer output = new StringBuffer();
		Matcher matcher = patternUnmarshallOpen.matcher(input);
		while (matcher.find()) {
			String ftl = matcher.group(1);
			String rep = lftl.get(Integer.valueOf(ftl) - 1);
			matcher.appendReplacement(output, rep.replace("$", "\\$"));
		}
		matcher.appendTail(output);
		return output.toString();
	}

	private static String unmarshalClose(String input, List<String> lftl) {
		StringBuffer output = new StringBuffer();
		Matcher matcher = patternUnmarshallClose.matcher(input);
		while (matcher.find()) {
			String ftl = matcher.group(1);
			String spc = matcher.group(2);
			String rep = "\n" + spc + lftl.get(Integer.valueOf(ftl) - 1);
			matcher.appendReplacement(output, rep.replace("$", "\\$"));
		}
		matcher.appendTail(output);
		return output.toString();
	}

	private static String unmarshalSelfContained(String input, List<String> lftl) {
		StringBuffer output = new StringBuffer();
		Matcher matcher = patternUnmarshallSelfContained.matcher(input);
		while (matcher.find()) {
			String ftl = matcher.group(1);
			String rep = (matcher.group().contains("<span>1</span>") ? "{{unindent}}"
					: "")
					+ lftl.get(Integer.valueOf(ftl) - 1);
			matcher.appendReplacement(output, rep.replace("$", "\\$"));
		}
		matcher.appendTail(output);
		
		String result = output.toString();
		result = result.replace("  {{unindent}}", "");
		return result;
	}

	public static String convertHtml2Ftl(String input, List<String> lftl) {
		String output = input;
		output = unmarshalOpen(output, lftl);
		output = unmarshalClose(output, lftl);
		output = unmarshalSelfContained(output, lftl);
		return output;
	}

	public static String indent(String s) throws IOException {
		List<String> lftl = new ArrayList<>();
		s = convertFtl2Html(s, lftl);

		final Tidy tidy = new Tidy();
		tidy.setXmlOut(true);
		tidy.setXmlPi(true);
		tidy.setXmlPIs(true);
		tidy.setXHTML(true);
		tidy.setFixComments(true);
		tidy.setFixBackslash(true);
		tidy.setTidyMark(false);
		tidy.setCharEncoding(Configuration.LATIN1);
		tidy.setRawOut(false);
		tidy.setIndentAttributes(false);
		tidy.setEncloseBlockText(true);
		tidy.setEncloseText(true);
		tidy.setMakeClean(true);
		tidy.setShowWarnings(false);
		tidy.setSmartIndent(false);
		tidy.setSpaces(2);
		tidy.setTidyMark(false);
		tidy.setIndentContent(true);
		tidy.setWraplen(0);

		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		tidy.parse(new ByteArrayInputStream(s.getBytes("iso-8859-1")), os);
		os.flush();
		String sResult = new String(os.toByteArray(), "iso-8859-1");
		sResult = bodyOnly(sResult);
		// if (true) return sResult;

		sResult = convertHtml2Ftl(sResult, lftl);
		return sResult;
	}

}
