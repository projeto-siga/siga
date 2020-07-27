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
package com.aryjr.nheengatu.util;

import java.awt.Color;

import com.aryjr.nheengatu.css2.Style;
import com.aryjr.nheengatu.html.Tag;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;

/**
 * 
 * 
 * 
 * @version $Id: GraphicsState.java,v 1.3 2009/07/30 14:43:36 kpf Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior </a>
 * 
 */
public class GraphicsState {
	private Font font;

	private int align, valign;

	private Color bgcolor;

	private float spacingBefore, spacingAfter;

	private float textIndent;

	private float marginLeft;

	private String listStyleType;

	private static final float CM_UNIT = 72.0f / 2.54f;

	public String getListStyleType() {
		return listStyleType;
	}

	public void setListStyleType(String listStyleType) {
		this.listStyleType = listStyleType;
	}

	public float getTextIndent() {
		return textIndent;
	}

	public void setTextIndent(final float textIndent) {
		this.textIndent = textIndent;
	}

	public float getSpacingAfter() {
		return spacingAfter;
	}

	public void setSpacingAfter(final float spacingAfter) {
		this.spacingAfter = spacingAfter;
	}

	public float getSpacingBefore() {
		return spacingBefore;
	}

	public void setSpacingBefore(final float spacingBefore) {
		this.spacingBefore = spacingBefore;
	}

	public GraphicsState() {
		font = FontFactory.getFont(Style.DEFAULT_FONT_FAMILY,
				Style.DEFAULT_FONT_SIZE, Font.NORMAL, Style.DEFAULT_FONT_COLOR);
		align = Element.ALIGN_LEFT;
		valign = Element.ALIGN_MIDDLE;
		bgcolor = new Color(0, 0, 0);
		spacingBefore = 8f;
		spacingAfter = 8f;
		textIndent = 0f * GraphicsState.CM_UNIT;
		marginLeft = 0f * GraphicsState.CM_UNIT;
		listStyleType = null;
	}

	public GraphicsState(final GraphicsState last) {
		font = FontFactory.getFont(last.getFont().getFamilyname(), last
				.getFont().getCalculatedSize(), last.getFont()
				.getCalculatedStyle(), last.getFont().getColor());
		align = last.getAlign();
		valign = last.getValign();
		bgcolor = new Color(last.getBgcolor().getRed(), last.getBgcolor()
				.getGreen(), last.getBgcolor().getBlue());
		spacingBefore = last.getSpacingBefore();
		spacingAfter = last.getSpacingAfter();
		textIndent = last.getTextIndent();
		marginLeft = last.getMarginLeft();
		listStyleType = last.listStyleType;
	}

	private Float toPoints(String s) {
		if (s == null || s.equals(""))
			return null;

		// TODO Tamanho de fontes CSS aqui
		if (s.indexOf("px") > 0) {
			s = s.substring(0, s.length() - 2);
			return Float.parseFloat(s) * .75f;
		} else if (s.indexOf("pt") > 0) {
			s = s.substring(0, s.length() - 2);
			return Float.parseFloat(s);
		} else if (s.indexOf("cm") > 0) {
			s = s.substring(0, s.length() - 2);
			return Float.parseFloat(s) * GraphicsState.CM_UNIT;
		} else if (s.indexOf("%") > 0) {
			s = s.substring(0, s.length() - 1);
			return (Float.parseFloat(s) / 100f) * Style.DEFAULT_FONT_SIZE;
		}
		return Float.parseFloat(s);
	}

	public void setStyle(final Style style) {
		String pv = null;
		if (style.getPropertyValue("font-family") != null) {
			font.setFamily(style.getPropertyValue("font-family"));
		}
		pv = style.getPropertyValue("font-size");
		if (pv != null) {
			font.setSize(toPoints(pv));
		}
		String sStyle = "";
		if (style.getPropertyValue("font-style") != null) {
			sStyle = style.getPropertyValue("font-style");
		}
		if (style.getPropertyValue("font-weight") != null) {
			if (sStyle.length() != 0)
				sStyle += ",";
			sStyle += style.getPropertyValue("font-weight");
		}
		if (sStyle.length() != 0)
			font.setStyle(sStyle);
		if (style.getPropertyValue("color") != null) {
			font.setColor(HexadecimalColor.hexa2Color(style
					.getPropertyValue("color")));
		}
		if (style.getPropertyValue("text-indent") != null) {
			setTextIndent(toPoints(style.getPropertyValue("text-indent")));
		}
		if (style.getPropertyValue("margin-left") != null) {
			setMarginLeft(toPoints(style.getPropertyValue("margin-left")));
		}
		if (style.getPropertyValue("text-align") != null) {
			setAlign(style.getPropertyValue("text-align"));
		}
		if (style.getPropertyValue("list-style-type") != null) {
			setListStyleType(style.getPropertyValue("list-style-type"));
		}
	}

	public void setFont(final Tag htmlFont) {
		if (htmlFont.getPropertyValue("face") != null) {
			font.setFamily(htmlFont.getPropertyValue("face"));
		}
		if (htmlFont.getPropertyValue("size") != null) {
			final Integer i = Integer.parseInt(htmlFont
					.getPropertyValue("size"));
			Integer iSize = 10;
			if (i == 1)
				iSize = 8;
			else if (i == 2)
				iSize = 9;
			else if (i == 3)
				iSize = 10;
			else if (i == 4)
				iSize = 11;
			else if (i == 5)
				iSize = 12;
			else if (i == 6)
				iSize = 14;
			else if (i == 7)
				iSize = 16;
			// TODO check the proportion between the <font size=""... > and the
			// iText Font.
			font.setSize(iSize);
		}
		if (htmlFont.getPropertyValue("color") != null) {
			font.setColor(HexadecimalColor.hexa2Color(htmlFont
					.getPropertyValue("color")));
		}
	}

	public Font getFont() {
		return font;
	}

	public void setAlign(final int align) {
		this.align = align;
	}

	public void setAlign(final String align) {
		setAlign(GraphicsState.getiTextAlign(align));
	}

	public int getAlign() {
		return align;
	}

	public int getValign() {
		return valign;
	}

	public void setValign(final int valign) {
		this.valign = valign;
	}

	public void setValign(final String valign) {
		setValign(GraphicsState.getiTextAlign(valign));
	}

	public void setBgcolor(final String bgcolor) {
		this.bgcolor = HexadecimalColor.hexa2Color(bgcolor);
	}

	public Color getBgcolor() {
		return bgcolor;
	}

	// Mapping HTML to iText by introspection
	private static int getiTextAlign(final String align) {
		// TODO align and valign together
		try {
			if (align.equalsIgnoreCase("JUSTIFY"))
				return Element.ALIGN_JUSTIFIED;
			else
				return Element.class.getField("ALIGN_" + align.toUpperCase())
						.getInt(null);
		} catch (final Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public float getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}

}
/**
 * 
 * $Log: GraphicsState.java,v $
 * Revision 1.3  2009/07/30 14:43:36  kpf
 * Mudançaa de pacote: itext v.1.4 para itext v 2.1.5.
 *
 * Alterações para suportar a nova  versão do text 2.1.5
 *
 * Revision 1.2  2008/09/09 15:18:06  eeh
 * *** empty log message ***
 * Revision 1.1 2007/12/26 15:57:40 tah *** empty
 * log message ***
 * 
 * Revision 1.8 2007/03/06 19:55:51 tah *** empty log message ***
 * 
 * Revision 1.7 2006/11/30 19:44:05 tah *** empty log message ***
 * 
 * Revision 1.6 2006/07/18 16:22:42 tah *** empty log message ***
 * 
 * Revision 1.5 2006/07/05 16:00:44 nts Refatorando para melhorar qualidade do
 * código
 * 
 * Revision 1.4 2006/05/23 19:35:06 tah *** empty log message ***
 * 
 * Revision 1.3 2006/05/11 20:30:23 tah *** empty log message *** Revision 1.2
 * 2006/04/11 19:43:43 tah *** empty log message *** Revision 1.1 2006/04/03
 * 21:30:45 tah Utilizando o nheengatu Revision 1.6 2006/01/01 13:45:37 aryjr
 * Feliz 2006!!!
 * 
 * Revision 1.5 2005/12/16 14:06:37 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.4 2005/11/22 14:51:59 aryjr Problemas com os formularios do site
 * assetline.
 * 
 * Revision 1.3 2005/11/18 15:10:53 aryjr Problems with rowspan.
 * 
 * Revision 1.2 2005/11/16 15:37:15 aryjr Larguras das celulas. Revision 1.1
 * 2005/11/14 12:17:47 aryjr Renomeando os pacotes.
 * 
 * Revision 1.1 2005/09/10 23:43:41 aryjr Passando para o java.net.
 * 
 * Revision 1.7 2005/07/02 01:18:57 aryjunior Site do projeto.
 * 
 * Revision 1.6 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.5 2005/05/30 14:31:25 aryjunior Ha uma pendencia com relacao aos
 * tamanhos das fontes.
 * 
 * Revision 1.4 2005/05/30 05:28:48 aryjunior Ajustando alguns javadocs.
 * 
 * Revision 1.3 2005/05/30 01:55:57 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:29 aryjunior Initial import.
 * 
 */
