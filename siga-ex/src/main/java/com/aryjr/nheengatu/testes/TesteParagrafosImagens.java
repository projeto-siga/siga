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
package com.aryjr.nheengatu.testes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * Teste para imagens dentro de paragrafos.
 * 
 * @version $Id: TesteParagrafosImagens.java,v 1.1 2007/12/26 15:57:41 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class TesteParagrafosImagens {

	public static void main(final String[] args) {
		new TesteParagrafosImagens();
	}

	public TesteParagrafosImagens() {
		final Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"/home/aryjr/cvs/java/Nheengatu/teste-paragrafos-imagens.pdf"));
			document.open();
			final Paragraph p = new Paragraph();
			final Image img1 = Image.getInstance(new URL("http://img.vci.com.br/marcadores/seta_cima_g.gif"));
			img1.setAlignment(Image.TEXTWRAP | Element.ALIGN_TOP);
			p.add(img1);
			final Image img2 = Image.getInstance(new URL("http://img.vci.com.br/marcadores/seta_baixo_g.gif"));
			img2.setAlignment(Image.TEXTWRAP | Element.ALIGN_TOP);
			p.add(img2);
			p
					.add(new Chunk(
							"Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos.Teste da imagem e texto dentro de paragrafos."));
			document.add(p);
			Runtime.getRuntime().exec("acroread /home/aryjr/cvs/java/Nheengatu/teste-paragrafos-imagens.pdf");
		} catch (final DocumentException de) {
			System.err.println(de.getMessage());
		} catch (final IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}

}
/**
 * 
 * $Log: TesteParagrafosImagens.java,v $
 * Revision 1.1  2007/12/26 15:57:41  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:48  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:47  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:42 tah
 * Utilizando o nheengatu
 * 
 * Revision 1.2 2006/01/01 13:45:31 aryjr Feliz 2006!!!
 * 
 * Revision 1.1 2005/12/19 15:17:05 aryjr Breaking lines when Images and text
 * are inside the same paragraphs not yet solved.
 * 
 * 
 */
