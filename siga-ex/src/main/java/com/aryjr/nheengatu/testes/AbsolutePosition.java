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

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * Absolute positioning tests.
 * 
 * @version $Id: AbsolutePosition.java,v 1.1 2007/12/26 15:57:41 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class AbsolutePosition {

	public static void main(final String[] args) {
//		System.out.println("My First PdfPTable");
		final Document document = new Document();
		try {
			final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/home/aryjr/MyFirstTable.pdf"));
			document.open();
			// PdfPTable table = createTable();
			// document.add(table);
			// table.writeSelectedRows(0, -1, 50, 200,
			// writer.getDirectContent());
			final PdfContentByte cb = writer.getDirectContent();
			cb.concatCTM(1f, 0f, 0f, -1f, 0f, 0f);
			// Paragraph text = new Paragraph("Ary Junior",
			// FontFactory.getFont(Style.DEFAULT_FONT_FAMILY,
			// Style.DEFAULT_FONT_SIZE, Font.NORMAL, Style.DEFAULT_FONT_COLOR));
			final BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			cb.beginText();
			cb.setFontAndSize(bf, 12);
			cb.showText("Ary Junior");
			cb.endText();
			Runtime.getRuntime().exec("acroread /home/aryjr/MyFirstTable.pdf");
		} catch (final DocumentException de) {
			System.err.println(de.getMessage());
		} catch (final IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}

	private static PdfPTable createTable() {
		final PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(200);
		PdfPCell cell = new PdfPCell(new Paragraph("header with colspan 3"));
		cell.setColspan(3);
		table.addCell(cell);
		table.addCell("1.1");
		table.addCell("2.1");
		table.addCell("3.1");
		table.addCell("1.2");
		table.addCell("2.2");
		table.addCell("3.2");
		cell = new PdfPCell(new Paragraph("cell test1"));
		cell.setBorderColor(new Color(255, 0, 0));
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("cell test2"));
		cell.setColspan(2);
		cell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
		table.addCell(cell);
		return table;
	}

}
