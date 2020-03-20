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
package com.aryjr.nheengatu.document;

/**
 * 
 * A basic document.
 * 
 * @version $Id: Document.java,v 1.1 2007/12/26 15:57:43 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class Document {
	private String path = ".";

	private String name = "newdoc";

	/**
	 * 
	 * Sets the document name.
	 * 
	 * @param name
	 *            The document's name without the extension. The default value
	 *            is "newdoc".
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * 
	 * Sets the document path. The default value is ".".
	 * 
	 * @param path
	 *            The document's path.
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * 
	 * Returns the document name.
	 * 
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * Returns the document path.
	 * 
	 */
	public String getPath() {
		return this.path;
	}

}
/**
 * 
 * $Log: Document.java,v $
 * Revision 1.1  2007/12/26 15:57:43  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:49  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:49  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:44 tah Utilizando o
 * nheengatu
 * 
 * Revision 1.3 2006/01/01 13:45:34 aryjr Feliz 2006!!!
 * 
 * Revision 1.2 2005/12/16 14:06:34 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.1 2005/11/14 12:17:48 aryjr Renomeando os pacotes.
 * 
 * Revision 1.1 2005/09/10 23:43:41 aryjr Passando para o java.net.
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
