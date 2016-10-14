package br.gov.jfrj.siga.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

public class FreemarkerIndent {
	static Pattern patternBody = Pattern.compile(
			"<body[^>]*>\\s*(.*?)\\s*</body>", Pattern.CASE_INSENSITIVE
					+ Pattern.DOTALL);

	static Pattern patternRemoveBodyIndent = Pattern.compile("^    (.*)$",
			Pattern.MULTILINE);

	static Pattern patternFMM = Pattern.compile(
			"\\{\\{fm\\}\\}(.*?)\\{\\{\\/fm\\}\\}", Pattern.DOTALL);

	static Pattern patternUnmarshall = Pattern
			.compile("<!--fm-\\w+=\"([^\"]*)\"-->");

	static Pattern patternAfterOpen = Pattern
			.compile(
					"^(\\s*)(<!--fm-(?:open|selfcontained|unindent)=\"\\d+\"-->)\\s*([^\\s].*?)$",
					Pattern.MULTILINE);

	static Pattern patternBeforeClose = Pattern
			.compile(
					"^(\\s*)([^\\s].*?)\\s*(<!--fm-(?:close|selfcontained|unindent)=\"\\d+\"-->.*)$",
					Pattern.MULTILINE);

	static Pattern patternSingleTag = Pattern
			.compile(
					"^([ ]*)([^ ].*?)[ ]*(<!--fm-(?:open|close|selfcontained|unindent)=\"\\d+\"-->)[ ]*(.*)$",
					Pattern.MULTILINE);

	static Pattern patternMultipleTags = Pattern
			.compile(
					"^([ ]*)(.*<!--fm-(?:open|close|selfcontained|unindent)=\"\\d+\"-->)(.*<!--fm-(?:open|close|selfcontained|unindent)=\"\\d+\"-->.*)$",
					Pattern.MULTILINE);

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
						|| ftl.startsWith("[#default")
						|| ftl.startsWith("[#recover")) {
					open = false;
					unindent = true;
				}
				if (ftl.startsWith("[#break"))
					open = false;
				if (ftl.startsWith("[#--"))
					open = false;
			}

			rep += "<!--fm-";
			if (unindent)
				rep += "unindent";
			else if (!open && !close)
				rep += "selfcontained";
			else if (open)
				rep += "open";
			else if (close)
				rep += "close";

			rep += "=\"" + lftl.size() + "\"";

			rep += "-->";

			// System.out.println("rep: " + rep);
			matcher.appendReplacement(output, rep);
		}
		matcher.appendTail(output);
		return output.toString();
	}

	private static String unmarshal(String input, List<String> lftl) {
		StringBuffer output = new StringBuffer();
		Matcher matcher = patternUnmarshall.matcher(input);
		while (matcher.find()) {
			String ftl = matcher.group(1);
			String rep = lftl.get(Integer.valueOf(ftl) - 1);
			matcher.appendReplacement(output, rep.replace("$", "\\$"));
		}
		matcher.appendTail(output);
		return output.toString();
	}

	public static String convertHtml2Ftl(String input, List<String> lftl) {
		String output = input;
		output = unmarshal(output, lftl);
		return output;
	}

	private static String multipleTags(String input) {
		StringBuffer output = new StringBuffer();
		Matcher matcher = patternMultipleTags.matcher(input);
		while (matcher.find()) {
			String spc = matcher.group(1);
			String open = matcher.group(2);
			String after = matcher.group(3);
			String rep = spc + open + "\n" + spc + after;
			matcher.appendReplacement(output, rep.replace("$", "\\$"));
		}
		matcher.appendTail(output);
		return output.toString();
	}

	private static String afterOpen(String input) {
		StringBuffer output = new StringBuffer();
		Matcher matcher = patternAfterOpen.matcher(input);
		while (matcher.find()) {
			String spc = matcher.group(1);
			String open = matcher.group(2);
			String after = matcher.group(3);
			String rep = spc + open + "\n" + spc + after;
			matcher.appendReplacement(output, rep.replace("$", "\\$"));
		}
		matcher.appendTail(output);
		return output.toString();
	}

	private static String beforeClose(String input) {
		StringBuffer output = new StringBuffer();
		Matcher matcher = patternBeforeClose.matcher(input);
		while (matcher.find()) {
			String spc = matcher.group(1);
			String before = matcher.group(2);
			String close = matcher.group(3);
			String rep = spc + before + "\n" + spc + close;
			matcher.appendReplacement(output, rep.replace("$", "\\$"));
		}
		matcher.appendTail(output);
		return output.toString();
	}

	private static String singleTag(String input) {
		StringBuffer output = new StringBuffer();
		Matcher matcher = patternSingleTag.matcher(input);
		while (matcher.find()) {
			String spc = matcher.group(1);
			String before = matcher.group(2);
			String tag = matcher.group(3);
			String after = matcher.group(4);
			String rep = "";
			if (before.trim().length() > 0)
				rep += spc + before + "\n";
			rep += spc + tag;
			if (after.trim().length() > 0)
				rep += "\n" + spc + after;
			matcher.appendReplacement(output, rep.replace("$", "\\$"));
		}
		matcher.appendTail(output);
		return output.toString();
	}

	private static String removeOddSpace(String input) {
		String lines[] = input.split("\n");
		StringBuffer sb = new StringBuffer();

		for (String s : lines) {
			if (sb.length() > 0)
				sb.append("\n");
			int len = 0;
			for (; len < s.length() && s.charAt(len) == ' '; len++)
				;
			if (len % 2 == 1)
				sb.append(s, 1, s.length());
			else
				sb.append(s);
		}
		return sb.toString();
	}

	private static String indentBasedOnFreemarkerTags(String input) {
		String lines[] = input.split("\n");
		StringBuffer sb = new StringBuffer();

		int indent = 0;
		boolean unindent = false;
		for (String s : lines) {
			if (s.trim().startsWith("<!--fm-unindent"))
				unindent = true;
			if (s.trim().startsWith("<!--fm-close"))
				indent--;
			if (sb.length() > 0)
				sb.append("\n");
			for (int i = 0; i < (indent - (unindent ? 1 : 0)); i++)
				sb.append("  ");
			unindent = false;
			sb.append(s);
			if (s.trim().startsWith("<!--fm-open"))
				indent++;
		}
		return sb.toString();
	}

	protected static String tidy(String s) throws UnsupportedEncodingException,
			IOException {
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
		tidy.setEncloseBlockText(false);
		tidy.setEncloseText(false);
		tidy.setMakeClean(true);
		tidy.setShowWarnings(false);
		tidy.setSmartIndent(false);
		tidy.setSpaces(2);
		tidy.setTabsize(2);
		tidy.setTidyMark(false);
		tidy.setIndentContent(true);
		tidy.setWraplen(0);
		tidy.setWrapSection(false);

		try (ByteArrayOutputStream os = new ByteArrayOutputStream();
				ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes("iso-8859-1"))) {
			tidy.parse(bais, os);
			os.flush();
			String sResult = new String(os.toByteArray(), "iso-8859-1");
			return sResult;
		}
	}

	public static String indent(String s) throws IOException {
		List<String> lftl = new ArrayList<>();
		s = convertFtl2Html(s, lftl);

		s = "<body>" + s + "</body>";

		String sResult = tidy(s);
		sResult = bodyOnly(sResult);
		// if (true) return sResult;
		while (true) {
			String sLastResult = multipleTags(sResult);
			if (sResult.equals(sLastResult))
				break;
			else
				sResult = sLastResult;
		}

		sResult = singleTag(sResult);
		sResult = removeOddSpace(sResult);
		// sResult = afterOpen(sResult);
		// sResult = beforeClose(sResult);
		sResult = indentBasedOnFreemarkerTags(sResult);

		sResult = convertHtml2Ftl(sResult, lftl);
		return sResult;
	}

}
