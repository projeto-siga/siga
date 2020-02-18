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
package com.aryjr.nheengatu.html;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import com.aryjr.nheengatu.css2.Style;

/**
 * 
 * Represents a basic HTML tag.<br>
 * <br>
 * See more at: <a
 * href="http://www.w3.org/TR/1998/REC-html40-19980424/index/elements.html"
 * target="htmlspec">http://www.w3.org/TR/1998/REC-html40-19980424/index/elements.html</a>
 * 
 * @version $Id: Tag.java,v 1.1 2007/12/26 15:57:42 tah Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class Tag {
	private String name;

	private final HashMap properties = new HashMap();

	private final ArrayList tags = new ArrayList();

	private Style style;

	public Tag(final String name, final String text) {
		this.name = name;
		tags.add(new Text(text));
	}

	public Tag(final String name, final Text text) {
		this.name = name;
		tags.add(text);
	}

	public Tag(final String name, final Tag tag) {
		this.name = name;
		tags.add(tag);
	}

	public Tag(final String name) {
		this.name = name;
	}

	/**
	 * 
	 * Sets a property value.
	 * 
	 * @param name
	 *            The property's name.
	 * @param property
	 *            The property's value.
	 * 
	 */
	public void setPropertyValue(final String name, final String property) {
		if (name.equals("style")) {
			style = new Style(property);
		}
		properties.put(name, property);
	}

	/**
	 * 
	 * Returns the "style" property as a Style object.
	 * 
	 */
	public Style getStyle() {
		return style;
	}

	/**
	 * 
	 * Returns a property value.
	 * 
	 * @param name
	 *            The property's name.
	 */
	public String getPropertyValue(final String name) {
		return (String) properties.get(name);
	}

	/**
	 * 
	 * Remove a property form this tag.
	 * 
	 * @param name
	 *            The property's name.
	 */
	public void removeProperty(final String name) {
		properties.remove(name);
	}

	/**
	 * 
	 * Returns all properties names.
	 * 
	 */
	public Iterator getPropertiesNames() {
		return properties.keySet().iterator();
	}

	/**
	 * 
	 * Adds a component to this tag.
	 * 
	 * @param tag
	 *            The Component object.
	 */
	public void addTag(final Tag tag) {
		tags.add(tag);
	}

	/**
	 * 
	 * Adds a component to this tag in a specified position.
	 * 
	 * @param tag
	 *            The Component object.
	 * @param ind
	 *            The position of the Component object.
	 */
	public void addTag(final Tag tag, final int ind) {
		tags.add(ind, tag);
	}

	public void addText(final String text) {
		addText(new Text(text));
	}

	public void addText(final Text text) {
		if (tags.size() > 0 && (tags.get(tags.size() - 1) instanceof Text)) {
			Text t = (Text) tags.get(tags.size() - 1);
			String s = t.getText();
			t.setText(s + text.getText());
			return;
		}
		tags.add(text);
	}

	/**
	 * 
	 * Returns a tag by order of adiction.
	 * 
	 */
	public Tag getTag(final int ind) {
		return (Tag) tags.get(ind);
	}

	/**
	 * 
	 * Remove and returns a tag by order of adiction.
	 * 
	 */
	public Tag removeTag(final int ind) {
		return (Tag) tags.remove(ind);
	}

	/**
	 * 
	 * Returns all tags.
	 * 
	 */
	public Iterator tags() {
		return tags.iterator();
	}

	/**
	 * 
	 * Returns all tags.
	 * 
	 */
	public ArrayList tagsCollection() {
		return tags;
	}

	/**
	 * 
	 * Returns all tags.
	 * 
	 */
	public Object[] tagsArray() {
		return tags.toArray();
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	public Tag getFirstTag(final String name) {
		final Iterator it = tags.iterator();
		Object obj;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof Tag && ((Tag) obj).getName().equalsIgnoreCase(name))
				return (Tag) obj;
		}
		return null;
	}

	public Tag removeFirstTag(final String name) {
		final Iterator it = tags.iterator();
		Object obj;
		int inc = 0;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof Tag && ((Tag) obj).getName().equalsIgnoreCase(name))
				return (Tag) tags.remove(inc);
			inc++;
		}
		return null;
	}

	/**
	 * 
	 * Returns all text without any format.
	 * 
	 * @return A pure String with all text inside this tag.
	 */
	public String getText() {
		final StringBuffer text = new StringBuffer("");
		final Iterator it = tags.iterator();
		Object obj;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof Text) {
				text.append(((Text) obj).getText());
			} else if (obj instanceof Tag) {
				text.append(((Tag) obj).getText());
			}
		}
		return text.toString();
	}

	/**
	 * 
	 * Returns a copy without any nested tags.
	 */
	public Tag getEmptyCopy() {
		final Tag copy = new Tag(name);
		final Iterator pNames = properties.keySet().iterator();
		String pName;
		while (pNames.hasNext()) {
			pName = (String) pNames.next();
			copy.setPropertyValue(pName, (String) properties.get(pName));
		}
		return copy;
	}

}
/**
 * 
 * $Log: Tag.java,v $
 * Revision 1.1  2007/12/26 15:57:42  tah
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/06 15:45:26  tah
 * *** empty log message ***
 * Revision 1.3 2006/07/05 16:00:51 nts Refatorando para
 * melhorar qualidade do código
 * 
 * Revision 1.2 2006/04/11 19:43:51 tah *** empty log message *** Revision 1.1
 * 2006/04/03 21:30:45 tah Utilizando o nheengatu
 * 
 * Revision 1.7 2006/01/03 13:37:14 aryjr Rowspan OK!!! I think... :-P
 * 
 * Revision 1.6 2006/01/02 15:24:23 aryjr ROWSPAN!!!!!
 * 
 * Revision 1.5 2006/01/01 13:45:33 aryjr Feliz 2006!!!
 * 
 * Revision 1.4 2005/12/22 20:28:12 aryjr Working with the "rowspan" support.
 * 
 * Revision 1.3 2005/12/16 14:06:32 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.2 2005/12/05 13:45:08 aryjr CSV file support.
 * 
 * Revision 1.1 2005/11/14 12:17:43 aryjr Renomeando os pacotes.
 * 
 * Revision 1.1 2005/09/10 23:43:39 aryjr Passando para o java.net.
 * 
 * Revision 1.5 2005/07/02 01:18:57 aryjunior Site do projeto.
 * 
 * Revision 1.4 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.3 2005/05/30 01:55:57 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:32 aryjunior Initial import.
 * 
 * 
 */
