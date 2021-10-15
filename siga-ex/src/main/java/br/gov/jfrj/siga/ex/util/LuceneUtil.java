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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.kxml2.io.KXmlParser;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xmlpull.v1.XmlPullParser;

public class LuceneUtil {

	public static final String obterTexto(byte[] conteudo, String tpConteudo)
			throws Exception {
		if (tpConteudo != null) {
			//if (tpConteudo.equals("application/pdf"))
			//	return obterTextoPDF(conteudo);
			if (tpConteudo.equals("text/html"))
				return obterTextoHTML(conteudo);
		}
		return "";

	}

	public static final String obterTextoHTML(byte[] conteudoHtml)
			throws Exception {

		KXmlParser parser = new KXmlParser();
		String sHtml = new String(conteudoHtml);

		Tidy tidy = new Tidy();
		tidy.setCharEncoding(Configuration.LATIN1);
		tidy.setRawOut(false);
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			tidy.parse(new ByteArrayInputStream(sHtml.getBytes("iso-8859-1")), os);
			os.flush();
			sHtml = new String(os.toByteArray(), "iso-8859-1");
		}

		//String canonHtml = (new ProcessadorHtml()).canonicalizarHtml(sHtml,
		//		true, true);
		// String canonHtml = URLDecoder.decode(sHtml, "iso-8859-1");

		parser.setInput(new StringReader(sHtml));
		while (!(parser.getEventType() == XmlPullParser.START_TAG
				&& parser.getName() != null && parser.getName().toUpperCase()
				.equals("BODY")) && parser.getEventType() != XmlPullParser.END_DOCUMENT) {
			parser.nextToken();
		}

		StringBuffer s2 = new StringBuffer();

		while (!(parser.getEventType() == XmlPullParser.END_TAG
				&& parser.getName() != null && parser.getName().toUpperCase()
				.equals("BODY")) && parser.getEventType() != XmlPullParser.END_DOCUMENT) {
			if (parser.getEventType() == XmlPullParser.TEXT
					&& parser.getText() != null)
				s2.append(parser.getText());
			parser.nextToken();
		}

		return new String(s2);
	}
}
