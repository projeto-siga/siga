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

import java.util.Iterator;

import com.aryjr.nheengatu.html.Tag;
import com.aryjr.nheengatu.html.Text;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * Get page break events from iText.
 * 
 * @version $Id: PDFPageBreak.java,v 1.2 2009/07/30 14:43:36 kpf Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class PDFPageBreak extends PdfPageEventHelper {
	private Tag headTag;

	private Tag footTag;

	private Tag headFirstPageTag;

	private Tag footFirstPageTag;

	private float topMargin;

	private float bottomMargin;

	private float headHeight = 0;

	private float footHeight = 0;

	private float headFirstPageHeight = 0;

	private float footFirstPageHeight = 0;

	public PDFPageBreak(final PdfWriter writer, final Document document, final Tag headFirstPageTag, final Tag footFirstPageTag, final Tag headTag,
			final Tag footTag) throws DocumentException {
		this.headFirstPageTag = headFirstPageTag;
		this.footFirstPageTag = footFirstPageTag;
		this.headTag = headTag;
		this.footTag = footTag;

		final Rectangle page = document.getPageSize();
		if (headTag != null) {
			final PDFTable pdft = createTable(writer, document, headTag);
			pdft.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
			headHeight = pdft.getTotalHeight();
		}
		if (footTag != null) {
			final PDFTable pdft = createTable(writer, document, footTag);
			pdft.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
			footHeight = pdft.getTotalHeight();
		}
		if (headFirstPageTag != null) {
			final PDFTable pdft = createTable(writer, document, headFirstPageTag);
			pdft.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
			headFirstPageHeight = pdft.getTotalHeight();
		}
		if (footFirstPageTag != null) {
			final PDFTable pdft = createTable(writer, document, footFirstPageTag);
			pdft.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
			footFirstPageHeight = pdft.getTotalHeight();
		}

		topMargin = document.topMargin();
		bottomMargin = document.bottomMargin();
		document.setMargins(document.leftMargin(), document.rightMargin(), topMargin + headFirstPageHeight,
				bottomMargin + footFirstPageHeight);

	}

	private void preprocessTags(final PdfWriter writer, final Document document, final Tag tag) {
		final Iterator tags = tag.tags();
		Object component;
		while (tags.hasNext()) {
			component = tags.next();
			if (component instanceof Text) {
				String s = ((Text) component).getText();
				if (-1 != s.indexOf("#pg")) {
					s = s.replace("#pg", String.valueOf(writer.getPageNumber()));
					((Text) component).changeText(s);
				}
			} else {
				preprocessTags(writer, document, (Tag) component);
			}
		}
	}

	private void restoreTags(final PdfWriter writer, final Document document, final Tag tag) {
		final Iterator tags = tag.tags();
		Object component;
		while (tags.hasNext()) {
			component = tags.next();
			if (component instanceof Text) {
				((Text) component).restoreText();
			} else {
				restoreTags(writer, document, (Tag) component);
			}
		}
	}

	public PDFTable createTable(final PdfWriter writer, final Document document, final Tag tag) throws DocumentException {
		preprocessTags(writer, document, tag);
		final PDFTable pdft = PDFTable.createTable(tag);
		restoreTags(writer, document, tag);
		return pdft;
	}

	@Override
	public void onEndPage(final PdfWriter writer, final Document document) {
		try {
			final Rectangle page = document.getPageSize();
			PDFTable head = null;
			PDFTable foot = null;

			if (writer.getPageNumber() == 1) {
				if (headFirstPageTag != null)
					head = createTable(writer, document, headFirstPageTag);
				if (footFirstPageTag != null)
					foot = createTable(writer, document, footFirstPageTag);
			} else {
				if (headTag != null)
					head = createTable(writer, document, headTag);
				if (footTag != null)
					foot = createTable(writer, document, footTag);
			}

			if (head != null) {
				head.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
				head.writeSelectedRows(0, -1, document.leftMargin(), page.getHeight() - topMargin, writer
						.getDirectContent());
			}
			if (foot != null) {
				foot.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
				foot.writeSelectedRows(0, -1, document.leftMargin(), bottomMargin + foot.getTotalHeight(), writer
						.getDirectContent());
			}
		} catch (final Exception e) {
			throw new ExceptionConverter(e);
		}

		document.setMargins(document.leftMargin(), document.rightMargin(), topMargin + headHeight, bottomMargin
				+ footHeight);
	}

}
/**
 * 
 * $Log: PDFPageBreak.java,v $
 * Revision 1.2  2009/07/30 14:43:36  kpf
 * Mudança de pacote: itext v.1.4 para itext v 2.1.5.
 *
 * Alterações para suportar a nova  versão do text 2.1.5
 *
 * Revision 1.1  2007/12/26 15:57:41  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:47  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:46  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:42 tah Utilizando o
 * nheengatu Revision 1.6 2006/01/01 13:45:32 aryjr Feliz 2006!!!
 * 
 * Revision 1.5 2005/12/20 13:26:20 aryjr Fixed a bug with rowspan.
 * 
 * Revision 1.4 2005/12/16 14:06:31 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.3 2005/12/07 00:48:08 aryjr *** empty log message ***
 * 
 * Revision 1.2 2005/11/25 18:18:41 aryjr Page break OK!!!
 * 
 * Revision 1.1 2005/11/25 15:10:28 aryjr Head and foot on page break.
 * 
 */
