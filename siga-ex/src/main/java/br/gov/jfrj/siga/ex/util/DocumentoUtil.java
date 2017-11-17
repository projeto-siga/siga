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
package br.gov.jfrj.siga.ex.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.base.Texto;

public class DocumentoUtil {

	public static String obterLetraViaPorCodigo(int codigo) {
		return new Character(((char)(codigo+64))).toString();
	}
	
	public static String obterLetraViaPorCodigo(String codigo){
		return obterLetraViaPorCodigo(Integer.parseInt(codigo));
	}
	
	public static String obterDataExtenso(String localidade, Date dataDoc) {

		// Forçando a ficar em pt_BR, antes a data aparecia na linguagem
		// definida no servidor de aplicação (tomcat, jbos, etc.)
		SimpleDateFormat df1 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy.",
				new Locale("pt", "BR"));
		try {
			Pattern p = Pattern.compile("(-..)$"); //hifen e dois últimos caracteres
			Matcher m = p.matcher(Texto.maiusculasEMinusculas(localidade));
			StringBuffer sb = new StringBuffer();
			while (m.find()) {
			    m.appendReplacement(sb, m.group(1).toUpperCase());
			}
			m.appendTail(sb);
			
			
			return sb + ", " + df1.format(dataDoc).toLowerCase();
		} catch (Exception e) {
			return null;
		}

	}

}
