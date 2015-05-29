/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.ldap.util;


public class LdapUtils {

	/**
	 * Formata o DN corretamente caso exista algum caracter especial.
	 * 
	 * Referências:
	 * http://java.sun.com/products/jndi/tutorial/beyond/names/syntax.html
	 * http://www.owasp.org/index.php/Preventing_LDAP_Injection_in_Java
	 * 
	 * @param name
	 * @return
	 */
	public static String escapeDN(String name) {
		StringBuilder sb = new StringBuilder();
		if ((name.length() > 0)
				&& ((name.charAt(0) == ' ') || (name.charAt(0) == '#'))) {
			sb.append('\\'); // add the leading backslash if needed
		}
		for (int i = 0; i < name.length(); i++) {
			char curChar = name.charAt(i);
			switch (curChar) {
			case '/':
				sb.append("\\/");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			// case ',':
			// sb.append("\\,");
			// break;
			case '+':
				sb.append("\\+");
				break;
			case '"':
				sb.append("\\\"");
				break;
			case '<':
				sb.append("\\<");
				break;
			case '>':
				sb.append("\\>");
				break;
			case ';':
				sb.append("\\;");
				break;
			default:
				sb.append(curChar);
			}
		}
		if ((name.length() > 1) && (name.charAt(name.length() - 1) == ' ')) {
			sb.insert(sb.length() - 1, '\\'); // add the trailing backslash if
			// needed
		}
		return sb.toString();
	}

	/**
	 * Formata o filtro corretamente caso exista algum caracter especial.
	 * 
	 * Referências:
	 * http://java.sun.com/products/jndi/tutorial/beyond/names/syntax.html
	 * http://www.owasp.org/index.php/Preventing_LDAP_Injection_in_Java
	 * 
	 * @param filter
	 * @return
	 */
	public static final String escapeLDAPSearchFilter(String filter) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < filter.length(); i++) {
			char curChar = filter.charAt(i);
			switch (curChar) {
			case '\\':
				sb.append("\\5c");
				break;
			case '*':
				sb.append("\\2a");
				break;
			case '(':
				sb.append("\\28");
				break;
			case ')':
				sb.append("\\29");
				break;
			case '\u0000':
				sb.append("\\00");
				break;
			default:
				sb.append(curChar);
			}
		}
		return sb.toString();
	}

}
