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
package com.aryjr.nheengatu.openoffice.impress;

import com.aryjr.nheengatu.html.HTMLHandler;

/**
 * 
 * A HTML parser that can create the Nhengatu objects.
 * 
 * @version $Id: HTML2ImpressParser.java,v 1.1 2007/12/26 15:57:43 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class HTML2ImpressParser extends HTMLHandler {
	private ImpressDocument impress;

	public HTML2ImpressParser(final String documentPath, final String documentName) {
		super(documentPath, documentName);
	}

	protected void exportHTML() throws Exception {
		/*
		 * impress = new ImpressDocument(documentName);
		 * impress.setPath(documentPath);
		 * //impress.setTitle(html.getHead().getTitle());
		 * impress.generateFile(); impress = null;
		 */
	}

}
/**
 * 
 * $Log: HTML2ImpressParser.java,v $
 * Revision 1.1  2007/12/26 15:57:43  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:50  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:50  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:44 tah
 * Utilizando o nheengatu
 * 
 * Revision 1.5 2006/01/01 13:45:35 aryjr Feliz 2006!!!
 * 
 * Revision 1.4 2005/12/16 14:06:35 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.3 2005/12/05 13:45:09 aryjr CSV file support.
 * 
 * Revision 1.2 2005/11/24 14:17:55 aryjr Head and foot of PDF documents.
 * 
 * Revision 1.1 2005/11/14 12:17:44 aryjr Renomeando os pacotes.
 * 
 * Revision 1.1 2005/09/10 23:43:43 aryjr Passando para o java.net.
 * 
 * Revision 1.2 2005/07/02 01:18:57 aryjunior Site do projeto.
 * 
 * Revision 1.1 2005/06/19 14:16:33 aryjunior Preparando o suporte ao
 * OpenOffice.
 * 
 * 
 */
