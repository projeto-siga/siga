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
import java.lang.reflect.Method;
import java.util.ArrayList;
import com.aryjr.nheengatu.css2.Style;
import com.aryjr.nheengatu.css2.StyleSheet;
import com.aryjr.nheengatu.html.Tag;
import com.lowagie.text.Font;

/**
 * 
 * Receive all tags and execute the doXXX methods for each one.
 * 
 * @version $Id: TagsManager.java,v 1.2 2009/05/25 21:07:16 eeh Exp $
 * @author <a href="mailto:junior@aryjr.com">Ary Junior</a>
 * 
 */
public class TagsManager {
	private static ThreadLocal<TagsManager> instance = new ThreadLocal<TagsManager>();

	public ArrayList states = new ArrayList();

	public TagsManager() {
		states.add(new GraphicsState());
	}

	public static TagsManager getInstance() {
		// The Singleton pattern [GoF]
		if (instance.get() == null) {
			instance.set(new TagsManager());
		}
		return instance.get();
	}
	
	public GraphicsState getLastState() {
		if (states.size() == 0)
			return null;
		return (GraphicsState) states.get(states.size() - 1);
	}

	public void checkTag(final Tag tag) {
		// TODO <center> is deprecated in HTML 4!!!
		final GraphicsState state = new GraphicsState((GraphicsState) states.get(states.size() - 1));
		states.add(state);
		if (tag.getPropertyValue("bgcolor") != null) {
			state.setBgcolor(tag.getPropertyValue("bgcolor"));
		}
		if (tag.getPropertyValue("align") != null) {
			state.setAlign(tag.getPropertyValue("align"));
		}
		if (tag.getPropertyValue("valign") != null) {
			state.setValign(tag.getPropertyValue("valign"));
		}
		// TODO do not forget the text position!!!
		if (tag.getPropertyValue("style") != null) {
			state.setStyle(new Style(tag.getPropertyValue("style")));
		}
		// TODO <font> and <basefont> are deprecated in HTML 4!!!
		if (tag.getName().equalsIgnoreCase("font")) {
			state.setFont(tag);
		}
		if (tag.getName().equalsIgnoreCase("link")) {
			final StyleSheet ss = StyleSheet.getInstance();
			ss.processCSSFile(tag.getPropertyValue("href"));
		}
		if (checkTag(tag.getName())) {
			doTag(tag);
		}
	}

	public Font getFont() {
		return ((GraphicsState) states.get(states.size() - 1)).getFont();
	}

	public Color getBgcolor() {
		return ((GraphicsState) states.get(states.size() - 1)).getBgcolor();
	}

	public int getAlign() {
		return ((GraphicsState) states.get(states.size() - 1)).getAlign();
	}

	public int getValign() {
		return ((GraphicsState) states.get(states.size() - 1)).getValign();
	}

	public float getSpacingAfter() {
		// TODO Auto-generated method stub
		return ((GraphicsState) states.get(states.size() - 1)).getSpacingAfter();
	}

	public float getSpacingBefore() {
		// TODO Auto-generated method stub
		return ((GraphicsState) states.get(states.size() - 1)).getSpacingBefore();
	}

	public float getTextIndent() {
		// TODO Auto-generated method stub
		return ((GraphicsState) states.get(states.size() - 1)).getTextIndent();
	}

	public float getMarginLeft() {
		// TODO Auto-generated method stub
		return ((GraphicsState) states.get(states.size() - 1)).getMarginLeft();
	}

	public String getListStyleType() {
		// TODO Auto-generated method stub
		return ((GraphicsState) states.get(states.size() - 1)).getListStyleType();
	}

	public void back() {
		states.remove(states.size() - 1);
	}

	public GraphicsState getPreviousState(final int ind) {
		return (GraphicsState) states.get(states.size() - (ind + 1));
	}

	private boolean checkTag(final String tagName) {
		try {
			final Method[] methods = TagsManager.class.getMethods();
			for (Method element : methods) {
				if (element.getName().equalsIgnoreCase("do" + tagName.toUpperCase()))
					return true;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Doing the HTML tags
	private void doTag(final Tag tag) {
		try {
			final Method method = TagsManager.class.getMethod("do" + tag.getName().toUpperCase(), new Class[] {});
			method.invoke(this, new String[] {});
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void doB() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setStyle(Font.BOLD);
	}

	public void doSTRONG() {
		doB();
	}

	public void doBIG() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setSize(20);
	}

	public void doCENTER() {
		((GraphicsState) states.get(states.size() - 1)).setAlign("center");
	}

	public void doH1() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setSize(16);
		((GraphicsState) states.get(states.size() - 1)).getFont().setStyle(Font.BOLD);
		((GraphicsState) states.get(states.size() - 1)).setSpacingBefore(8f);
		((GraphicsState) states.get(states.size() - 1)).setSpacingAfter(8f);
	}

	public void doH2() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setSize(14);
		((GraphicsState) states.get(states.size() - 1)).getFont().setStyle(Font.BOLD);
		((GraphicsState) states.get(states.size() - 1)).setSpacingBefore(7f);
		((GraphicsState) states.get(states.size() - 1)).setSpacingAfter(7f);
	}

	public void doH3() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setSize(12);
		((GraphicsState) states.get(states.size() - 1)).getFont().setStyle(Font.BOLD);
		((GraphicsState) states.get(states.size() - 1)).setSpacingBefore(6f);
		((GraphicsState) states.get(states.size() - 1)).setSpacingAfter(6f);
	}

	public void doH4() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setSize(12);
		((GraphicsState) states.get(states.size() - 1)).getFont().setStyle(Font.UNDERLINE);
		((GraphicsState) states.get(states.size() - 1)).setSpacingBefore(6f);
		((GraphicsState) states.get(states.size() - 1)).setSpacingAfter(6f);
	}

	public void doH5() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setSize(11);
		((GraphicsState) states.get(states.size() - 1)).getFont().setStyle(Font.BOLD);
	}

	public void doH6() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setSize(11);
		((GraphicsState) states.get(states.size() - 1)).getFont().setStyle(Font.BOLD);
	}

	public void doI() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setStyle(Font.ITALIC);
	}

	public void doEM() {
		doI();
	}

	public void doU() {
		((GraphicsState) states.get(states.size() - 1)).getFont().setStyle(Font.UNDERLINE);
	}
}
/**
 * 
 * $Log: TagsManager.java,v $
 * Revision 1.2  2009/05/25 21:07:16  eeh
 * *** empty log message ***
 *
 * Revision 1.1  2007/12/26 15:57:40  tah
 * *** empty log message ***
 *
 * Revision 1.9  2007/03/06 19:55:51  tah
 * *** empty log message ***
 *
 * Revision 1.8  2006/08/25 16:48:23  tah
 * *** empty log message ***
 *
 * Revision 1.7  2006/07/18 16:22:42  tah
 * *** empty log message ***
 *
 * Revision 1.6  2006/07/06 15:45:25  tah
 * *** empty log message ***
 *
 * Revision 1.5  2006/07/05 16:00:44  nts
 * Refatorando para melhorar qualidade do código
 *
 * Revision 1.4  2006/05/23 19:35:06  tah
 * *** empty log message ***
 *
 * Revision 1.3  2006/05/11 20:30:23  tah
 * *** empty log message ***
 *
 * Revision 1.2  2006/04/11 19:43:43  tah
 * *** empty log message ***
 * Revision 1.1 2006/04/03 21:30:45 tah Utilizando o
 * nheengatu
 * 
 * Revision 1.6 2006/01/01 13:45:37 aryjr Feliz 2006!!!
 * 
 * Revision 1.5 2005/12/16 14:06:37 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.4 2005/12/07 14:49:50 aryjr Bugs with the relatorio.jsp HTML code.
 * 
 * Revision 1.3 2005/11/16 12:42:18 aryjr Testes com posicionamentos absolutos.
 * 
 * Revision 1.2 2005/11/14 13:23:00 aryjr CSS Parser ok from now.
 * 
 * Revision 1.1 2005/11/14 12:17:46 aryjr Renomeando os pacotes.
 * 
 * Revision 1.3 2005/11/11 21:25:31 aryjr Trabalhando no suporte ao CSS2.
 * 
 * Revision 1.2 2005/11/11 21:09:53 aryjr Retomando o desenvolvimento e
 * trabalhando no suporte ao CSS2.
 * 
 * Revision 1.1 2005/09/26 19:41:14 aryjr Aproveitando a greve para voltar a
 * atividade.
 * 
 * Revision 1.1 2005/09/10 23:43:41 aryjr Passando para o java.net.
 * 
 * Revision 1.6 2005/07/02 01:18:57 aryjunior Site do projeto.
 * 
 * Revision 1.5 2005/06/04 13:29:25 aryjunior LGPL.
 * 
 * Revision 1.4 2005/06/04 02:24:41 aryjunior Testes com o snapshot. Um .jsp com
 * um HTML mais complexo.
 * 
 * Revision 1.3 2005/05/30 01:55:57 aryjunior Alguns detalhes no cabecalho dos
 * arquivos e fazendo alguns testes com tabelas ainhadas.
 * 
 * Revision 1.2 2005/05/28 23:21:41 aryjunior Corrigindo o cabecalho.
 * 
 * Revision 1.1.1.1 2005/05/28 21:10:29 aryjunior Initial import.
 * 
 */
