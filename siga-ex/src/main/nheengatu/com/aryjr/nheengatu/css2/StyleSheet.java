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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

/**
 * 
 * Receive a .css file and parse it.
 * 
 * @version $Id: StyleSheet.java,v 1.1 2007/12/26 15:57:42 tah Exp $
 * @author <a href="mailto:guilherme@3fx.com.br">Guilherme Souza</a>
 * 
 */
public class StyleSheet {
	private static StyleSheet instance;

	private HashMap styles = new HashMap();

	public static StyleSheet getInstance() {
		// The Singleton pattern [GoF]
		if (StyleSheet.instance == null) {
			StyleSheet.instance = new StyleSheet();
		}
		return StyleSheet.instance;
	}
	
	public StyleSheet() {
		
	}

	public void processCSSFile(InputStream isCSS) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(isCSS))) {
			final CSSOMParser parser = new CSSOMParser();
			final InputSource is = new InputSource(reader);

			final CSSStyleSheet stylesheet = parser.parseStyleDeclaration(is).getParentRule().getParentStyleSheet();
			final CSSRuleList rules = stylesheet.getCssRules();
			CSSRule rule;
			String css;
			for (int i = 0; i < rules.getLength(); i++) {
				rule = rules.item(i);
				css = rule.getCssText();
				styles.put(css.substring(0, css.indexOf('{')).trim(), new Style(css.substring(0, css.indexOf('{')).trim(), css
						.substring(css.indexOf('{') + 1, css.length() - 1)));
			}
		} catch (final Exception e) {
		}
	}

	public void processCSSFile(final String url) {
		try {
			final URL u = new URL(url);
			processCSSFile(u.openStream());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap getStyles() {
		return styles;
	}
}
/**
 * 
 * $Log: StyleSheet.java,v $
 * Revision 1.1  2007/12/26 15:57:42  tah
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/24 18:02:39  tah
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
 * Revision 1.4 2005/12/16 14:06:33 aryjr Problem with cell heights solved!!!
 * 
 * Revision 1.3 2005/11/14 14:37:37 aryjr Grouping components inside a div tag.
 * 
 * Revision 1.2 2005/11/14 13:22:59 aryjr CSS Parser ok from now.
 * 
 * Revision 1.1 2005/11/14 12:17:39 aryjr Renomeando os pacotes.
 * 
 * Revision 1.3 2005/11/11 21:25:32 aryjr Trabalhando no suporte ao CSS2.
 * 
 * Revision 1.2 2005/11/11 21:09:53 aryjr Retomando o desenvolvimento e
 * trabalhando no suporte ao CSS2.
 * 
 * Revision 1.1 2005/09/10 23:43:42 aryjr Passando para o java.net.
 * 
 * Revision 1.1 2005/08/18 01:32:34 aryjunior Publicando a contribuicao do
 * Guilherme.
 * 
 */
