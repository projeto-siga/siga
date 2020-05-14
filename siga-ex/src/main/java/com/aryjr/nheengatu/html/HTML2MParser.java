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

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringReader;

import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.InputSource;

/**
 * 
 * A HTML parser that can create the Nhengatu objects.
 * 
 * @version $Id: HTML2MParser.java,v 1.1 2007/12/26 15:57:42 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class HTML2MParser {
	protected String documentPath;

	protected String documentName;

	protected FileReader documentFile;

	/**
	 * @param documentFile
	 *            The documentFile to set.
	 */
	public void setDocumentFile(final FileReader documentFile) {
		this.documentFile = documentFile;
	}

	/**
	 * @param documentName
	 *            The documentName to set.
	 */
	public void setDocumentName(final String documentName) {
		this.documentName = documentName;
	}

	/**
	 * @param documentPath
	 *            The documentPath to set.
	 */
	public void setDocumentPath(final String documentPath) {
		this.documentPath = documentPath;
	}

	public void parse() {
	}

	protected void parse(final HTMLHandler htmlHandler, final InputStream is) {
		final Parser parser = new Parser();
		parser.setContentHandler(htmlHandler);
		parser.setErrorHandler(htmlHandler);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			byte b[] = new byte[1024];
			while (true) {
				int i = is.read(b);
				if (i == -1)
					break;
				baos.write(b, 0, i);
			}
			if (baos.size() == 0)
				return;
			String s = new String(baos.toByteArray(), "utf-8");
			// parser.parse(s);
		//	System.out.println(System.currentTimeMillis() + " - INI parse.parse");
			parser.parse(new InputSource(new StringReader(s)));
		//	System.out.println(System.currentTimeMillis() + " - FIM parse.parse");
			// parser.parse(new InputSource(is));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void parse(final HTMLHandler htmlHandler, final FileReader fr) {
		final Parser parser = new Parser();
		parser.setContentHandler(htmlHandler);
		parser.setErrorHandler(htmlHandler);
		try {
			parser.parse(new InputSource(fr));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
/**
 * 
 * $Log: HTML2MParser.java,v $
 * Revision 1.1  2007/12/26 15:57:42  tah
 * *** empty log message ***
 *
 * Revision 1.8  2007/07/19 21:16:42  nts
 * Inserindo comentários
 *
 * Revision 1.7  2007/06/29 16:20:45  tah
 * *** empty log message ***
 *
 * Revision 1.6  2006/11/23 21:39:59  tah
 * *** empty log message ***
 * Revision 1.5 2006/07/06 15:45:26 tah *** empty
 * log message ***
 * 
 * Revision 1.4 2006/07/05 16:00:51 nts Refatorando para melhorar qualidade do
 * código
 * 
 * Revision 1.3 2006/04/11 19:43:51 tah *** empty log message *** Revision 1.2
 * 2006/04/10 20:26:41 tah *** empty log message ***
 * 
 * Revision 1.1 2006/04/03 21:30:45 tah Utilizando o nheengatu
 * 
 * Revision 1.5 2006/03/03 21:37:07 aryjr Working with the report builder on
 * NheengatuDesktop.
 * 
 * Revision 1.4 2006/01/01 13:45:33 aryjr Feliz 2006!!!
 * 
 * Revision 1.3 2005/12/16 14:06:32 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.2 2005/12/07 15:00:45 aryjr Problems with the head and foot inside
 * a war file.
 * 
 * Revision 1.1 2005/12/05 13:45:06 aryjr CSV file support.
 * 
 */
