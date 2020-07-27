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

import com.aryjr.nheengatu.html.Text;
import com.aryjr.nheengatu.util.TagsManager;
import com.lowagie.text.Chunk;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;

/**
 * 
 * A HTML text fragment in a PDF document.
 * 
 * @version $Id: PDFText.java,v 1.2 2009/07/30 14:43:36 kpf Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class PDFText {

	public static Paragraph createParagraph(final Text htmlText, TagsManager tm) {

		if (tm == null)
			tm = TagsManager.getInstance();

		final Paragraph text = new Paragraph(htmlText != null ? htmlText.getText() : null, tm.getFont());
		text.setAlignment(tm.getAlign());
		text.setKeepTogether(true);
		// float b = tm.getSpacingBefore();
		// float a = tm.getSpacingAfter();
		text.setSpacingBefore(tm.getSpacingBefore());
		text.setSpacingAfter(tm.getSpacingAfter());
		text.setFirstLineIndent(tm.getTextIndent());
		text.setIndentationLeft(tm.getMarginLeft());
		// text.setFirstLineIndent(20f);
		text.setLeading(tm.getFont().getSize() * 1.5f);
		return text;
	}

	public static Chunk createChunk(final Text htmlText) {
		final TagsManager tm = TagsManager.getInstance();
		final Chunk text = new Chunk(htmlText.getText(), tm.getFont());
		return text;
	}

	public static float getWidth(final Text htmlText) {
		final TagsManager tm = TagsManager.getInstance();
		final Chunk text = new Chunk(htmlText.getText(), tm.getFont());
		return text.getWidthPoint();
	}

	public static float getHeight(final Text htmlText) {
		final TagsManager tm = TagsManager.getInstance();
		final BaseFont bf = tm.getFont().getCalculatedBaseFont(true);
		return bf.getAscentPoint("teste", tm.getFont().getCalculatedSize())
				- bf.getDescentPoint("teste", tm.getFont().getCalculatedSize());
	}

}
/**
 * 
 * $Log: PDFText.java,v $
 * Revision 1.2  2009/07/30 14:43:36  kpf
 * Mudançaa de pacote: itext v.1.4 para itext v 2.1.5.
 *
 * Alterações para suportar a nova  versão do text 2.1.5
 *
 * Revision 1.1  2007/12/26 15:57:41  tah
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/06 19:55:52  tah
 * *** empty log message ***
 *
 * Revision 1.7  2006/07/10 18:33:25  tah
 * *** empty log message ***
 *
 * Revision 1.6  2006/07/05 16:00:47  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.5  2006/05/23 19:35:06  tah
 * *** empty log message ***
 * Revision 1.4 2006/05/22 19:29:49 tah *** empty log
 * message ***
 * 
 * Revision 1.3 2006/05/11 20:30:23 tah *** empty log message ***
 * 
 * Revision 1.2 2006/04/11 19:43:46 tah *** empty log message *** Revision 1.1
 * 2006/04/03 21:30:42 tah Utilizando o nheengatu
 * 
 * Revision 1.5 2006/01/01 13:45:32 aryjr Feliz 2006!!!
 * 
 * Revision 1.4 2005/12/16 14:06:32 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.3 2005/12/10 13:53:20 aryjr *** empty log message ***
 * 
 * Revision 1.2 2005/11/17 14:49:31 aryjr Cell widths OK!!! By now :-P
 * 
 * Revision 1.1 2005/11/14 12:17:31 aryjr Renomeando os pacotes.
 * 
 * Revision 1.2 2005/09/26 19:41:13 aryjr Aproveitando a greve para voltar a
 * atividade.
 * 
 * Revision 1.1 2005/09/10 23:43:40 aryjr Passando para o java.net.
 * 
 * Revision 1.6 2005/07/02 01:18:56 aryjunior Site do projeto.
 * 
 * Revision 1.5 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.4 2005/05/30 05:28:48 aryjunior Ajustando alguns javadocs.
 * 
 * Revision 1.3 2005/05/30 01:55:56 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:32 aryjunior Initial import.
 * 
 * 
 */
