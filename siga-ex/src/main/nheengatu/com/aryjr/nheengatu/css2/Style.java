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
package com.aryjr.nheengatu.css2;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.aryjr.nheengatu.util.HexadecimalColor;

/**
 * 
 * A "style" property of a tag.<br>
 * <br>
 * See more at: <a
 * href="http://www.w3.org/TR/1998/REC-CSS2-19980512/Overview.html"
 * target="htmlspec">http://www.w3.org/TR/1998/REC-CSS2-19980512/Overview.html</a>
 * 
 * @version $Id: Style.java,v 1.1 2007/12/26 15:57:42 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class Style {
	/**
	 * 
	 * The default font size is 9.
	 */
	public static final int DEFAULT_FONT_SIZE = 11;

	/**
	 * 
	 * The default font name is "TimesRoman".
	 */
	public static final String DEFAULT_FONT_FAMILY = "Arial";

	/**
	 * 
	 * The default font style is "normal".
	 */
	public static final String DEFAULT_FONT_STYLE = "normal";

	/**
	 * 
	 * The default font color is black.
	 */
	public static final Color DEFAULT_FONT_COLOR = new Color(0, 0, 0);

	private final Hashtable properties = new Hashtable();

	private String name;

	public Style() {
		properties.put("color", HexadecimalColor.color2Hexa(Style.DEFAULT_FONT_COLOR));
		properties.put("font-size", String.valueOf(Style.DEFAULT_FONT_SIZE));
		properties.put("font-family", Style.DEFAULT_FONT_FAMILY);
		properties.put("font-style", Style.DEFAULT_FONT_STYLE);
	}

	/**
	 * 
	 * Constructor with the properties values and a name for this style.
	 * 
	 * @param rule
	 *            A css rule from the cssparser package.
	 */
	public Style(final String name, final String properties) {
		setName(name);
		setProperties(properties);
	}

	/**
	 * 
	 * Constructor with the properties values.
	 * 
	 * @param properties
	 *            For instance: "font-size:15; font-family:verdana;
	 *            color:#000000; font-style:normal".
	 */
	public Style(final String properties) {
		setProperties(properties);
	}

	/**
	 * 
	 * Sets the properties values.
	 * 
	 * @param properties
	 *            For instance: "font-size:15; font-family:verdana;
	 *            color:#000000; font-style:normal".
	 */
	public void setProperties(final String properties) {
		final StringTokenizer stk = new StringTokenizer(properties, ";");
		String s = null;
		while (stk.hasMoreTokens()) {
			s = stk.nextToken();
			if (s!=null && s.trim().length() >0){
				setProperty(s.substring(0, s.indexOf(':')).trim(), s.substring(s.indexOf(':') + 1, s.length()).trim());	
			}
			
		}
	}

	/**
	 * 
	 * Sets a property value.
	 * 
	 * @param name
	 *            For instance: "font-size".
	 * @param value
	 *            For instance: "15".
	 */
	public void setProperty(final String name, final String value) {
		properties.put(name.toLowerCase().trim(), value.toLowerCase().trim());
	}

	/**
	 * 
	 * Returns a collection with the properties names.
	 * 
	 */
	public Enumeration getPropertiesNames() {
		return properties.keys();
	}

	/**
	 * 
	 * Returns the value of a propertie.
	 * 
	 * @param name
	 *            The property name.
	 */
	public String getPropertyValue(final String name) {
		return (String) properties.get(name);
	}

	public void setName(final String n) {
		name = n;
	}

	public String getName() {
		return name;
	}

}
/**
 * 
 * $Log: Style.java,v $
 * Revision 1.1  2007/12/26 15:57:42  tah
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/15 20:18:23  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/05 16:00:47  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.2  2006/04/11 19:43:46  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:45 tah Utilizando o
 * nheengatu
 * 
 * Revision 1.5 2006/01/01 13:45:34 aryjr Feliz 2006!!!
 * 
 * Revision 1.4 2005/12/16 14:06:34 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.3 2005/11/14 14:37:36 aryjr Grouping components inside a div tag.
 * 
 * Revision 1.2 2005/11/14 13:22:59 aryjr CSS Parser ok from now.
 * 
 * Revision 1.1 2005/11/14 12:17:39 aryjr Renomeando os pacotes.
 * 
 * Revision 1.1 2005/09/10 23:43:42 aryjr Passando para o java.net.
 * 
 * Revision 1.6 2005/08/18 01:32:34 aryjunior Publicando a contribuicao do
 * Guilherme.
 * 
 * Revision 1.4 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.3 2005/05/30 01:55:57 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:28 aryjunior Initial import.
 * 
 */
