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

/**
 * 
 * Represents a text fragment.
 * 
 * @version $Id: Text.java,v 1.1 2007/12/26 15:57:42 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class Text {
	private String text;

	private String textSave;

	private Boolean fSaved = false;

	public void setText(final String text) {
		this.text = text;
	}

	public void changeText(final String text) {
		this.textSave = this.text;
		this.text = text;
		this.fSaved = true;
	}

	public void restoreText() {
		if (this.fSaved) {
			this.text = this.textSave;
			fSaved = false;
		}
	}

	public Text() {
		text = "";
	}

	/**
	 * 
	 * You can construct this object with a text.
	 * 
	 * @param text
	 *            Start text.
	 * 
	 */
	public Text(final String text) {
		this.text = text;
	}

	/**
	 * 
	 * Returns the text fragment.
	 * 
	 */
	public String getText() {
		return text;
	}

}
/**
 * 
 * $Log: Text.java,v $
 * Revision 1.1  2007/12/26 15:57:42  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:50  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:51  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:45 tah Utilizando o
 * nheengatu Revision 1.5 2006/01/01 13:45:33 aryjr Feliz 2006!!!
 * 
 * Revision 1.4 2005/12/16 14:06:32 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.3 2005/11/17 14:49:30 aryjr Cell widths OK!!! By now :-P
 * 
 * Revision 1.2 2005/11/16 15:37:13 aryjr Larguras das celulas.
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
