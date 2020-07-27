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

import com.aryjr.nheengatu.html.HTMLDocument;
import com.aryjr.nheengatu.html.Tag;
import com.aryjr.nheengatu.pdf.PDFDocument;

/**
 * 
 * Teste para o pacote val-html-para-pdf.
 * 
 * @version $Id: TesteTabelao.java,v 1.1 2007/12/26 15:57:41 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class TesteTabelao {

	public static void main(final String[] args) {
		new TesteTabelao();
	}

	public TesteTabelao() {
		final HTMLDocument html = new HTMLDocument("teste-tabelao");
		html.setPath("/TEMP/");
		html.breakLines(true);
		final PDFDocument pdf = new PDFDocument("teste-tabelao");
		pdf.setPath("/TEMP/");
		html.getBody().addTag(table());
		pdf.setBody(html.getBody());
		// Agora salvando os documentos
		try {
			html.generateFile();
			pdf.generateFile();
			Runtime.getRuntime().exec("acroread /TEMP/teste-tabelao.pdf");
		} catch (final Exception e) {
			System.out.println("Exception ao criar os documentos.");
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private Tag table() {
		final Tag table = new Tag("table");
		table.setPropertyValue("align", "center");
		table.setPropertyValue("width", "400");
		table.setPropertyValue("bgcolor", "#FFFFFF");
		Tag row, cell;
		// The head
		final Tag thead = new Tag("thead");
		thead.setPropertyValue("style", "font-size: 10; font-family:Times New Roman");
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#DDDDDD");
		cell = new Tag("td", "NOME DA FASE");
		cell.setPropertyValue("align", "center");
		cell.setPropertyValue("rowspan", "2");
		row.addTag(cell);
		cell = new Tag("td", "Valores");
		cell.setPropertyValue("align", "center");
		cell.setPropertyValue("colspan", "2");
		row.addTag(cell);
		thead.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#DDDDDD");
		cell = new Tag("td", "Valor 1");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "Valor 2");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		thead.addTag(row);
		table.addTag(thead);
		// The body
		final Tag tbody = new Tag("tbody");
		tbody.setPropertyValue("style", "font-size: 10; font-family:Times New Roman");
		Tag img;
		Tag font;
		for (int inc = 0; inc < 100; inc++) {
			row = new Tag("tr");
			row.setPropertyValue("bgcolor", "#EEEEEE");
			font = new Tag("font", "FASE LITORÂNEA " + inc);
			font.setPropertyValue("color", "#DD0000");
			cell = new Tag("td", font);
			cell.setPropertyValue("align", "center");
			row.addTag(cell);
			cell = new Tag("td", String.valueOf(inc));
			cell.setPropertyValue("align", "center");
			row.addTag(cell);
			cell = new Tag("td", String.valueOf(inc) + " ");
			cell.setPropertyValue("align", "center");
			img = new Tag("img");
			img.setPropertyValue("src", "up.gif");
			cell.addTag(img);
			row.addTag(cell);
			tbody.addTag(row);
		}
		table.addTag(tbody);
		return table;
	}

}
/**
 * 
 * $Log: TesteTabelao.java,v $
 * Revision 1.1  2007/12/26 15:57:41  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:48  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:47  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:42 tah Utilizando o
 * nheengatu
 * 
 * Revision 1.4 2006/03/03 20:15:02 aryjr First steps with NheengatuDesktop.
 * 
 * Revision 1.3 2006/01/01 13:45:32 aryjr Feliz 2006!!!
 * 
 * Revision 1.2 2005/12/16 14:06:30 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.1 2005/11/14 12:17:28 aryjr Renomeando os pacotes.
 * 
 * Revision 1.2 2005/11/11 21:09:50 aryjr Retomando o desenvolvimento e
 * trabalhando no suporte ao CSS2.
 * 
 * Revision 1.1 2005/09/10 23:43:37 aryjr Passando para o java.net.
 * 
 * Revision 1.6 2005/07/02 01:18:57 aryjunior Site do projeto.
 * 
 * Revision 1.5 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.4 2005/05/30 14:31:24 aryjunior Ha uma pendencia com relacao aos
 * tamanhos das fontes.
 * 
 * Revision 1.3 2005/05/30 01:55:57 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:29 aryjunior Initial import.
 * 
 */
