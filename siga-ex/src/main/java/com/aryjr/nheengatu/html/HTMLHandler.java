/*
 * The Nheengatu Project : a free Java library for HTML  abstraction.
 *
 * Project Info:  http://www.aryjr.com/nheengatu/
 * Project Lead:  Ary Rodrigues Ferreira Junior
 *
 * (C) Copyright 2005, 2006 by Ary Junior
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.aryjr.nheengatu.html;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * A HTML parser that can create the Nhengatu objects.
 * 
 * @version $Id: HTMLHandler.java,v 1.1 2007/12/26 15:57:42 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class HTMLHandler extends DefaultHandler {
	private HTMLDocument html;

	private Stack stack;

	private String documentName;

	private String documentPath;

	public HTMLHandler() {
		this.documentName = null;
		this.documentPath = null;
		stack = new Stack();
	}

	public HTMLHandler(final String documentPath, final String documentName) {
		this.documentName = documentName;
		this.documentPath = documentPath;
		stack = new Stack();
	}

	@Override
	public void startDocument() {
		if (documentName != null) {
			html = new HTMLDocument(documentName);
			html.setPath(documentPath);
		} else {
			html = new HTMLDocument();
		}

		// html.breakLines(true);
		html.breakLines(false);
	}

	@Override
	public void endDocument() {
		try {
			// TODO when generate the HTML file
			if (documentName != null) {
				html.generateFile();
			}
			// TODO html = null;
			////System.gc();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startElement(final String uri, final String name,
			final String qName, final Attributes atts) {
		// Ignoring these tags created by TagSoup
		if (name.equals("html")) {
			return;
		} else if (name.equals("body")) {
			// The HTML Body is the top of Stack
			stack.push(html.getBody());
			return;
		} else if (name.equals("head")) {
			// The HTML Head is the top of Stack
			stack.push(html.getHead());
			return;
		} else if (stack.empty()) {
			// todos os elementos devem estar contidos dentro de tags
			// <body> ou <head>
			return; 
		} else {
			final Tag tag = new Tag(qName);
			if (qName.equalsIgnoreCase("table")) {
				tag.setPropertyValue("cellspacing", "1");
				tag.setPropertyValue("cellpadding", "1");
			}
			for (int inc = 0; inc < atts.getLength(); inc++) {
				if (!(atts.getQName(inc).equals("colspan") && atts
						.getValue(inc).equals("1"))
						&& !(atts.getQName(inc).equals("rowspan") && atts
								.getValue(inc).equals("1"))) {
					tag
							.setPropertyValue(atts.getQName(inc), atts
									.getValue(inc));
				}
			}
			stack.push(tag);
		}
	}

	@Override
	public void endElement(final String uri, final String name,
			final String qName) {
		if (name.equals("html") || name.equals("head") || name.equals("body"))
			return;
		else {
			final Tag tag = (Tag) stack.pop();
			((Tag) stack.peek()).addTag(tag);
		}
	}

	@Override
	public void characters(final char ch[], final int start, final int length) {
		String text = "";
		for (int i = start; i < start + length; i++) {
			switch (ch[i]) {
			case '\\':
				text += "\\\\";
				break;
			case '"':
				text += "\\\"";
				break;
			case '\n':
				// text += "\\n";
				text += " ";
				break;
			case '\r':
				// text += "\\r";
				text += " ";
				break;
			case '\t':
				// text += "\\t";
				text += " ";
				break;
			default:
				text += String.valueOf(ch[i]);
				break;
			}
		}
		text = text.replace("  ", " ");
		if (!text.trim().equals("")) {
			((Tag) stack.peek()).addText(text);
		}
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		((Tag) stack.peek()).addText(" ");
	}

	/**
	 * @return Returns the html.
	 */
	public HTMLDocument getHtml() {
		return html;
	}

}
/**
 * 
 * $Log: HTMLHandler.java,v $
 * Revision 1.1  2007/12/26 15:57:42  tah
 * *** empty log message ***
 *
 * Revision 1.6  2007/06/29 16:20:45  tah
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/24 18:02:38  tah
 * *** empty log message ***
 * Revision 1.4 2006/07/06 15:45:26 tah *** empty log
 * message ***
 * 
 * Revision 1.3 2006/07/05 16:00:51 nts Refatorando para melhorar qualidade do
 * código
 * 
 * Revision 1.2 2006/04/11 19:43:51 tah *** empty log message *** Revision 1.1
 * 2006/04/03 21:30:45 tah Utilizando o nheengatu Revision 1.3 2006/01/01
 * 13:45:33 aryjr Feliz 2006!!!
 * 
 * Revision 1.2 2005/12/16 14:06:32 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.1 2005/12/05 13:45:08 aryjr CSV file support.
 * 
 * Revision 1.4 2005/11/25 18:18:41 aryjr Page break OK!!!
 * 
 * Revision 1.3 2005/11/25 15:10:29 aryjr Head and foot on page break.
 * 
 * Revision 1.2 2005/11/24 14:17:54 aryjr Head and foot of PDF documents.
 * 
 * Revision 1.1 2005/11/14 12:17:43 aryjr Renomeando os pacotes.
 * 
 * Revision 1.1 2005/09/10 23:43:39 aryjr Passando para o java.net.
 * 
 * Revision 1.6 2005/07/02 01:18:57 aryjunior Site do projeto.
 * 
 * Revision 1.5 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.4 2005/06/03 23:09:22 aryjunior Generalizando o parser HTML.
 * 
 * Revision 1.3 2005/05/30 01:55:57 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:33 aryjunior Initial import.
 * 
 * 
 */
