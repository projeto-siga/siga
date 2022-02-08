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
package br.gov.jfrj.siga.ex.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import br.gov.jfrj.siga.cp.util.CpProcessadorReferencias;

public class ProcessadorReferencias {
	XmlPullParser parser;

	XmlSerializer serializer;

	private Set<String> htIgnorar = new HashSet<String>();

	public ProcessadorReferencias() {
		parser = new KXmlParser();
		serializer = new KXmlSerializer();
	}

	public void ignorar(String s) {
		htIgnorar.add(s);
	}

	public void limparIgnorar() {
		htIgnorar.clear();
	}

	public String processarReferencias(String s) throws Exception {
		s = s.replace("<!-- INICIO PRIMEIRO CABECALHO",
				"<!-- INICIO PRIMEIRO CABECALHO -->");
		s = s.replace("FIM PRIMEIRO CABECALHO -->",
				"<!-- FIM PRIMEIRO CABECALHO -->");
		s = s.replace("<!-- INICIO CABECALHO", "<!-- INICIO CABECALHO -->");
		s = s.replace("FIM CABECALHO -->", "<!-- FIM CABECALHO -->");
		s = s.replace("<!-- INICIO PRIMEIRO RODAPE",
				"<!-- INICIO PRIMEIRO RODAPE -->");
		s = s.replace("FIM PRIMEIRO RODAPE -->", "<!-- FIM PRIMEIRO RODAPE -->");
		s = s.replace("<!-- INICIO RODAPE", "<!-- INICIO RODAPE -->");
		s = s.replace("FIM RODAPE -->", "<!-- FIM RODAPE -->");

		s = marcarReferencias(s);

		s = s.replace("<!-- INICIO PRIMEIRO CABECALHO -->",
				"<!-- INICIO PRIMEIRO CABECALHO");
		s = s.replace("<!-- FIM PRIMEIRO CABECALHO -->",
				"FIM PRIMEIRO CABECALHO -->");
		s = s.replace("<!-- INICIO CABECALHO -->", "<!-- INICIO CABECALHO");
		s = s.replace("<!-- FIM CABECALHO -->", "FIM CABECALHO -->");
		s = s.replace("<!-- INICIO PRIMEIRO RODAPE -->",
				"<!-- INICIO PRIMEIRO RODAPE");
		s = s.replace("<!-- FIM PRIMEIRO RODAPE -->", "FIM PRIMEIRO RODAPE -->");
		s = s.replace("<!-- INICIO RODAPE -->", "<!-- INICIO RODAPE");
		s = s.replace("<!-- FIM RODAPE -->", "FIM RODAPE -->");
		
		return s;
	}

	public void writeStartTag(final XmlPullParser parser,
			final XmlSerializer serializer) throws XmlPullParserException,
			IOException {
		final String sName = parser.getName();
		serializer.startTag(null, sName);
		for (int i = 0; i < parser.getAttributeCount(); i++) {
			serializer.attribute(parser.getAttributeNamespace(i),
					parser.getAttributeName(i), parser.getAttributeValue(i));
		}
	}

	public void writeToken(final XmlPullParser parser,
			final XmlSerializer serializer, ByteArrayOutputStream os)
			throws XmlPullParserException, IOException {
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

				s = CpProcessadorReferencias.marcarReferenciasParaDocumentos(s, htIgnorar);
				serializer.flush();
				os.write(s.getBytes("utf-8"));
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
			// throw new RuntimeException("unrecognized event: "
			// + parser.getEventType());
		}
	}

	public String marcarReferencias(final String sHtml) {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

		    final Document document = Jsoup.parse(sHtml);
		    document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);    
		    // document.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml);
		    document.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.extended);
		    String html = document.html();
		    
			parser.setInput(new StringReader(html));
			serializer.setOutput(os, "utf-8");

			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				writeToken(parser, serializer, os);
				parser.nextToken();
			}
			writeToken(parser, serializer, os);
			serializer.flush();
			final String s = new String(os.toByteArray(), "utf-8");
			return s;
		} catch (final Exception ex) {
			return sHtml;
		}
	}
}