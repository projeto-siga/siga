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

import com.aryjr.nheengatu.html.HTMLDocument;
import com.aryjr.nheengatu.html.Tag;
import com.aryjr.nheengatu.pdf.PDFDocument;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * Teste com tabelas aninhadas.
 * 
 * @version $Id: TesteTabelasAninhadas.java,v 1.1 2007/12/26 15:57:41 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class TesteTabelasAninhadas {

	public static void main(final String[] args) {
		new TesteTabelasAninhadas();
	}

	public void OTesteTabelasAninhadas() {
		final Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"/home/aryjr/cvs/java/Nheengatu/teste-tabelas-aninhadas.pdf"));
			document.open();
			final PdfPTable table = new PdfPTable(2);
			final PdfPTable nested1 = new PdfPTable(1);
			nested1.addCell("1.1");
			nested1.addCell("2.1");
			nested1.addCell("3.1");
			nested1.addCell("4.1");
			nested1.addCell("5.1");
			nested1.addCell("6.1");
			final PdfPCell cell1 = new PdfPCell();
			final Paragraph p1 = new Paragraph("teste");
			cell1.addElement(p1);
			cell1.addElement(nested1);
			table.addCell(cell1);
			final PdfPTable nested2 = new PdfPTable(1);
			nested2.addCell("1.2");
			nested2.addCell("2.2");
			nested2.addCell("3.2");
			nested2.addCell("4.2");
			nested2.addCell("5.2");
			nested2.addCell("6.2");
			final PdfPCell cell2 = new PdfPCell();
			cell2.addElement(nested2);
			table.addCell(cell2);
			final PdfPTable nested3 = new PdfPTable(1);
			nested3.addCell("1.3");
			nested3.addCell("2.3");
			nested3.addCell("3.3");
			nested3.addCell("4.3");
			nested3.addCell("5.3");
			nested3.addCell("6.3");
			final PdfPCell cell3 = new PdfPCell();
			cell3.addElement(nested3);
			table.addCell(cell3);
			final PdfPTable nested4 = new PdfPTable(1);
			nested4.addCell("1.4");
			nested4.addCell("2.4");
			nested4.addCell("3.4");
			nested4.addCell("4.4");
			nested4.addCell("5.4");
			nested4.addCell("6.4");
			final PdfPCell cell4 = new PdfPCell();
			cell4.addElement(nested4);
			table.addCell(cell4);
			document.add(table);
			document.close();
			Runtime.getRuntime().exec("acroread /home/aryjr/cvs/java/Nheengatu/teste-tabelas-aninhadas.pdf");
		} catch (final DocumentException de) {
			System.err.println(de.getMessage());
		} catch (final IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}

	public TesteTabelasAninhadas() {
		final HTMLDocument html = new HTMLDocument("teste-tabelas-aninhadas");
		html.setPath("/home/aryjr/cvs/java/Nheengatu/");
		html.breakLines(true);
		html.getBody().setPropertyValue("bgcolor", "#DDDDDD");
		final PDFDocument pdf = new PDFDocument("teste-tabelas-aninhadas");
		pdf.setPath("/home/aryjr/cvs/java/Nheengatu/");
		html.getBody().addTag(new Tag("center", new Tag("h1", "Teste com tabelas aninhadas")));
		final Tag table = new Tag("table");
		table.setPropertyValue("bgcolor", "#000000");
		table.setPropertyValue("align", "center");
		table.setPropertyValue("width", "100%");
		table.setPropertyValue("id", "raiz");
		Tag row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		Tag cell = new Tag("td", table(1));
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", table(2));
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", table(3));
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", table(4));
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		html.getBody().addTag(table);
		pdf.setBody(html.getBody());
		try {
			html.generateFile();
			pdf.generateFile();
			Runtime.getRuntime().exec("acroread /home/aryjr/cvs/java/Nheengatu/teste-tabelas-aninhadas.pdf");
		} catch (final Exception e) {
			System.out.println("Exception ao criar os documentos.");
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private String ttable(final int ind) {
		return "tabela " + ind;
	}

	private Tag table(final int ind) {
		final Tag table = new Tag("table");
		table.setPropertyValue("cellspacing", "1");
		table.setPropertyValue("align", "center");
		table.setPropertyValue("width", "100%");
		table.setPropertyValue("bgcolor", "#000000");
		table.setPropertyValue("id", String.valueOf(ind));
		Tag row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		Tag cell = new Tag("td", "Fase");
		cell.setPropertyValue("align", "center");
		cell.setPropertyValue("rowspan", "2");
		row.addTag(cell);
		cell = new Tag("td", "Período");
		cell.setPropertyValue("align", "center");
		cell.setPropertyValue("colspan", "2");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "Início tabela " + ind);
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "Fim");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "FASE LITORÂNEA");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1500");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1640");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "FASE BANDEIRISTA");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1573");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1756");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "FASE AMAZÔNICA");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1616");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1832");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		row = new Tag("tr");
		row.setPropertyValue("bgcolor", "#FFFFFF");
		cell = new Tag("td", "FASE SERTANEJA");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1624");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		cell = new Tag("td", "1758");
		cell.setPropertyValue("align", "center");
		row.addTag(cell);
		table.addTag(row);
		return table;
	}
}
/**
 * 
 * $Log: TesteTabelasAninhadas.java,v $
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
 * Revision 1.6 2006/01/01 13:45:31 aryjr Feliz 2006!!!
 * 
 * Revision 1.5 2005/12/21 13:29:20 aryjr Nested table OK! The new problem is
 * the "nowrap" on cells.
 * 
 * Revision 1.4 2005/12/20 15:11:13 aryjr Nested tables!!!
 * 
 * Revision 1.3 2005/12/20 13:26:21 aryjr Fixed a bug with rowspan.
 * 
 * Revision 1.2 2005/12/16 14:06:29 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.1 2005/11/14 12:17:28 aryjr Renomeando os pacotes.
 * 
 * Revision 1.2 2005/11/11 21:09:51 aryjr Retomando o desenvolvimento e
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
 * Revision 1.3 2005/05/30 05:24:56 aryjunior Espacamento apos cada item dentro
 * do "body" com valor 20.
 * 
 * Revision 1.2 2005/05/30 05:08:25 aryjunior Testes com tabelas aninhadas e
 * cellspacing OK!!!
 * 
 * Revision 1.1 2005/05/30 01:55:57 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * 
 */
