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
package com.aryjr.nheengatu.csv;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import com.aryjr.nheengatu.html.Tag;

/**
 * 
 * Can generate a CSV (Comma Separated Values) document based in a HTML table.
 * See more at
 * http://www.iso.org/iso/en/CatalogueDetailPage.CatalogueDetail?CSNUMBER=16387
 * And more at http://www.w3.org/MarkUp/SGML/sgml-lex/sgml-lex (HTML is an SGML
 * application)
 * 
 * @version $Id: CSVDocument.java,v 1.1 2007/12/26 15:57:42 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class CSVDocument extends com.aryjr.nheengatu.document.Document {
	private Tag table;

	public CSVDocument() {
		setName(getName() + ".csv");
	}

	/**
	 * 
	 * Create a CSV (Comma Separated Values) Document..
	 * 
	 * @param name
	 *            The name without the extencion ".csv".
	 */
	public CSVDocument(final String name) {
		setName(name + ".csv");
	}

	/**
	 * 
	 * Create a CSV (Comma Separated Values) Document.
	 * 
	 * @param path
	 *            The file's path on the filesystem.
	 * @param name
	 *            The name without the extencion ".csv".
	 */
	public CSVDocument(final String path, final String name) {
		setPath(path);
		setName(name + ".csv");
	}

	/**
	 * 
	 * Generate a PDF file with the contents of the Body object defined and
	 * OutputStream.
	 * 
	 */
	public void generateFile(final OutputStream out) throws IOException {
		try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(out))) {
			if (table != null && table.getName().equalsIgnoreCase("table")) {
				if ((table.getFirstTag("thead") == null && table.getFirstTag("tbody") != null)
						|| (table.getFirstTag("tbody") == null && table.getFirstTag("thead") != null)) {
					output.write("Please, thead requires tbody and vice-versa.");
				} else {
					if (table.getFirstTag("thead") == null) {
						// Only tr tags on table.
						writeLines(table.tags(), output);
					} else {
						// Table with thead and tbody tag.
						writeLines(table.getFirstTag("thead").tags(), output);
						writeLines(table.getFirstTag("tbody").tags(), output);
					}
				}
			} else {
				output.write("Only HTML tables can be converted to CSV files.");
			}
		}
	}

	/**
	 * 
	 * Generate a PDF file with the contents of the Body object defined.
	 * 
	 */
	public void generateFile() throws IOException {
		this.generateFile(new FileOutputStream(getPath() + getName()));
	}

	private void writeLines(final Iterator rows, final BufferedWriter output) throws IOException {
		Iterator cells;
		Tag cell;
		final StringBuffer line = new StringBuffer();
		while (rows.hasNext()) {
			cells = ((Tag) rows.next()).tags();
			while (cells.hasNext()) {
				cell = (Tag) cells.next();
				line.append(cell.getText() + ";");
			}
			output.write(line.toString() + "\n");
			line.setLength(0);
		}
	}

	/**
	 * @param table
	 *            The table to set.
	 */
	public void setTable(final Tag table) {
		this.table = table;
	}

	/**
	 * @return Returns the table.
	 */
	public Tag getTable() {
		return table;
	}

}
/**
 * 
 * $Log: CSVDocument.java,v $
 * Revision 1.1  2007/12/26 15:57:42  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:51  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:51  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:45 tah Utilizando o
 * nheengatu
 * 
 * Revision 1.4 2006/01/03 11:58:41 aryjr Writing files on output stream working
 * with CSV files too.
 * 
 * Revision 1.3 2006/01/01 13:45:33 aryjr Feliz 2006!!!
 * 
 * Revision 1.2 2005/12/16 14:06:33 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.1 2005/12/05 13:45:05 aryjr CSV file support.
 * 
 * 
 */
