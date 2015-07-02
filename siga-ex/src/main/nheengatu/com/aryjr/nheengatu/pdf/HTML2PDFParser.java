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
package com.aryjr.nheengatu.pdf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.aryjr.nheengatu.css2.StyleSheet;
import com.aryjr.nheengatu.html.HTML2MParser;
import com.aryjr.nheengatu.html.HTMLHandler;
import com.aryjr.nheengatu.html.Tag;

/**
 * 
 * A HTML parser that can create the Nhengatu objects and export to a PDF file.
 * 
 * @version $Id: HTML2PDFParser.java,v 1.2 2008/07/31 01:43:51 eeh Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class HTML2PDFParser extends HTML2MParser {
	private InputStream headFile;

	private InputStream footFile;

	private InputStream headFileFirstPage;

	private InputStream footFileFirstPage;

	private PDFDocument pdf;

	// private static Log log = LogFactory.getLog(HTML2PDFParser.class);

	/**
	 * <code>pdfFile</code>, pdf file generated.
	 * 
	 * @author Ney Anderson
	 */
	private OutputStream pdfFile;

	/**
	 * @param footFile
	 *            The footFile to set.
	 */
	public void setFootFile(final InputStream footFile) {
		this.footFile = footFile;
	}

	/**
	 * @param headFile
	 *            The headFile to set.
	 */
	public void setHeadFile(final InputStream headFile) {
		this.headFile = headFile;
	}

	public void setFootFileFirstPage(final InputStream footFileFirstPage) {
		this.footFileFirstPage = footFileFirstPage;
	}

	public void setHeadFileFirstPage(final InputStream headFileFirstPage) {
		this.headFileFirstPage = headFileFirstPage;
	}

	public void parse(final InputStream isDoc,
			final InputStream isHeadFirstPage,
			final InputStream isFootFirstPage, final InputStream isHead,
			final InputStream isFoot) throws UnsupportedEncodingException {

		// System.out.println(System.currentTimeMillis() + " - INI parse");

		HTMLHandler handler = new HTMLHandler();
		pdf = new PDFDocument();
		pdf.setPath(documentPath);
		pdf.setName(documentName);
		// pdf.setTitle(html.getHead().getTitle());
		// System.out.println(System.currentTimeMillis() + " - INI parse 1");
		parse(handler, isDoc);
		// System.out.println(System.currentTimeMillis() + " - FIM parse 1");
		pdf.setBody(handler.getHtml().getBody());

		Tag head = handler.getHtml().getHead();

		if (head != null) {
			Tag style = head.getFirstTag("style");
			// Tag style = handler.getHtml().getFirstTag("style");
			if (style != null) {
				String sSS = style.getText();
				if (sSS != null) {
					sSS = sSS.replace("&#64;", "@");
					StyleSheet ss = new StyleSheet();
					ss.processCSSFile(new ByteArrayInputStream(sSS
							.getBytes("utf-8")));
					pdf.setStyleSheet(ss);
				}
			}
		}

		// Parsing the head
		if (isHead != null) {
			handler = new HTMLHandler();
			// System.out.println(System.currentTimeMillis() + " - INI parse
			// 2");
			parse(handler, isHead);
			// System.out.println(System.currentTimeMillis() + " - FIM parse
			// 2");
			if (handler.getHtml() != null)
				pdf.setHead(handler.getHtml().getBody().getFirstTag("table"));
		}

		// Parsing the foot
		if (isFoot != null) {
			handler = new HTMLHandler();
			// System.out.println(System.currentTimeMillis() + " - INI parse
			// 3");
			parse(handler, isFoot);
			// System.out.println(System.currentTimeMillis() + " - FIM parse
			// 3");
			pdf.setFoot(handler.getHtml().getBody().getFirstTag("table"));
		}

		// Parsing the head of the first page
		if (isHeadFirstPage != null) {
			handler = new HTMLHandler();
			// System.out.println(System.currentTimeMillis() + " - INI parse
			// 4");
			parse(handler, isHeadFirstPage);
			// System.out.println(System.currentTimeMillis() + " - FIM parse
			// 4");
			pdf.setHeadFirstPage(handler.getHtml().getBody().getFirstTag(
					"table"));
		}

		// Parsing the foot
		if (isFootFirstPage != null) {
			handler = new HTMLHandler();
			// System.out.println(System.currentTimeMillis() + " - INI parse
			// 5");
			parse(handler, isFootFirstPage);
			// System.out.println(System.currentTimeMillis() + " - FIM parse
			// 5");
			pdf.setFootFirstPage(handler.getHtml().getBody().getFirstTag(
					"table"));
		}

		// System.out.println(System.currentTimeMillis() + " - FIM parse");

	}

	/**
	 * @return Retorna o(a) pdfFile.
	 */
	public OutputStream getPdfFile() {
		return pdfFile;
	}

	/**
	 * Seta o(a) pdfFile
	 * 
	 * @param OutputStream
	 *            pdfFile.
	 */
	public void setPdfFile(final OutputStream pdfFile) {
		this.pdfFile = pdfFile;
	}

	public PDFDocument getPdf() {
		return pdf;
	}

}
/**
 * 
 * $Log: HTML2PDFParser.java,v $
 * Revision 1.2  2008/07/31 01:43:51  eeh
 * *** empty log message ***
 * Revision 1.1 2007/12/26 15:57:41 tah *** empty
 * log message ***
 * 
 * Revision 1.7 2007/07/19 21:17:22 nts Inserindo comentários
 * 
 * Revision 1.6 2007/06/29 16:20:45 tah *** empty log message ***
 * 
 * Revision 1.5 2006/11/23 21:39:59 tah *** empty log message ***
 * 
 * Revision 1.4 2006/10/24 18:02:38 tah *** empty log message *** Revision 1.3
 * 2006/07/05 16:00:47 nts Refatorando para melhorar qualidade do código
 * 
 * Revision 1.2 2006/04/11 19:43:46 tah *** empty log message *** Revision 1.1
 * 2006/04/03 21:30:42 tah Utilizando o nheengatu Revision 1.10 2006/01/02
 * 18:27:27 neyanderson Adaptando para geração do arquivo direto no
 * OutputStream.
 * 
 * Revision 1.9 2006/01/01 13:45:32 aryjr Feliz 2006!!!
 * 
 * Revision 1.8 2005/12/16 14:06:31 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.7 2005/12/07 15:00:44 aryjr Problems with the head and foot inside
 * a war file.
 * 
 * Revision 1.6 2005/12/07 14:49:49 aryjr Bugs with the relatorio.jsp HTML code.
 * 
 * Revision 1.5 2005/12/07 11:41:10 aryjr Problems with the foot and head files
 * inside a .war file.
 * 
 * Revision 1.4 2005/12/05 13:45:03 aryjr CSV file support.
 * 
 * Revision 1.3 2005/11/25 15:10:28 aryjr Head and foot on page break.
 * 
 * Revision 1.2 2005/11/24 14:17:52 aryjr Head and foot of PDF documents.
 * 
 * Revision 1.1 2005/11/14 12:17:30 aryjr Renomeando os pacotes.
 * 
 * Revision 1.1 2005/09/10 23:43:40 aryjr Passando para o java.net.
 * 
 * Revision 1.4 2005/07/02 01:18:56 aryjunior Site do projeto.
 * 
 * Revision 1.3 2005/06/19 14:16:33 aryjunior Preparando o suporte ao
 * OpenOffice.
 * 
 * Revision 1.2 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.1 2005/06/03 23:09:23 aryjunior Generalizando o parser HTML.
 * 
 * 
 */
