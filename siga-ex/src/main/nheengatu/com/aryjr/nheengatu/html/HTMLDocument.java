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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;

import com.aryjr.nheengatu.document.Document;

/**
 * 
 * A HTML Document.<br>
 * <br>
 * See more at: <a
 * href="http://www.w3.org/TR/1998/REC-html40-19980424/struct/global.html"
 * target="htmlspec">http://www.w3.org/TR/1998/REC-html40-19980424/struct/global.html</a>
 * 
 * @version $Id: HTMLDocument.java,v 1.1 2007/12/26 15:57:42 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class HTMLDocument extends Document {
	private Tag head;

	private Tag body;

	private final StringBuffer strAux = new StringBuffer("");

	private boolean htmlTag = true;

	private boolean headTag = true;

	private boolean bodyTag = true;

	private String breakLine = "\n";

	public HTMLDocument() {
		setName(getName() + ".html");
	}

	/**
	 * 
	 * Constructs a HTML document with a name.
	 * 
	 * @param name
	 *            The document's name.
	 */
	public HTMLDocument(final String name) {
		setName(name + ".html");
	}

	/**
	 * 
	 * It determines If the HTML document generated will have break lines. The
	 * default value is true.
	 * 
	 * @param value
	 */
	public void breakLines(final boolean value) {
		breakLine = value ? "\n" : "";
	}

	/**
	 * 
	 * It determines If the HTML document generated will have the <html> tag.
	 * The default value is true.
	 * 
	 * @param value
	 */
	public void generateHtmlTag(final boolean value) {
		htmlTag = value;
	}

	/**
	 * 
	 * It determines If the HTML document generated will have the <head> tag.
	 * The default value is true.
	 * 
	 * @param value
	 */
	public void generateHeadTag(final boolean value) {
		headTag = value;
	}

	/**
	 * 
	 * It determines If the HTML document generated will have the <body> tag.
	 * The default value is true.
	 * 
	 * @param value
	 */
	public void generateBodyTag(final boolean value) {
		bodyTag = value;
	}

	/**
	 * 
	 * Sets the document's head.
	 * 
	 * @param head
	 *            The Head object.
	 */
	public void setHead(final Tag head) {
		this.head = head;
	}

	/**
	 * 
	 * Sets the document's body.
	 * 
	 * @param body
	 *            The Body object.
	 */
	public void setBody(final Tag body) {
		this.body = body;
	}

	/**
	 * 
	 * Returns the document's head.
	 * 
	 */
	public Tag getHead() {
		if (head == null) {
			head = new Tag("head", "New HTML Document");
		}
		return this.head;
	}

	/**
	 * 
	 * Returns the document's body.
	 * 
	 */
	public Tag getBody() {
		if (body == null) {
			body = new Tag("body");
		}
		return this.body;
	}

	/**
	 * 
	 * Begins the generate.
	 * 
	 */
	public void generateFile() throws IOException {
		try (PrintWriter file = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getPath() + getName()), "utf-8"))) {
			final StringBuffer strHTML = new StringBuffer("");
			if (htmlTag) {
				strHTML.append("<html>" + breakLine);
			}
			if (headTag) {
				strHTML.append("<head>" + breakLine);
				// TODO metatags!!!
				/*
				 * Iterator metatags = head.tags(); while(metatags.hasNext()) { name =
				 * (String)metatags.next(); strHTML.append("<meta name=\""+name+"\"
				 * content=\""+head.getMetaTagValue(name)+"\">"+breakLine); }
				 */
				strHTML.append("<title></title>" + breakLine);
				strHTML.append("</head>" + breakLine);
			}
			if (bodyTag) {
				String name = null;
				strAux.append("<body ");
				final Iterator properties = body.getPropertiesNames();
				name = null;
				while (properties.hasNext()) {
					name = (String) properties.next();
					if (body.getPropertyValue(name).equals("")) {
						strAux.append(name + " ");
					} else {
						strAux.append(name + "=\"" + body.getPropertyValue(name) + "\" ");
					}
				}
				strHTML.append(strAux + ">" + breakLine);
				strAux.setLength(0);
			}
			generateBody(body.tags(), strHTML);
			if (bodyTag) {
				strHTML.append("</body>" + breakLine);
			}
			if (htmlTag) {
				strHTML.append("</html>" + breakLine);
			}
			file.println(strHTML.toString());
		}
	}

	private void generateBody(final Iterator tags, final StringBuffer strHTML) {
		Object tag = null;
		Iterator properties = null;
		while (tags.hasNext()) {
			tag = tags.next();
			if (tag instanceof Text) {
				strHTML.append(((Text) tag).getText());
			} else {
				strAux.append("<" + ((Tag) tag).getName() + " ");
				properties = ((Tag) tag).getPropertiesNames();
				String name = null;
				while (properties.hasNext()) {
					name = (String) properties.next();
					if (((Tag) tag).getPropertyValue(name).equals("")) {
						strAux.append(name + " ");
					} else {
						strAux.append(name + "=\"" + ((Tag) tag).getPropertyValue(name) + "\" ");
					}
				}
				strHTML.append(strAux + ">" + breakLine);
				strAux.setLength(0);
				generateBody(((Tag) tag).tags(), strHTML);
				if (!((Tag) tag).getName().equals("img") && !((Tag) tag).getName().equals("br")) {
					strHTML.append("</" + ((Tag) tag).getName() + ">" + breakLine);
				}
			}
		}
	}

	public static void writeHTML(final String fileName, final String filePath, final Tag tag) {
		final HTMLDocument html = new HTMLDocument();
		html.getBody().addTag(tag);
		html.setName(fileName);
		html.setPath(filePath);
		try {
			html.generateFile();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
/**
 * 
 * $Log: HTMLDocument.java,v $
 * Revision 1.1  2007/12/26 15:57:42  tah
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/05 16:00:50  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.3  2006/04/11 19:43:51  tah
 * *** empty log message ***
 * Revision 1.2 2006/04/07 22:19:38 tah Final com
 * pdf
 * 
 * Revision 1.1 2006/04/03 21:30:45 tah Utilizando o nheengatu
 * 
 * Revision 1.7 2006/01/01 13:45:33 aryjr Feliz 2006!!!
 * 
 * Revision 1.6 2006/01/01 13:37:17 aryjr Rowspan is almost finished.
 * 
 * Revision 1.5 2005/12/16 14:06:32 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.4 2005/11/25 15:10:29 aryjr Head and foot on page break.
 * 
 * Revision 1.3 2005/11/24 14:17:54 aryjr Head and foot of PDF documents.
 * 
 * Revision 1.2 2005/11/18 15:10:53 aryjr Problems with rowspan.
 * 
 * Revision 1.1 2005/11/14 12:17:43 aryjr Renomeando os pacotes.
 * 
 * Revision 1.1 2005/09/10 23:43:39 aryjr Passando para o java.net.
 * 
 * Revision 1.5 2005/07/02 01:18:57 aryjunior Site do projeto.
 * 
 * Revision 1.4 2005/06/04 13:29:25 aryjunior LGPL.
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
