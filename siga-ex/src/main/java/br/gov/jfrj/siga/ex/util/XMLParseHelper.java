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

import java.io.StringReader;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XMLParseHelper {

	private KXmlParser parser = new KXmlParser();

	public XMLParseHelper(KXmlParser parser) {
		setParser(parser);
	}

	public XMLParseHelper(String conteudo) {
		try {
			getParser().setInput(new StringReader(conteudo));
		} catch (Exception e) {
			// engolindo
		}
	}

	public KXmlParser getParser() {
		return parser;
	}

	public void setParser(KXmlParser parser) {
		this.parser = parser;
	}

	public boolean seekTag(String name) {
		try {
			while (!(parser.getEventType() == XmlPullParser.START_TAG
					&& parser.getName() != null && parser.getName()
					.equalsIgnoreCase(name))) {
				parser.nextToken();
				if (parser.getEventType() == XmlPullParser.END_DOCUMENT)
					return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getTextByTagName(String tagName) {
		try {
			if (!seekTag(tagName))
				return null;
			while (!(parser.getEventType() == XmlPullParser.END_TAG
					&& parser.getName() != null && parser.getName()
					.equalsIgnoreCase(tagName))
					&& !(parser.getEventType() == XmlPullParser.END_DOCUMENT)) {
				if (parser.getEventType() == XmlPullParser.TEXT)
					return parser.getText();
				parser.nextToken();
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/*public String getTextByTagChain(String path) {
		String[] patharr = path.split("/");
		try {
			int pos = 0;
			if (patharr.length<1)
				return null;
			if (!seekTag(patharr[0]))
				return null;
			while (!(parser.getEventType() == XmlPullParser.END_TAG
					&& parser.getName() != null && pos == patharr.length - 1 && parser
					.getName().equalsIgnoreCase(patharr[pos]))
					&& !(parser.getEventType() == XmlPullParser.END_DOCUMENT)) {
				
				if (parser.getEventType() == XmlPullParser.TEXT)
					return parser.getText();
				
				parser.nextToken();
				
				if (parser.getEventType() == XmlPullParser.START_TAG
						&& parser.getName() != null && pos < patharr.length - 1
						&& parser.getName().equalsIgnoreCase(patharr[pos+1]))
					pos++;
				
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}*/

}
