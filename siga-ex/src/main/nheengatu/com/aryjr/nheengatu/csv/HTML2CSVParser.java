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

import java.io.IOException;
import java.io.OutputStream;

import com.aryjr.nheengatu.html.HTML2MParser;
import com.aryjr.nheengatu.html.HTMLHandler;

/**
 * 
 * A HTML parser that can create the Nhengatu objects and export to a CSV (Comma
 * Separated Values) file based in a HTML table.
 * 
 * @version $Id: HTML2CSVParser.java,v 1.1 2007/12/26 15:57:42 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class HTML2CSVParser extends HTML2MParser {
	/**
	 * <code>csvFile</code>, csv file generated.
	 */
	private OutputStream csvFile;

	@Override
	public void parse() {
		final HTMLHandler handler = new HTMLHandler(documentPath, documentName);
		parse(handler, documentFile);
		CSVDocument csv = new CSVDocument(documentName);
		csv.setPath(documentPath);
		csv.setTable(handler.getHtml().getBody().getFirstTag("table"));
		try {
			csv.generateFile(csvFile);
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
		csv = null;
	}

	/**
	 * @param csvFile
	 *            The csvFile to set.
	 */
	public void setCsvFile(final OutputStream csvFile) {
		this.csvFile = csvFile;
	}

	/**
	 * @return Returns the csvFile.
	 */
	public OutputStream getCsvFile() {
		return csvFile;
	}

}
/**
 * 
 * $Log: HTML2CSVParser.java,v $
 * Revision 1.1  2007/12/26 15:57:42  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:51  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:51  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:45 tah Utilizando
 * o nheengatu
 * 
 * Revision 1.4 2006/01/03 11:58:41 aryjr Writing files on output stream working
 * with CSV files too.
 * 
 * Revision 1.3 2006/01/01 13:45:33 aryjr Feliz 2006!!!
 * 
 * Revision 1.2 2005/12/16 14:06:33 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.1 2005/12/05 13:45:06 aryjr CSV file support.
 * 
 */
