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
package br.gov.jfrj.siga.gc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.w3c.tidy.Tidy;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class ProcessadorHtml {
	XmlPullParser parser;

	XmlSerializer serializer;

	HashSet<String> setTrimElements;

	HashMap<String, Tag> mapKnownElements;

	Boolean fTrimLeft;

	static final String asTrimElements[] = { "html", "title", "head", "body",
			"div", "hr", "table", "caption", "colgroup", "col", "th", "tr",
			"td", "ol", "ul", "li", "p", "br", "hr", "blockquote" };

	static final String asKnownElements[] = { "html", "title", "head", "body",
			"div", "hr", "table", "caption", "colgroup", "col", "th", "tr",
			"td", "ol", "ul", "li", "p", "br", "hr", "blockquote" };

	private class Tag {
		String name;

		HashMap<String, HashSet<String>> attrs = null;

		HashMap<String, HashSet<String>> styles = null;

		public Tag(String name, String attrs, String styles, boolean hasContent) {
			String s;
			this.name = name;
			if (attrs != null) {
				this.attrs = new HashMap<String, HashSet<String>>();
				for (String attrStr : attrs.split(";")) {
					String attrSplit[] = attrStr.split("=");
					HashSet<String> vals = null;
					if (attrSplit.length > 1) {
						vals = new HashSet<String>();
						s = "";
						for (String attrVal : attrSplit[1].split(",")) {
							if (attrVal.endsWith("\\")) {
								s += attrVal.substring(0, attrVal.length() - 1);
								continue;
							}
							vals.add(s + attrVal);
							s = "";
						}
					}
					this.attrs.put(attrSplit[0], vals);
				}
			}
			if (styles != null) {
				this.styles = new HashMap<String, HashSet<String>>();
				for (String styleStr : styles.split(";")) {
					String styleSplit[] = styleStr.split("=");
					HashSet<String> vals = null;
					if (styleSplit.length > 1) {
						vals = new HashSet<String>();
						s = "";
						for (String styleVal : styleSplit[1].split(",")) {
							if (styleVal.endsWith("\\")) {
								s += styleVal.substring(0,
										styleVal.length() - 1);
								continue;
							}
							vals.add(s + styleVal);
							s = "";
						}
					}
					this.styles.put(styleSplit[0], vals);
				}
			}
		}

	}

	static HashMap<String, Tag> tags = null;

	private void add(HashMap<String, Tag> tags, String name, String attrs,
			String styles, boolean hasContent) {
		tags.put(name, new Tag(name, attrs, styles, hasContent));
	}

	public ProcessadorHtml() {
		parser = new KXmlParser();
		serializer = new KXmlSerializer();

		setTrimElements = new HashSet<String>();
		for (final String s : ProcessadorHtml.asTrimElements)
			setTrimElements.add(s.intern());

		tags = null;
		if (tags == null) {
			HashMap<String, Tag> myTags = new HashMap<String, Tag>();
			add(myTags, "html", "lang=en;xml:lang=en;xmlns", null, true);
			add(myTags, "head", null, null, true);
			add(myTags, "meta", null, null, false);
			add(myTags, "style", "type=text/css", null, true);
			add(myTags, "body", null, null, true);

			add(myTags, "b", null, null, true);
			add(myTags, "strong", null, null, true);
			add(myTags, "i", null, null, true);
			add(myTags, "em", null, null, true);

			add(myTags, "blockquote", null, null, true);
			add(myTags, "br", null, null, false);

			String styleP = "font-family=arial,avantgarde bk bt\\, arial;font-size=6pt,7pt,8pt,9pt,10pt,11pt,12pt,13pt,14pt;font-weight=bold"
					+ ";margin-left;text-decoration=italic;text-align=left,right,center,justify"
					+ ";text-indent;text-decoration;font-size-no-fix=yes;float=none;clear=both";
			add(myTags, "div", "align;class", styleP + ";page-break-after;",
					true);
			add(myTags, "span", "align;class", styleP, true);

			add(myTags, "h1", null, null, true);
			add(myTags, "h2", null, null, true);
			add(myTags, "h3", null, null, true);
			add(myTags, "h4", null, null, true);
			add(myTags, "h5", null, null, true);
			add(myTags, "h6", null, null, true);
			add(myTags, "p", "align=left,right,center,justify;class", styleP,
					true);
			add(myTags, "ol", "class", "list-style-type=roman", true);
			add(myTags, "ul", "class", null, true);
			add(myTags, "li", "class", null, true);

			add(myTags, "hr", "size;color", "color", false);

			final String sWidth = "width="
					+ "1%,2%,3%,4%,5%,6%,7%,8%,9%,10%,11%,12%,13%,14%,15%,16%,17%,18%,19%,20%"
					+ ",21%,22%,23%,24%,25%,26%,27%,28%,29%,30%,31%,32%,33%,34%,35%,36%,37%,38%,39%,40%"
					+ ",41%,42%,43%,44%,45%,46%,47%,48%,49%,50%,51%,52%,53%,54%,55%,56%,57%,58%,59%,60%"
					+ ",61%,62%,63%,64%,65%,66%,67%,68%,69%,70%,71%,72%,73%,74%,75%,76%,77%,78%,79%,80%"
					+ ",81%,82%,83%,84%,85%,86%,87%,88%,89%,90%,91%,92%,93%,94%,95%,96%,97%,98%,99%,100%";

			String styleT = "border-style=solid;border-color;border-width;border-collapse=collapse;float=none;clear=both";

			add(myTags,
					"table",
					"align=left,right,center,justify;bgcolor;bordercolor;border=0,1;cellpadding;cellspacing;heigth;summary;class;width;"
							+ sWidth, styleT, true);
			add(myTags, "caption", null, null, true);
			add(myTags, "colgroup", null, null, true);
			add(myTags, "col", "width", null, true);
			add(myTags, "tbody", null, null, true);
			add(myTags,
					"tr",
					"class;bgcolor;align=left,right,center,justify;valign=bottom,top,middle;",
					null, true);
			add(myTags,
					"th",
					"width;class;bgcoloralign=left,right,center,justify;valign=bottom,top,middle;id;",
					null, true);
			add(myTags,
					"td",
					"width;class;align=left,right,center,justify;valign=bottom,top,middle;bgcolor;headers;colspan;rowspan;"
							+ sWidth, styleP, true);
			add(myTags, "thead", null, null, true);
			add(myTags, "tfoot", null, null, true);

			add(myTags, "img", "src;width;height", null, true);

			tags = myTags;
		}

	}

	public String canonicalizarHtml(String s, boolean fRemoverEspacos,
			boolean fRemoverTagsDesconhecidos, boolean fIso8859,
			boolean fBodyOnly, boolean desprezarComentarios) throws Exception {
		// System.out.println(System.currentTimeMillis()
		// + " - INI canonicalizarHtml");

		final Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setSmartIndent(true);
		tidy.setIndentContent(true);
		tidy.setTidyMark(false);
		// tidy.setOutputEncoding("ISO-8859-1");
		// tidy.setPrintBodyOnly(true);
		tidy.setRawOut(true);
		tidy.setIndentAttributes(false);
		tidy.setWord2000(true);

		// tidy.setXmlOut(true);
		// tidy.setXmlPi(true);
		// tidy.setXmlPIs(true);
		// tidy.setXHTML(true);
		// tidy.setFixComments(true);
		// tidy.setFixBackslash(true);
		// tidy.setTidyMark(false);
		// tidy.setEncloseBlockText(true);
		// tidy.setEncloseText(true);
		// tidy.setMakeClean(true);
		// tidy.setShowWarnings(false);
		// tidy.setSmartIndent(true);
		// tidy.setSpaces(0);
		// tidy.setTidyMark(false);
		// tidy.setIndentContent(!fRemoverEspacos);

		// s = s.replace("\r\n", "*newline*");
		// s = s.replace("\n", "*newline*");
		// s = s.replace("*newline*", "\r\n");
		//
		// s = s.replace("\r\n", " ");

		// Resolve o problema do namespace colado do word. Outra opcao seria
		// alterar o jtidy conforme abaixo:
		// --- Lexer.java Fri Nov 03 18:09:12 2000
		// +++ \temp\Lexer.java Fri Dec 15 17:23:31 2000
		// @@ -695,7 +695,7 @@
		// if (attr != null)
		// {
		// - if (attr.value.equals(profile))
		// + if (!attr.value.equals(profile))
		// {
		// Report.warning(this, node, null, Report.INCONSISTENT_NAMESPACE);
		// attr.value = new String(profile);
		s = s.replace("<o:", "<");
		s = s.replace("</o:", "</");

		// final HtmlCleaner htmlc = new HtmlCleaner(s);
		// htmlc.setOmitDeprecatedTags(true);
		// htmlc.setOmitUnknownTags(true);
		// htmlc.setRecognizeUnicodeChars(fIso8859);
		// htmlc.setTranslateSpecialEntities(fIso8859);
		// htmlc.setUseCdataForScriptAndStyle(false);
		//
		// htmlc.clean();
		// if (fRemoverEspacos)
		// s = htmlc.getCompactXmlAsString();
		// else
		// s = htmlc.getPrettyXmlAsString();

		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			tidy.pprint(tidy.parseDOM(
				new ByteArrayInputStream(s.getBytes("iso-8859-1")), null), os);
			// tidy.parse(new ByteArrayInputStream(s.getBytes("iso-8859-1")), os);
			os.flush();
			s = new String(os.toByteArray(), "iso-8859-1");
		}

		// if (fRemoverTagsDesconhecidos) {
		// s = removerTagsDesconhecidosHtml(s);
		// // Depois precisamos rodar o tidy novamente para recuperar a
		// // formatacao
		// final ByteArrayOutputStream os2 = new ByteArrayOutputStream();
		// tidy.parse(new ByteArrayInputStream(s.getBytes("iso-8859-1")), os2);
		// os2.flush();
		// s = new String(os2.toByteArray(), "iso-8859-1");
		// }
		//
		// if (fRemoverEspacos)
		// s = removerEspacosHtml(s);
		//
		// s = s.replace("\r\n", "*newline*");
		// s = s.replace("\n", "*newline*");
		// s = s.replace("*newline*", "\r\n");

		// s = s.replace(
		// "<head>\r\n<meta name=\"generator\" content=\"HTML Tidy, see www.w3.org\" />"
		// + "\r\n<title></title>\r\n</head>\r\n", "");
		// s = s.replace(
		// "<head>\r\n<meta content=\"HTML Tidy, see www.w3.org\" name=\"generator\" />"
		// + "\r\n<title></title>\r\n</head>\r\n", "");

		// s = s.replace("<head>", "");
		// s = s.replace("</head>", "");
		// s = s.replace(
		// "<meta name=\"generator\" content=\"HTML Tidy, see www.w3.org\" />",
		// "");
		// s = s.replace(
		// "<meta content=\"HTML Tidy, see www.w3.org\" name=\"generator\" />",
		// "");
		// s = s.replace("<title></title>", "");

		// s = s.replace("\r\n", " \r\n");
		// s = s.replace("  ", " ");
		//
		if (fBodyOnly) {
			String sBody = bodyOnly(s);
			if (sBody != null)
				s = sBody;
		}
		//
		// System.out.println(System.currentTimeMillis()
		// + " - FIM canonicalizarHtml");
		System.out.println(s);
		return s;
	}

	public static String bodyOnly(String s) {
		Pattern p = Pattern.compile("<body[^>]*>\\r*\\n*(.*?)\\r*\\n*</body>",
				Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		Matcher m = p.matcher(s);
		if (m.find()) {
			// System.out.println(System.currentTimeMillis()
			// + " - FIM canonicalizarHtml");
			String found = m.group(1);
			StringBuilder sb = new StringBuilder();
			for (String ss : found.split("\\n")) {
				if (sb.length() > 0)
					sb.append("\n");
				if (ss.startsWith("    "))
					sb.append(ss.substring(4));
				else
					sb.append(ss);
			}
			return sb.toString();
		}
		return null;
	}

	public void writeStartTag(final XmlPullParser parser,
			final XmlSerializer serializer) throws XmlPullParserException,
			IOException {
		// check forcase when feature xml roundtrip is supported
		// if (parser.getFeature (FEATURE_XML_ROUNDTRIP)) {
		// TODO: how to do pass through string with actual start tag in
		// getText()
		// return;
		// }

		// if
		// (!parser.getFeature(XmlPullParser.FEATURE_REPORT_NAMESPACE_ATTRIBUTES))
		// {
		// for (int i = parser.getNamespaceCount(parser.getDepth() - 1); i <
		// parser.getNamespaceCount(parser
		// .getDepth()) - 1; i++) {
		// serializer.setPrefix(parser.getNamespacePrefix(i),
		// parser.getNamespaceUri(i));
		// }
		// }
		// serializer.startTag(parser.getNamespace(), parser.getName());
		// serializer.startTag(parser.getNamespace().equals("n0") ? null :
		// parser.getNamespace(), parser.getName());
		final String sName = parser.getName();
		if (setTrimElements.contains(sName.intern())) {
			fTrimLeft = true;
		}

		serializer.startTag(null, sName);

		for (int i = 0; i < parser.getAttributeCount(); i++) {
			serializer.attribute(parser.getAttributeNamespace(i),
					parser.getAttributeName(i), parser.getAttributeValue(i));
		}
	}

	public void writeToken(final XmlPullParser parser,
			final XmlSerializer serializer) throws XmlPullParserException,
			IOException {
		switch (parser.getEventType()) {
		case XmlPullParser.START_DOCUMENT:
			serializer.startDocument(null, null);
			break;

		case XmlPullParser.END_DOCUMENT:
			serializer.endDocument();
			break;

		case XmlPullParser.START_TAG:
			writeStartTag(parser, serializer);
			break;

		case XmlPullParser.END_TAG:
			// serializer.endTag(parser.getNamespace(), parser.getName());
			final String sEndTag = parser.getName();
			if (setTrimElements.contains(sEndTag.intern())) {
				fTrimLeft = true;
			}
			serializer.endTag(null, parser.getName());
			break;

		case XmlPullParser.IGNORABLE_WHITESPACE:
			// comment it to remove ignorable whtespaces from XML
			// infoset
			// serializer.ignorableWhitespace(parser.getText().replace(" ",
			// "@"));
			break;

		case XmlPullParser.TEXT:
			if (parser.getText() == null)
				System.err.println("null text error at: "
						+ parser.getPositionDescription());
			else {
				String s = parser.getText();
				s = s.replace("\r\n", "*newline*");
				s = s.replace("\n", "*newline*");
				s = s.replace("*newline*", " ");
				while (-1 != s.indexOf("  "))
					s = s.replace("  ", " ");
				if (fTrimLeft) {
					while (s.startsWith(" "))
						s = s.substring(1);
					if ("".equals(s))
						break;
					else
						fTrimLeft = false;
				}
				if (s.endsWith(" "))
					fTrimLeft = true;
				serializer.text(s);
			}
			break;

		case XmlPullParser.ENTITY_REF:
			if (parser.getText() != null)
				serializer.text(parser.getText());
			else
				serializer.entityRef(parser.getName());
			break;

		case XmlPullParser.CDSECT:
			serializer.cdsect(parser.getText());
			break;

		case XmlPullParser.PROCESSING_INSTRUCTION:
			serializer.processingInstruction(parser.getText());
			break;

		case XmlPullParser.COMMENT:
			serializer.comment(parser.getText());
			break;

		case XmlPullParser.DOCDECL:
			serializer.docdecl(parser.getText());
			break;

		default:
			throw new RuntimeException("unrecognized event: "
					+ parser.getEventType());
		}
	}

	public String removerEspacosHtml(final String sHtml) throws Exception {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			fTrimLeft = true;

			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

			parser.setInput(new StringReader(sHtml));
			// serializer.setOutput(os);
			serializer.setOutput(os, "utf-8");

			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				writeToken(parser, serializer);
				serializer.flush();
				parser.nextToken();
			}
			writeToken(parser, serializer);
			serializer.flush();
			final String s = new String(os.toByteArray(), "utf-8");
			return s;
		} catch (final Exception ex) {
			throw new Exception(ex);
		}
	}

	public void writeTagsConhecidos(final XmlPullParser parser,
			final XmlSerializer serializer) throws XmlPullParserException,
			IOException {
		switch (parser.getEventType()) {
		case XmlPullParser.START_DOCUMENT:
			serializer.startDocument(null, null);
			break;

		case XmlPullParser.END_DOCUMENT:
			serializer.endDocument();
			break;

		case XmlPullParser.START_TAG:
			final String sName = parser.getName();

			if (!tags.containsKey(sName.intern()))
				break;

			Tag t = tags.get(sName);

			serializer.startTag(null, sName);

			for (int i = 0; i < parser.getAttributeCount(); i++) {
				String attrName = parser.getAttributeName(i).intern();
				String val = parser.getAttributeValue(i).intern();
				if (attrName != "style") {
					if (t.attrs == null
							|| !t.attrs.containsKey(attrName.intern()))
						continue;
					HashSet<String> vals = t.attrs.get(attrName);
					if (vals == null || vals.contains(val)
							|| (val == "style" && t.styles != null)) {
						serializer.attribute(parser.getAttributeNamespace(i),
								parser.getAttributeName(i),
								parser.getAttributeValue(i));
					}
				} else {
					StringBuilder sb = null;
					if (t.styles == null)
						continue;
					for (String style : val.split(";")) {
						String items[] = style.split(":");
						String styleName = items[0].trim().toLowerCase()
								.intern();
						if (!t.styles.containsKey(styleName))
							continue;
						if (items.length < 2)
							continue;
						String styleVal = items[1].trim().toLowerCase()
								.intern();
						HashSet<String> vals = t.styles.get(styleName);
						if (vals == null || vals.contains(styleVal)) {
							if (sb == null)
								sb = new StringBuilder();
							sb.append(styleName.toLowerCase());
							sb.append(":");
							sb.append(styleVal);
							sb.append(";");
						}
					}
					if (sb != null) {
						serializer.attribute(parser.getAttributeNamespace(i),
								"style", sb.toString());
					}
				}
			}
			break;

		case XmlPullParser.END_TAG:
			if (tags.containsKey(parser.getName()))
				serializer.endTag(null, parser.getName());
			break;

		case XmlPullParser.IGNORABLE_WHITESPACE:
			// comment it to remove ignorable whtespaces from XML
			// infoset
			// serializer.ignorableWhitespace(parser.getText().replace(" ",
			// "@"));
			break;

		case XmlPullParser.TEXT:
			if (parser.getText() == null)
				System.err.println("null text error at: "
						+ parser.getPositionDescription());
			else {
				serializer.text(parser.getText());
			}
			break;

		case XmlPullParser.ENTITY_REF:
			if (parser.getText() != null)
				serializer.text(parser.getText());
			else
				serializer.entityRef(parser.getName());
			break;

		case XmlPullParser.CDSECT:
			serializer.cdsect(parser.getText());
			break;

		case XmlPullParser.PROCESSING_INSTRUCTION:
			serializer.processingInstruction(parser.getText());
			break;

		case XmlPullParser.COMMENT:
			serializer.comment(parser.getText());
			break;

		case XmlPullParser.DOCDECL:
			serializer.docdecl(parser.getText());
			break;

		default:
			throw new RuntimeException("unrecognized event: "
					+ parser.getEventType());
		}
	}

	public String removerTagsDesconhecidosHtml(final String sHtml)
			throws Exception {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

			parser.setInput(new StringReader(sHtml));
			// serializer.setOutput(os);
			serializer.setOutput(os, "utf-8");

			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				writeTagsConhecidos(parser, serializer);
				parser.nextToken();
			}
			writeTagsConhecidos(parser, serializer);
			serializer.flush();
			final String s = new String(os.toByteArray(), "utf-8");
			return s;
		} catch (final Exception ex) {
			throw new Exception(ex);
		}
	}

}