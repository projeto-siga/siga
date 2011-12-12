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
package br.gov.jfrj.lucene;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.hibernate.search.bridge.StringBridge;
import org.kxml2.io.KXmlParser;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xmlpull.v1.XmlPullParser;

import br.gov.jfrj.siga.ex.util.ProcessadorHtml;

public class HtmlBridge implements StringBridge {

	public String objectToString(Object arg0) {
		try {
			byte[] conteudoHtml = (byte[]) arg0;
			KXmlParser parser = new KXmlParser();
			if (conteudoHtml == null || conteudoHtml.length == 0)
				return null;
			String sHtml = new String(conteudoHtml);

			//Tidy tidy = new Tidy();
			//tidy.setCharEncoding(Configuration.LATIN1);
			//tidy.setRawOut(false);
			//final ByteArrayOutputStream os = new ByteArrayOutputStream();
			//tidy.parse(new ByteArrayInputStream(sHtml.getBytes("iso-8859-1")),
			//		os);
			//os.flush();
			//sHtml = new String(os.toByteArray(), "iso-8859-1");
			
			sHtml = sHtml .replace("<!-- INICIO PRIMEIRO CABECALHO", "<!-- INICIO PRIMEIRO CABECALHO -->");
			sHtml  = sHtml .replace("FIM PRIMEIRO CABECALHO -->", "<!-- FIM PRIMEIRO CABECALHO -->");
			sHtml  = sHtml .replace("<!-- INICIO PRIMEIRO RODAPE", "<!-- INICIO PRIMEIRO RODAPE -->");
			sHtml  = sHtml .replace("FIM PRIMEIRO RODAPE -->", "<!-- FIM PRIMEIRO RODAPE-->");
			
			//String canonHtml = (new
			//ProcessadorHtml()).canonicalizarHtml(sHtml,
			//true, true);
			// String canonHtml = URLDecoder.decode(sHtml, "iso-8859-1");

			parser.setInput(new StringReader(sHtml));
			while (!(parser.getEventType() == XmlPullParser.START_TAG
					&& parser.getName() != null && parser.getName()
					.toUpperCase().equals("BODY"))
					&& parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				parser.nextToken();
			}

			StringBuffer s2 = new StringBuffer();

			while (!(parser.getEventType() == XmlPullParser.END_TAG
					&& parser.getName() != null && parser.getName()
					.toUpperCase().equals("BODY"))
					&& parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlPullParser.TEXT
						&& parser.getText() != null)
					s2.append(parser.getText());
				parser.nextToken();
			}

			return new String(s2);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
}
