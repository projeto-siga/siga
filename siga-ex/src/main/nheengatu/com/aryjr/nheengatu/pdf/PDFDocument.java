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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aryjr.nheengatu.css2.Style;
import com.aryjr.nheengatu.css2.StyleSheet;
import com.aryjr.nheengatu.html.Tag;
import com.aryjr.nheengatu.html.Text;
import com.aryjr.nheengatu.util.GraphicsState;
import com.aryjr.nheengatu.util.TagsManager;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * Can generate a PDF document.<br>
 * See more at <a
 * href="http://www.iso.org/iso/en/CatalogueDetailPage.CatalogueDetail?CSNUMBER=16387"
 * target="_blank">http://www.iso.org/iso/en/CatalogueDetailPage.CatalogueDetail?CSNUMBER=16387</a><br>
 * And more at <a href="http://www.w3.org/MarkUp/SGML/sgml-lex/sgml-lex (HTML is
 * an SGML application)"
 * target="_blank">http://www.w3.org/MarkUp/SGML/sgml-lex/sgml-lex (HTML is an
 * SGML application)</a><br>
 * 
 * @version $Id: PDFDocument.java,v 1.4 2009/07/30 14:43:36 kpf Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class PDFDocument extends com.aryjr.nheengatu.document.Document {
	private String title = "New PDF Document";

	private Tag body;

	private Tag head;

	private Tag foot;

	private Tag headFirstPage;

	private Tag footFirstPage;

	private StyleSheet styleSheet;

	public PDFDocument() {
		setName(getName() + ".pdf");
	}

	/**
	 * 
	 * Create a PDF Document..
	 * 
	 * @param name
	 *            The name without the extencion ".pdf".
	 */
	public PDFDocument(final String name) {
		setName(name + ".pdf");
	}

	/**
	 * 
	 * Create a PDF Document.
	 * 
	 * @param path
	 *            The file's path on the filesystem.
	 * @param name
	 *            The name without the extencion ".pdf".
	 */
	public PDFDocument(final String path, final String name) {
		setPath(path);
		setName(name + ".pdf");
	}

	/**
	 * 
	 * Sets the document's title.
	 * 
	 * @param title
	 *            The document's title on the filesystem.
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * 
	 * Sets the document's body.
	 * 
	 * @param body
	 *            The Body object..
	 */
	public void setBody(final Tag body) {
		this.body = body;
	}

	/**
	 * 
	 * Gets the document's title.
	 * 
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 
	 * Gets the document's body.
	 * 
	 */
	public Tag getBody() {
		if (body == null) {
			body = new Tag("body");
		}
		return this.body;
	}

	/**
	 * 
	 * Generate a PDF file with the contents of the Body object defined.
	 * 
	 */
	public void generateFile() throws IOException {
		this.generateFile(new FileOutputStream(getPath() + getName()));
	}

	/**
	 * 
	 * Generate a PDF file with the contents of the Body object defined and
	 * OutputStream.
	 * 
	 */

	private static final float CM_UNIT = 72.0f / 2.54f;

	private float styleMeasure(String s, String sDefault) {
		if (s == null)
			s = sDefault;
		s = s.toLowerCase().trim();
		if (s.endsWith("cm")) {
			return Float.parseFloat(s.substring(0, s.length() - 2)) * CM_UNIT;
		} else {
			return Float.parseFloat(s);
		}
	}

	public void generateFile(final OutputStream out) throws IOException {
		final Document document;
		if (styleSheet == null || styleSheet.getStyles() == null
				|| !styleSheet.getStyles().containsKey("@page")) {
			document = new Document(PageSize.A4);
			document.setMargins(3.0f * PDFDocument.CM_UNIT,
					2.0f * PDFDocument.CM_UNIT, 1.0f * PDFDocument.CM_UNIT,
					2.0f * PDFDocument.CM_UNIT);
//			System.out.println("Processamento: terminou setMargins");
		} else {
			Style style = ((Style) (styleSheet.getStyles().get("@page")));
			if ("landscape".equals(style.getPropertyValue("size"))) {
				document = new Document(PageSize.A4.rotate());
			} else {
				document = new Document(PageSize.A4);
			}
			String s = style.getPropertyValue("margin-left");
			Float f = styleMeasure(s, "3cm");
			document
					.setMargins(styleMeasure(style
							.getPropertyValue("margin-left"), "3cm"),
							styleMeasure(
									style.getPropertyValue("margin-right"),
									"2cm"), styleMeasure(style
									.getPropertyValue("margin-top"), "1cm"),
							styleMeasure(style
									.getPropertyValue("margin-bottom"), "2cm"));
//			System.out.println("Processamento: terminou setMargins do else");
		}
		try {
			final PdfWriter writer = PdfWriter.getInstance(document, out);
			writer.setPageEvent(new PDFPageBreak(writer, document,
					headFirstPage, footFirstPage, head, foot));
			document.open();
			final MultiColumnText mct = new MultiColumnText();
			// set up 3 even columns with 10pt space between
			mct.addRegularColumns(document.left(), document.right(), 0f, 1);

//			System.out
//					.println("Processamento: prestes a extract visible components");

			// Extracting the document content
			extractVisibleComponents(body, document, mct, null, null);

			document.add(mct);
			document.close();
		} catch (final DocumentException de) {
			System.err.println(de.getMessage());
		}
	}

	private static Log log = LogFactory.getLog(PDFDocument.class);

	private void extractVisibleComponents(final Tag tag, final Document doc,
			final MultiColumnText mct, final Paragraph paragraph,
			final List list) throws DocumentException {
		final Iterator tags = tag.tags();
		Object component;
		Image image;
		PDFTable table;
		final TagsManager tm = TagsManager.getInstance();

		PDFDocument.log.info("extractVisibleComponents");
		// PDFDocument.log.info(tm.states.size());
		// PDFDocument.log.info(tm.getTextIndent());
		// if (paragraph != null)
		// PDFDocument.log.info(paragraph.getFirstLineIndent());

		while (tags.hasNext()) {
			component = tags.next();
			if (component instanceof Text) {
//				System.out
//						.println("Processamento: Iniciou while -> if instanceof text");
				String s = ((Text) component).getText();
				if (s.contains("\\\"")) {
					s = s.replace("\\\"", "\"");
					((Text) component).setText(s);
				}
				PDFDocument.log.info("text: " + ((Text) component).getText());
				// PDFDocument.log.info(tm.states.size());
				// PDFDocument.log.info(tm.getTextIndent());
				// PDFDocument.log.info(tm.getSpacingBefore());
				// PDFDocument.log.info(tm.getSpacingAfter());

				// If it's a text, create a iText text component for it
				if (paragraph != null)
					paragraph.add(PDFText.createChunk((Text) component));
				else if (list != null)
					list.add(PDFText.createParagraph((Text) component, tm));
				else
					mct.addElement(PDFText
							.createParagraph((Text) component, tm));
//				System.out
//						.println("Processamento: terminou while -> if instanceof text");
			} else if (component instanceof Tag
					&& ((Tag) component).getName().equalsIgnoreCase("br")) {
				// PDFDocument.log.info("br");
				// PDFDocument.log.info(tm.states.size());
				// PDFDocument.log.info(tm.getTextIndent());

				// If it's a HTML line break
				if (paragraph == null) {
					mct.addElement(new Paragraph("\n"));
				} else {
					paragraph.add("\n");
				}
//				System.out
//						.println("Processamento: Iniciou while -> if instanceof tag br");
			} else if (component instanceof Tag
					&& ((Tag) component).getName().equalsIgnoreCase("p")) {
				// If it's a HTML paragraph, create a iText paragraph for it

				tm.checkTag((Tag) component);
				final Paragraph p = PDFText.createParagraph(null, tm);

				PDFDocument.log.info("p");
				PDFDocument.log.info(tm.getFont().getSize());
				PDFDocument.log.info(p.getLeading());
				// PDFDocument.log.info(tm.states.size());
				// PDFDocument.log.info(tm.getTextIndent());
				// PDFDocument.log.info("align:");
				// PDFDocument.log.info(((Tag)
				// component).getPropertyValue("align"));
				// PDFDocument.log.info(tm.getAlign());

				// Paragraph p = new Paragraph();
				// p.setAlignment(tm.getAlign());
				// p.setKeepTogether(true);
				// // float b = tm.getSpacingBefore();
				// // float a = tm.getSpacingAfter();
				// p.setSpacingBefore(tm.getSpacingBefore());
				// p.setSpacingAfter(tm.getSpacingAfter());
				// p.setFirstLineIndent(tm.getTextIndent());
				extractVisibleComponents((Tag) component, doc, mct, p, list);
				if (paragraph != null)
					paragraph.add(p);
				else
					mct.addElement(p);

				// String align = ((Tag) component).getPropertyValue("align");
				// if (align != null) {
				// p.setAlignment(align.toLowerCase());
				// }
				tm.back();
//				System.out
//						.println("Processamento: Iniciou while -> if instanceof tag p");
			} else if (component instanceof Tag
					&& ((Tag) component).getName().equalsIgnoreCase("ol")) {
				// If it's a HTML paragraph, create a iText paragraph for it
				tm.checkTag((Tag) component);
				if (tm.getListStyleType() == null) {
					((GraphicsState) tm.states.get(tm.states.size() - 1))
							.setListStyleType("upper-roman");
				} else if (tm.getListStyleType().equals("upper-roman")) {
					((GraphicsState) tm.states.get(tm.states.size() - 1))
							.setListStyleType("lower-alpha");
				}
				final List l = new RomanList(tm.getListStyleType(), 30);
				if (list != null)
					list.add(l);
				else
					mct.addElement(l);
				extractVisibleComponents((Tag) component, doc, mct, null, l);
				tm.back();
//				System.out
//						.println("Processamento: Iniciou while -> if instanceof tag ol");
			} else if (component instanceof Tag
					&& ((Tag) component).getName().equalsIgnoreCase("ul")) {
				// If it's a HTML paragraph, create a iText paragraph for it
				final List l = new List(false, false, 20.0f);
				tm.checkTag((Tag) component);
				if (paragraph != null)
					paragraph.add(l);
				else
					mct.addElement(l);
				extractVisibleComponents((Tag) component, doc, mct, null, l);
				tm.back();
//				System.out
//						.println("Processamento: Iniciou while -> if instanceof tag ul");
			} else if (component instanceof Tag
					&& ((Tag) component).getName().equalsIgnoreCase("li")) {
				// If it's a HTML paragraph, create a iText paragraph for it
				final ListItem li = new ListItem(tm.getFont().getSize() * 1.25f);
				li.setSpacingAfter(tm.getFont().getSize() * 0.5f);

				PDFDocument.log.info("li");
				PDFDocument.log.info(tm.getFont().getSize());
				PDFDocument.log.info(li.getLeading());

				tm.checkTag((Tag) component);
				if (list == null)
					mct.addElement(li);
				else
					list.add(li);
				extractVisibleComponents((Tag) component, doc, mct, li, list);
				tm.back();
			} else if (component instanceof Tag
					&& ((Tag) component).getName().equalsIgnoreCase("img")) {
				// If it's a HTML image, create a iText image component for it
				try {
					// TODO the image path can't be static
					image = PDFImage.createImage((Tag) component);
					if (paragraph == null) {
						mct.addElement(image);
					} else {
						paragraph.add(image);
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
//				System.out
//						.println("Processamento: Iniciou while -> if instanceof tag img");
			} else if (component instanceof Tag
					&& ((Tag) component).getName().equalsIgnoreCase("table")) {
				// If it's a HTML table, create a iText table component for it
				try {
					table = PDFTable.createTable((Tag) component);
					mct.addElement(table);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} else if (component instanceof Tag
					&& ((Tag) component).getName().equalsIgnoreCase("div")) {
				final String s = ((Tag) component).getPropertyValue("style");
				if (s != null && s.equals("PAGE-BREAK-AFTER: always")) {
					doc.add(mct);
					mct.nextColumn();
				}
				tm.checkTag((Tag) component);
				extractVisibleComponents((Tag) component, doc, mct, paragraph,
						list);
				tm.back();
//				System.out
//						.println("Processamento: Iniciou while -> if instanceof tag div");
			} else {
				// If it's an another tag, check the name and call this method
				// again

				// PDFDocument.log.info("other!");
				// PDFDocument.log.info(tm.states.size());
				// PDFDocument.log.info(tm.getTextIndent());

				tm.checkTag((Tag) component);
				extractVisibleComponents((Tag) component, doc, mct, paragraph,
						list);
				tm.back();
//				System.out.println("Processamento: Iniciou while -> else if");
			}
		}
	}

	/**
	 * @param head
	 *            The head to set.
	 */
	public void setHead(final Tag head) {
		this.head = head;
	}

	/**
	 * @param foot
	 *            The foot to set.
	 */
	public void setFoot(final Tag foot) {
		this.foot = foot;
	}

	public Tag getFootFirstPage() {
		return footFirstPage;
	}

	public void setFootFirstPage(final Tag footFirstPage) {
		this.footFirstPage = footFirstPage;
	}

	public Tag getHeadFirstPage() {
		return headFirstPage;
	}

	public void setHeadFirstPage(final Tag headFirstPage) {
		this.headFirstPage = headFirstPage;
	}

	public void setStyleSheet(StyleSheet ss) {
		this.styleSheet = ss;
	}

	public StyleSheet getStyleSheet() {
		return styleSheet;
	}

}
/**
 * 
 * $Log: PDFDocument.java,v $
 * Revision 1.4  2009/07/30 14:43:36  kpf
 * Mudançaa de pacote: itext v.1.4 para itext v 2.1.5.
 *
 * Alterações para suportar a nova  versão do text 2.1.5
 *
 * Revision 1.3  2009/07/06 19:32:37  dud
 * *** empty log message ***
 * Revision 1.2 2009/04/03 19:36:16 eeh *** empty log
 * message ***
 * 
 * Revision 1.1 2007/12/26 15:57:41 tah *** empty log message ***
 * 
 * Revision 1.16 2007/10/15 16:05:57 tah *** empty log message ***
 * 
 * Revision 1.15 2006/12/12 20:23:27 tah *** empty log message *** Revision 1.14
 * 2006/10/24 18:02:38 tah *** empty log message *** Revision 1.13 2006/10/20
 * 15:48:13 tah *** empty log message ***
 * 
 * Revision 1.12 2006/08/25 16:48:23 tah *** empty log message ***
 * 
 * Revision 1.11 2006/07/19 21:40:40 tah *** empty log message ***
 * 
 * Revision 1.10 2006/07/18 16:22:42 tah *** empty log message *** Revision 1.9
 * 2006/07/10 18:33:25 tah *** empty log message ***
 * 
 * Revision 1.8 2006/07/06 15:45:26 tah *** empty log message ***
 * 
 * Revision 1.7 2006/07/05 16:00:47 nts Refatorando para melhorar qualidade do
 * código
 * 
 * Revision 1.6 2006/07/04 20:29:39 tah *** empty log message *** Revision 1.5
 * 2006/05/23 19:35:06 tah *** empty log message ***
 * 
 * Revision 1.4 2006/05/22 19:29:49 tah *** empty log message ***
 * 
 * Revision 1.3 2006/05/11 20:30:23 tah *** empty log message *** Revision 1.2
 * 2006/04/11 19:43:46 tah *** empty log message *** Revision 1.1 2006/04/03
 * 21:30:42 tah Utilizando o nheengatu Revision 1.20 2006/01/06 03:28:32 aryjr
 * ROWSPAN!!!
 * 
 * Revision 1.19 2006/01/05 15:29:22 aryjr Working with ROWSPAN again!!!
 * 
 * Revision 1.18 2006/01/02 18:27:27 neyanderson Adaptando para geração do
 * arquivo direto no OutputStream.
 * 
 * Revision 1.17 2006/01/01 13:45:32 aryjr Feliz 2006!!!
 * 
 * Revision 1.16 2005/12/30 20:39:32 aryjr Rowspan!!!!
 * 
 * Revision 1.15 2005/12/22 20:28:11 aryjr Working with the "rowspan" support.
 * 
 * Revision 1.14 2005/12/22 15:15:18 aryjr Fixed many bugs.
 * 
 * Revision 1.13 2005/12/21 15:00:28 aryjr The "br" HTML tag support is OK.
 * 
 * Revision 1.12 2005/12/19 15:17:06 aryjr Breaking lines when Images and text
 * are inside the same paragraphs not yet solved.
 * 
 * Revision 1.11 2005/12/16 14:06:31 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.10 2005/12/15 12:19:50 aryjr Removing space after components in a
 * PDF document body.
 * 
 * Revision 1.9 2005/12/07 14:49:49 aryjr Bugs with the relatorio.jsp HTML code.
 * 
 * Revision 1.8 2005/12/07 11:41:10 aryjr Problems with the foot and head files
 * inside a .war file.
 * 
 * Revision 1.7 2005/12/07 00:48:08 aryjr *** empty log message ***
 * 
 * Revision 1.6 2005/12/05 13:45:04 aryjr CSV file support.
 * 
 * Revision 1.5 2005/11/25 15:10:29 aryjr Head and foot on page break.
 * 
 * Revision 1.4 2005/11/24 14:17:53 aryjr Head and foot of PDF documents.
 * 
 * Revision 1.3 2005/11/14 14:37:34 aryjr Grouping components inside a div tag.
 * 
 * Revision 1.2 2005/11/14 13:22:59 aryjr CSS Parser ok from now.
 * 
 * Revision 1.1 2005/11/14 12:17:30 aryjr Renomeando os pacotes.
 * 
 * Revision 1.2 2005/09/26 19:41:13 aryjr Aproveitando a greve para voltar a
 * atividade.
 * 
 * Revision 1.1 2005/09/10 23:43:40 aryjr Passando para o java.net.
 * 
 * Revision 1.8 2005/07/02 01:18:56 aryjunior Site do projeto.
 * 
 * Revision 1.7 2005/06/20 03:28:36 aryjunior Iniciando a geracao de documentos
 * do Writer do OpenOffice.
 * 
 * Revision 1.6 2005/06/19 14:16:33 aryjunior Preparando o suporte ao
 * OpenOffice.
 * 
 * Revision 1.5 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.4 2005/05/30 05:24:56 aryjunior Espacamento apos cada item dentro
 * do "body" com valor 20.
 * 
 * Revision 1.3 2005/05/30 01:55:56 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:32 aryjunior Initial import.
 * 
 */
