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

import java.util.ArrayList;
import java.util.Iterator;

import com.aryjr.nheengatu.html.Tag;
import com.aryjr.nheengatu.html.Text;
import com.aryjr.nheengatu.util.TagsManager;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;


/**
 * 
 * A HTML table in a PDF document.
 * 
 * @version $Id: PDFTable.java,v 1.3 2009/08/05 19:02:03 kpf Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior </a>
 * 
 */
public class PDFTable extends PdfPTable {
	private static final String CELLSPACING = "cellspacing";
	private static final String CELLPADDING = "cellpadding";

	private static final String ROWSPAN = "rowspan";

	private static final String COLSPAN = "colspan";

	public PDFTable(final int cols) {
		super(cols);
	}

	public static PDFTable createTable(final Tag htmlTable) throws DocumentException {
		// TODO Do not use <tfoot> tag !!! Inside a <table> only <thead>,
		// <tbody> and <tr>.
		// TODO If you are using a <thead>, you can use only <tbody> inside a
		// <table>.
		// TODO If you do not use <thead>, you can use only <tr>
		int headerRowCount = 0;
		if (htmlTable.getFirstTag("thead") != null || htmlTable.getFirstTag("tbody") != null) {
			// Before I will remove the <thead> and <tbody> tags
			headerRowCount = PDFTable.removeTheadTbody(htmlTable);
		}
		// Now, I will remove all rowspans from table and convert it to
		// nestedTables
		headerRowCount -= PDFTable.removeAllRowspans(htmlTable);
		// Creating the iText table
		PDFTable table;
		final TagsManager tm = TagsManager.getInstance();
		int maxCols = 0;// The max number of cells in this table
		final Iterator rows = htmlTable.tags();
		Tag row;
		while (rows.hasNext()) {
			row = (Tag) rows.next();
			if (maxCols < row.tagsCollection().size()) {
				maxCols = row.tagsCollection().size();
			}
		}
		table = new PDFTable(maxCols);
		table.setHeaderRows(headerRowCount < 0 ? 0 : headerRowCount);
		tm.checkTag(htmlTable);
		final CellWidths cws = new CellWidths();
		PDFTable.createTableRows(htmlTable, htmlTable, table, cws);
		tm.back();
		String align = htmlTable.getPropertyValue("align");
		if (align != null) {
			align = align.toLowerCase();
			if (align.equals("left"))
				table.setHorizontalAlignment(Element.ALIGN_LEFT);
			else if (align.equals("right"))
				table.setHorizontalAlignment(Element.ALIGN_RIGHT);
			else if (align.equals("center"))
				table.setHorizontalAlignment(Element.ALIGN_CENTER);
		}
		final String width = htmlTable.getPropertyValue("width");
		// table.setLockedWidth(true);
		if (width != null && width.indexOf('%') > 0) {
			// TODO what is the reference for the % here? See the method
			// table.setWidthPercentage(float[] , Rectangle).
			table.setWidthPercentage(Float.parseFloat(width.substring(0, width.length() - 1)));
		} else if (width != null) {
			table.setTotalWidth(Float.parseFloat(width));
		}
		return table;
	}

	private static void createTableRows(final Tag htmlTable, final Tag content, final PDFTable table, final CellWidths cws)
			throws DocumentException {
		final TagsManager tm = TagsManager.getInstance();
		final ArrayList rows = content.tagsCollection();
		Tag row;
		ArrayList suggestedCellWidths;
		boolean fCellWidthsAreSet = false;
		for (int inc = 0; inc < rows.size(); inc++) {
			suggestedCellWidths = new ArrayList();
			row = (Tag) rows.get(inc);
			// Inside a "table" only "tr", "tbody" or "thead"!!! Please!!!
			if (!row.getName().equalsIgnoreCase("tr")) {
				continue;
			}
			row.setPropertyValue(PDFTable.CELLSPACING, content.getPropertyValue(PDFTable.CELLSPACING));
			row.setPropertyValue(PDFTable.CELLPADDING, content.getPropertyValue(PDFTable.CELLPADDING));
			tm.checkTag(row);
			PDFTable.createTableRow(table, row, suggestedCellWidths);
			// The columns widths are defined by the first row without colspan
			if (PDFTable.checkRowspanOrColspan(row, PDFTable.COLSPAN) == 0 && !fCellWidthsAreSet) {
				Tag cell = null;
				String width = null;
				int cww = 0;// Cells Without Width
				float sumWidths = 0f;
				if (cws.size() == 0) {
					for (int i = 0; i < row.tagsCollection().size(); i++) {
						cws.add(new Float(0f));
					}
				}
				// All cells with percentage widths or all cells with widths in
				// pixels. It's a iText requirement.
				final Iterator cells = row.tags();
				boolean percentage = false;
				for (int i = 0; i < cws.size() && cells.hasNext(); i++) {
					cell = (Tag) cells.next();
					width = cell.getPropertyValue("width");
					if (width == null) {
						cww++;
					} else {
						if (!percentage) {
							percentage = width.indexOf('%') > 0;
						}
						cws.set(i, new Float(width == null ? 0f : percentage ? Float.parseFloat(width.substring(0,
								width.length() - 1)) : Float.parseFloat(width)));
						sumWidths += ((Float) cws.get(i)).floatValue();
					}
				}
				// Checking the cells without the width property
				float value;
				if (percentage) {
					value = (100 - sumWidths) / cww;
				} else {
					value = 1;
				}
				for (int ic = 0; ic < cws.size(); ic++) {
					if (((Float) cws.get(ic)).floatValue() == 0f) {
						cws.set(ic, new Float(value));
					}
				}
				/*
				 * for (int icc = 0; icc < suggestedCellWidths.size(); icc++) {
				 * if (((Float) cws.get(icc)).floatValue() < ((Float)
				 * suggestedCellWidths.get(icc)).floatValue()) { cws.set(icc,
				 * (Float) suggestedCellWidths.get(icc)); } }
				 */
				// TODO problems here yet
				table.setWidths(cws.toFloatArray());
				fCellWidthsAreSet = true;
			}
			tm.back();
		}
	}

	private static int removeTheadTbody(final Tag table) {
		// Removes the <thead> and <tbody> from a table and returns the number
		// of rows in <thead>
		final Tag thead = table.removeFirstTag("thead");
		final Tag tbody = table.removeFirstTag("tbody");

		Iterator rows;
		Tag row;
		int rowspan = 0;
		if (thead != null) {
			rows = thead.tags();
			while (rows.hasNext()) {
				row = (Tag) rows.next();
				if (thead.getPropertyValue("style") != null) {
					row.setPropertyValue("style", thead.getPropertyValue("style"));
				}
				table.addTag(row);
				if (PDFTable.checkRowspanOrColspan(row, PDFTable.ROWSPAN) > rowspan) {
					rowspan = PDFTable.checkRowspanOrColspan(row, PDFTable.ROWSPAN);
				}
			}
		}

		if (tbody != null) {
			rows = tbody.tags();
			while (rows.hasNext()) {
				row = (Tag) rows.next();
				if (tbody.getPropertyValue("style") != null) {
					row.setPropertyValue("style", tbody.getPropertyValue("style"));
				}
				table.addTag(row);
			}
		}
		return rowspan == 0 ? 1 : rowspan;
	}

	private static int removeAllRowspans(final Tag table) {
		final ArrayList rows = table.tagsCollection();
		// TODO the major rowspan ever on the first row, for now
		final int indRow = 0;
		final Tag row = (Tag) rows.get(indRow);
		final ArrayList cells = row.tagsCollection();
		int rowspan = 0, colspan = 0;
		final int majorRowspan = PDFTable.checkRowspanOrColspan(row, PDFTable.ROWSPAN);
		if (majorRowspan == 0)
			return majorRowspan;
		Tag auxRow, ntdTable, ntdRow;
		final Tag newRow = row.getEmptyCopy();
		Tag cell, newCell, ntdCell;
		ArrayList ntdCells;
		// iterate all <td> tags of the first row
		final int cellsCount = cells.size();
		for (int inc = 0; inc < cellsCount; inc++) {
			cell = row.removeTag(0);
			rowspan = cell.getPropertyValue(PDFTable.ROWSPAN) == null ? 0 : Integer.parseInt(cell.getPropertyValue(PDFTable.ROWSPAN));
			// if the <td> tag have the major rowspan
			if (rowspan == majorRowspan) {
				// it will be a <td> of the new line
				cell.removeProperty(PDFTable.ROWSPAN);
				newRow.addTag(cell);
			} else {
				// creating the nested table
				ntdTable = table.getEmptyCopy();
				ntdTable.setPropertyValue("width", "100%");
				ntdRow = row.getEmptyCopy();
				colspan += cell.getPropertyValue(PDFTable.COLSPAN) == null ? 1 : Integer
						.parseInt(cell.getPropertyValue(PDFTable.COLSPAN));
				ntdRow.addTag(cell);
				cells: for (inc++; inc < cellsCount; inc++) {
					cell = row.removeTag(0);
					if (cell.getPropertyValue(PDFTable.ROWSPAN) != null
							&& Integer.parseInt(cell.getPropertyValue(PDFTable.ROWSPAN)) == majorRowspan) {
						row.addTag(cell, 0);
						inc--;
						break cells;
					} else {
						colspan += cell.getPropertyValue(PDFTable.COLSPAN) == null ? 1 : Integer.parseInt(cell
								.getPropertyValue(PDFTable.COLSPAN));
						ntdRow.addTag(cell);
					}
				}
				ntdTable.addTag(ntdRow);
				for (int i = indRow + 1; i < majorRowspan; i++) {
					ntdRow = row.getEmptyCopy();
					auxRow = (Tag) rows.get(i);
					ntdCells = auxRow.tagsCollection();
					final int ntdCellsCount = ntdCells.size();
					ntdCells: for (int ic = 0, colspanSum = 0; ic < ntdCellsCount; ic++) {
						ntdCell = auxRow.removeTag(0);
						if (colspanSum == colspan) {
							auxRow.addTag(ntdCell, 0);
							break ntdCells;
						} else {
							colspanSum += ntdCell.getPropertyValue(PDFTable.COLSPAN) == null ? 1 : Integer.parseInt(ntdCell
									.getPropertyValue(PDFTable.COLSPAN));
							ntdRow.addTag(ntdCell);
						}
					}
					ntdTable.addTag(ntdRow);
				}
				PDFTable.adjustColspan(ntdTable);
				PDFTable.removeAllRowspans(ntdTable);
				// put the nested table inside a <td> of the new line
				newCell = cell.getEmptyCopy();
				newCell.removeProperty(PDFTable.ROWSPAN);
				newCell.addTag(ntdTable);
				newCell.setPropertyValue("itext-padding", "5f");
				newCell.setPropertyValue("colspan", String.valueOf(colspan));
				newRow.addTag(newCell);
				colspan = 0;
			}
		}
		// removing all rows of rowspan and put the new line
		for (int inc = 0; inc < majorRowspan; inc++) {
			table.removeTag(indRow);
		}
		table.addTag(newRow, indRow);
		return majorRowspan - 1;
	}

	private static void adjustColspan(final Tag table) {
		// Finding the row with the major number of cells
		ArrayList rows = table.tagsCollection();
		int maxCells = 0, ind = 0;
		for (int inc = 0; inc < rows.size(); inc++) {
			if (maxCells < ((Tag) rows.get(inc)).tagsCollection().size()) {
				maxCells = ((Tag) rows.get(inc)).tagsCollection().size();
				ind = inc;
			}
		}
		// Check if that row have colspan
		if (PDFTable.checkRowspanOrColspan((Tag) rows.get(ind), PDFTable.COLSPAN) == 0
				|| PDFTable.checkRowspanOrColspan((Tag) rows.get(ind), PDFTable.ROWSPAN) > 0)
			return;
		// Now, reducing the colspan values
		rows = table.tagsCollection();
		Iterator cells;
		Tag cell;
		for (int inc = 0; inc < rows.size(); inc++) {
			cells = ((Tag) rows.get(inc)).tags();
			while (cells.hasNext()) {
				cell = (Tag) cells.next();
				if (cell.getPropertyValue(PDFTable.COLSPAN) != null) {
					cell
							.setPropertyValue(PDFTable.COLSPAN, String
									.valueOf(Integer.parseInt(cell.getPropertyValue(PDFTable.COLSPAN)) / 2));
					if (Integer.parseInt(cell.getPropertyValue(PDFTable.COLSPAN)) <= 1) {
						cell.removeProperty(PDFTable.COLSPAN);
					}
				}
			}
		}
	}

	private static void createTableRow(final PDFTable table, final Tag row, final ArrayList suggestedCellWidths) {
		final TagsManager tm = TagsManager.getInstance();
		final Iterator cells = row.tags();
		Tag htmlCell;
		PdfPCell cell;
		Paragraph pCell;
		try {
			int inc = 0;
			// TODO suggested cell widths only without rowspan
			while (cells.hasNext()) {
				htmlCell = (Tag) cells.next();
				htmlCell.setPropertyValue(PDFTable.CELLSPACING, row.getPropertyValue(PDFTable.CELLSPACING));
				htmlCell.setPropertyValue(PDFTable.CELLPADDING, row.getPropertyValue(PDFTable.CELLPADDING));
				cell = PDFTable.createCell(htmlCell);
				tm.checkTag(htmlCell);
				pCell = new Paragraph();
				// TODO Cell heights problem solved!!! Waiting for a iText
				// solution.
				pCell.setLeading((pCell.getLeading() / 2) + 2);
				//pCell.setLeading((pCell.leading() / 2));
				cell.addElement(pCell);
				if (htmlCell.getPropertyValue("itext-padding") != null) {
					cell.setPadding(Float.valueOf(htmlCell.getPropertyValue("itext-padding")).floatValue());
				}
				if (htmlCell.getPropertyValue("cellpadding") != null) {
					cell.setPadding(Float.valueOf(htmlCell.getPropertyValue("cellpadding")).floatValue());
				}
				if (htmlCell.getPropertyValue("cellspacing") != null) {
					cell.setBorderWidth(Float.valueOf(htmlCell.getPropertyValue("cellspacing")).floatValue());
				}
				final float cellWidth = PDFTable.extractVisibleComponents(htmlCell, cell, pCell, 0f);
				// TODO compare the size of anothers widths
				suggestedCellWidths.add(new Float(cellWidth));
				tm.back();
				table.addCell(cell);
				inc++;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	// TODO for each cell I have a paragraph associated
	private static float extractVisibleComponents(final Tag tag, final PdfPCell cell, final Paragraph paragraph, float cellWidth)
			throws DocumentException {
		final Iterator tags = tag.tags();// tags inside the cell
		Object component;
		final TagsManager tm = TagsManager.getInstance();
		// Seting the HTML row or table background color
		if (tag.getName().equalsIgnoreCase("TD")) {
			cell.setBorderColor(tm.getPreviousState(2).getBgcolor());
			cell.setBorderWidth(tag.getPropertyValue(PDFTable.CELLSPACING) == null ? 1 : Integer.parseInt(tag
					.getPropertyValue(PDFTable.CELLSPACING)));
			//cell.setBackgroundColor(tm.getBgcolor());
			cell.setBackgroundColor(new Color(255, 255, 255));
			cell.setVerticalAlignment(tm.getValign());
			cell.setHorizontalAlignment(tm.getAlign());
			paragraph.setAlignment(tm.getAlign());
		}
		while (tags.hasNext()) {
			component = tags.next();
			if (component instanceof Text) {
				String s = ((Text) component).getText();
				if (s.contains("\\\"")) {
					s = s.replace("\\\"", "\"");
					((Text) component).setText(s);
				}
				// If it's a text, create a iText text component for it
				paragraph.add(PDFText.createChunk((Text) component));
				cellWidth += PDFText.getWidth((Text) component);
			} else if (component instanceof Tag && ((Tag) component).getName().equalsIgnoreCase("br")) {
				// If it's a HTML line break
				paragraph.add("\n");
			} else if (component instanceof Tag && ((Tag) component).getName().equalsIgnoreCase("img")) {
				// If it's a HTML image, create a iText image component for it
				try {
					final Image img = PDFImage.createImage((Tag) component);
					//Nato: paragraph.add(new Chunk(img, 0, -2));
					paragraph.add(new Chunk(img, 0, 0));
					cellWidth += img.getScaledWidth();
					// TODO Is there an iText bug here?
					//if (img.scaledHeight() == 1f) {
						//cell.setFixedHeight(img.getScaledHeight());
					//}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} else if (component instanceof Tag && ((Tag) component).getName().equalsIgnoreCase("table")) {
				// If it's a HTML table, create a iText table component for it
				try {
					// TODO the default value of nowrap here will be true or
					// false??? If true, there is a problem with the cell width
					cell.setNoWrap(false);
					final PDFTable t = PDFTable.createTable((Tag) component);
					cell.addElement(t);
					final float[] cw = t.getWidths();
					for (final float element : cw) {
						cellWidth += element;
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} else if (component instanceof Tag && ((Tag) component).getName().equalsIgnoreCase("select")) {
				// If it's a HTML select field, I will get only the selected
				// option
				final Tag select = (Tag) component;
				Tag opt;
				tm.checkTag(select);
				final Iterator opts = select.tags();
				boolean selected = false;
				while (opts.hasNext()) {
					opt = (Tag) opts.next();
					if (opt.getPropertyValue("selected") != null) {
						tm.checkTag(opt);
						cellWidth = PDFTable.extractVisibleComponents(opt, cell, paragraph, cellWidth);
						selected = true;
						break;
					}
				}
				if (!selected) {
					// if none option have the selected attribute the first will
					// be shown
					opt = select.getFirstTag("option");
					tm.checkTag(opt);
					cellWidth = PDFTable.extractVisibleComponents(opt, cell, paragraph, cellWidth);
				}
			} else {
				// If it's an another tag, check the name and call this method
				// again
				tm.checkTag((Tag) component);
				cellWidth = PDFTable.extractVisibleComponents((Tag) component, cell, paragraph, cellWidth);
				tm.back();
			}
		}
		return cellWidth;
	}

	private static PdfPCell createCell(final Tag ref) {
		final PdfPCell cell = new PdfPCell();
		cell.setBorderWidth(ref.getPropertyValue(PDFTable.CELLSPACING) == null ? 1 : Integer.parseInt(ref
				.getPropertyValue(PDFTable.CELLSPACING)));
		cell.setPadding(ref.getPropertyValue(PDFTable.CELLPADDING) == null ? 1 : Integer.parseInt(ref
				.getPropertyValue(PDFTable.CELLPADDING)));
		cell.setColspan(Integer.parseInt(ref.getPropertyValue(PDFTable.COLSPAN) == null ? "0" : ref.getPropertyValue(PDFTable.COLSPAN)));
		cell.setNoWrap(ref.getPropertyValue("nowrap") != null);
		return cell;
	}

	private static int checkRowspanOrColspan(final Tag row, final String which) {
		// Returns the major rowspan
		final Iterator cells = row.tags();
		int rowspan = 0, next;
		Tag cell;
		while (cells.hasNext()) {
			cell = (Tag) cells.next();
			next = cell.getPropertyValue(which) == null ? 0 : Integer.parseInt(cell.getPropertyValue(which));
			if (rowspan < next) {
				rowspan = next;
			}
		}
		return rowspan;
	}

	public float[] getWidths() {
		return relativeWidths;
	}

}
/**
 * 
 * $Log: PDFTable.java,v $
 * Revision 1.3  2009/08/05 19:02:03  kpf
 * Esta atualização resolve o seuinte problema:
 *
 * - O brasão não está aparecendo quando se pede para visualizar a impressão de um memorando recém criado.
 *
 * Revision 1.2  2009/07/30 14:43:36  kpf
 * Mudança de pacote: itext v.1.4 para itext v 2.1.5.
 *
 * Alterações para suportar a nova  versão do text 2.1.5
 *
 * Revision 1.1  2007/12/26 15:57:41  tah
 * *** empty log message ***
 *
 * Revision 1.10  2007/06/29 16:20:45  tah
 * *** empty log message ***
 *
 * Revision 1.9  2006/12/12 20:23:27  tah
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/20 15:48:13  tah
 * *** empty log message ***
 *
 * Revision 1.7  2006/08/04 17:30:54  tah
 * *** empty log message ***
 *
 * Revision 1.6  2006/07/05 16:00:47  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.5  2006/05/23 19:35:06  tah
 * *** empty log message ***
 *
 * Revision 1.4  2006/05/22 19:29:49  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/04/11 19:43:46  tah
 * *** empty log message ***
 * Revision 1.2 2006/04/05 17:55:53 tah *** empty log
 * message ***
 * 
 * Revision 1.1 2006/04/03 21:30:42 tah Utilizando o nheengatu Revision 1.46
 * 2006/01/06 15:49:38 aryjr <thead> and rowspan together OK!!!
 * 
 * Revision 1.45 2006/01/06 14:13:19 aryjr Cell widths OK!!!
 * 
 * Revision 1.44 2006/01/06 13:02:18 aryjr ROWSPAN working OK. Now I will solve
 * the cell widths problem.
 * 
 * Revision 1.43 2006/01/06 03:28:33 aryjr ROWSPAN!!!
 * 
 * Revision 1.42 2006/01/05 15:29:22 aryjr Working with ROWSPAN again!!!
 * 
 * Revision 1.41 2006/01/04 01:21:37 aryjr Bugs
 * 
 * Revision 1.40 2006/01/03 15:38:39 aryjr Fixing many bugs.
 * 
 * Revision 1.39 2006/01/03 13:37:13 aryjr Rowspan OK!!! I think... :-P
 * 
 * Revision 1.38 2006/01/03 03:24:49 aryjr *** empty log message ***
 * 
 * Revision 1.37 2006/01/02 15:24:23 aryjr ROWSPAN!!!!!
 * 
 * Revision 1.36 2006/01/02 02:44:52 aryjr ROWSPAN!!!
 * 
 * Revision 1.35 2006/01/01 13:37:17 aryjr Rowspan is almost finished.
 * 
 * Revision 1.34 2005/12/31 19:24:31 aryjr Rowspan is almost ready.
 * 
 * Revision 1.33 2005/12/30 20:39:32 aryjr Rowspan!!!!
 * 
 * Revision 1.32 2005/12/22 20:28:11 aryjr Working with the "rowspan" support.
 * 
 * Revision 1.31 2005/12/22 15:15:19 aryjr Fixed many bugs.
 * 
 * Revision 1.30 2005/12/21 15:00:28 aryjr The "br" HTML tag support is OK.
 * 
 * Revision 1.29 2005/12/21 13:34:55 aryjr Nested tables OK and nowrap not OK
 * yet.
 * 
 * Revision 1.28 2005/12/21 13:29:20 aryjr Nested table OK! The new problem is
 * the "nowrap" on cells.
 * 
 * Revision 1.27 2005/12/20 15:11:13 aryjr Nested tables!!!
 * 
 * Revision 1.26 2005/12/20 13:26:20 aryjr Fixed a bug with rowspan.
 * 
 * Revision 1.25 2005/12/19 15:17:07 aryjr Breaking lines when Images and text
 * are inside the same paragraphs not yet solved.
 * 
 * Revision 1.24 2005/12/19 12:34:46 aryjr Fixed a bug related with the cell
 * width.
 * 
 * Revision 1.23 2005/12/16 20:38:12 aryjr Cell widths without rowspan OK!!!
 * 
 * Revision 1.22 2005/12/16 14:07:39 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.21 2005/12/16 14:06:31 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.20 2005/12/15 14:09:15 aryjr Cell heights.
 * 
 * Revision 1.19 2005/12/15 13:42:29 aryjr Back to 1.16 version of
 * PDFTable.java.
 * 
 * Revision 1.18 2005/12/15 12:12:12 aryjr Correcting many bugs.
 * 
 * Revision 1.17 2005/12/10 13:53:20 aryjr *** empty log message ***
 * 
 * Revision 1.16 2005/12/09 20:42:22 aryjr Cell heights!!!
 * 
 * Revision 1.15 2005/12/08 14:02:54 aryjr Fighting with the cell height!!!
 * 
 * Revision 1.14 2005/12/07 17:34:01 aryjr Fighting with the cell height!!!
 * 
 * Revision 1.13 2005/12/07 16:24:30 aryjr Working with the select HTML form
 * field.
 * 
 * Revision 1.12 2005/12/07 14:49:50 aryjr Bugs with the relatorio.jsp HTML
 * code.
 * 
 * Revision 1.11 2005/12/07 00:48:09 aryjr *** empty log message ***
 * 
 * Revision 1.10 2005/12/01 14:34:17 aryjr Problems with inner tables.
 * 
 * Revision 1.9 2005/11/30 15:42:45 aryjr Problems with inner tables.
 * 
 * Revision 1.8 2005/11/25 18:18:41 aryjr Page break OK!!!
 * 
 * Revision 1.7 2005/11/22 14:51:59 aryjr Problemas com os formularios do site
 * assetline.
 * 
 * Revision 1.6 2005/11/18 18:53:57 aryjr Rowspan OK!!! By now :-).
 * 
 * Revision 1.5 2005/11/18 15:10:53 aryjr Problems with rowspan. Revision 1.4
 * 2005/11/17 14:49:30 aryjr Cell widths OK!!! By now :-P
 * 
 * Revision 1.3 2005/11/16 15:37:13 aryjr Larguras das celulas.
 * 
 * Revision 1.2 2005/11/14 14:37:34 aryjr Grouping components inside a div tag.
 * 
 * Revision 1.1 2005/11/14 12:17:30 aryjr Renomeando os pacotes.
 * 
 * Revision 1.2 2005/09/26 19:41:13 aryjr Aproveitando a greve para voltar a
 * atividade.
 * 
 * Revision 1.1 2005/09/10 23:43:40 aryjr Passando para o java.net.
 * 
 * Revision 1.12 2005/07/02 01:18:56 aryjunior Site do projeto.
 * 
 * Revision 1.11 2005/06/11 17:40:33 aryjunior Larguras das celulas OK!!!
 * 
 * Revision 1.10 2005/06/05 04:21:17 aryjunior Larguras das celulas +- OK!!! O
 * snapshot ainda esta com problemas.
 * 
 * Revision 1.9 2005/06/05 00:20:31 aryjunior Tentando melhorar o algoritmo de
 * criacao da tabela,
 * 
 * Revision 1.8 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.7 2005/06/04 04:29:55 aryjunior Largura das tabelas e celulas,
 * ainda nao terminei.
 * 
 * Revision 1.6 2005/06/04 02:24:35 aryjunior Testes com o snapshot. Um .jsp com
 * um HTML mais complexo.
 * 
 * Revision 1.5 2005/05/30 05:28:48 aryjunior Ajustando alguns javadocs.
 * 
 * Revision 1.4 2005/05/30 05:08:25 aryjunior Testes com tabelas aninhadas e
 * cellspacing OK!!!
 * 
 * Revision 1.3 2005/05/30 01:55:56 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:32 aryjunior Initial import.
 * 
 */
