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
package br.gov.jfrj.siga.base.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class Texto {

	/**
	 * Remove os acentos da string e coloca os caracteres em letras minúsculas
	 * 
	 * @param acentuado
	 *            - String acentuada
	 * @return String sem acentos
	 */
	public static String removeAcentoHTMLMinusculas(String acentuado) {
		if (acentuado == null)
			return null;
		return removeAcentoHTML(acentuado.toLowerCase());
	}

	/**
	 * Remove os acentos da string e coloca os caracteres em letras maiúsculas
	 * 
	 * @param acentuado
	 *            - String acentuada
	 * @return String sem acentos
	 */
	public static String removeAcentoMaiusculas(String acentuado) {
		if (acentuado == null)
			return null;
		return removeAcento(acentuado.toUpperCase());
	}
	
	/**
	 * Remove os acentos da string
	 * 
	 * @param acentuado
	 *            - String acentuada
	 * @return String sem acentos
	 */
	public static String removeAcento(String acentuado) {
		if (acentuado == null)
			return null;
		String temp = new String(acentuado);
		temp = temp.replaceAll("[ÃÂÁÀ]", "A");
		temp = temp.replaceAll("[ÉÈÊ]", "E");
		temp = temp.replaceAll("[ÍÌÎ]", "I");
		temp = temp.replaceAll("[ÕÔÓÒ]", "O");
		temp = temp.replaceAll("[ÛÚÙÜ]", "U");
		temp = temp.replaceAll("[Ç]", "C");
		temp = temp.replaceAll("[ãâáà]", "a");
		temp = temp.replaceAll("[éèê]", "e");
		temp = temp.replaceAll("[íìî]", "i");
		temp = temp.replaceAll("[õôóò]", "o");
		temp = temp.replaceAll("[ûúùü]", "u");
		temp = temp.replaceAll("[ç]", "c");
		return temp;
	}

	/**
	 * Remove os acentos da string
	 * 
	 * @param acentuado
	 *            - String acentuada
	 * @return String sem acentos
	 */
	public static String removeAcentoHTML(String acentuado) {
		if (acentuado == null)
			return null;
		String temp = new String(acentuado);
		temp = temp.replaceAll("&aacute;", "a");
		temp = temp.replaceAll("&eacute;", "e");
		temp = temp.replaceAll("&iacute;", "i");
		temp = temp.replaceAll("&oacute;", "o");
		temp = temp.replaceAll("&uacute;", "u");
		temp = temp.replaceAll("&agrave;", "à");
		temp = temp.replaceAll("&acirc;", "a");
		temp = temp.replaceAll("&ecirc;", "e");
		temp = temp.replaceAll("&ocirc;", "o");
		temp = temp.replaceAll("&atilde;", "a");
		temp = temp.replaceAll("&otilde;", "o");
		return temp;
	}
	
	public String iniciais(String s) {
		final StringBuilder sb = new StringBuilder(10);
		boolean f = true;

		s = s.replace(" E ", " ");
		s = s.replace(" DA ", " ");
		s = s.replace(" DE ", " ");
		s = s.replace(" DO ", " ");

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (f) {
				sb.append(c);
				f = false;
			}
			if (c == ' ') {
				f = true;
			}
		}
		return sb.toString();
	}

	public static String maiusculasEMinusculas(String s) {
		final StringBuilder sb = new StringBuilder(s.length());
		boolean f = true;

		for (short i = 0; i < s.length(); i++) {
			String ch = s.substring(i, i + 1);
			if (!ch.toUpperCase().equals(ch.toLowerCase())) {
				if (f) {
					sb.append(ch.toUpperCase());
					f = false;
				} else
					sb.append(ch.toLowerCase());
			} else {
				sb.append(ch);
				f = true;
			}
		}

		s = sb.toString();

		s = s.replace(" E ", " e ");
		s = s.replace(" Da ", " da ");
		s = s.replace(" Das ", " das ");
		s = s.replace(" De ", " de ");
		s = s.replace(" Do ", " do ");
		s = s.replace(" Dos ", " dos ");

		return s;
	}

	public static String primeiraMaiuscula(String string) {
		String stringAux1 = string.substring(0, 1);
		String stringAux2 = string.substring(1).toLowerCase();
		return stringAux1 + stringAux2;
	}
	
	public static String extrai(final String sSource, final String sBegin,
			final String sEnd) throws UnsupportedEncodingException {
		final Integer iBegin = sSource.indexOf(sBegin);
		final Integer iEnd = sSource.indexOf(sEnd);

		if (iBegin == -1 || iEnd == -1)
			return null;

		final String sResult = sSource
				.substring(iBegin + sBegin.length(), iEnd);
		return sResult;
	}

	public static String removerEspacosExtra(String texto) {
		return texto.replaceAll("\\s{2,}", " ");
	}

	public static String inciaisMaiuscula(String texto) {
		if (texto == null) {
			return texto;
		}
		char caracteres[] = texto.toCharArray();
		for (int i = 0; i < caracteres.length; i++) {
			if (i == 0 || String.valueOf(caracteres[i - 1]).equals(" ")) {
				caracteres[i] = Character.toUpperCase(caracteres[i]);
			} else {
				caracteres[i] = Character.toLowerCase(caracteres[i]);
			}
		}

		return String.valueOf(caracteres);
	}

	public static String slugify(String string, boolean lowercase,
			boolean underscore) {
		if (string == null)
			return null;
		string = string.trim();
		if (string.length() == 0)
			return null;
		string = removeAcento(string);
		// Apostrophes.
		string = string.replaceAll("([a-z])'s([^a-z])", "$1s$2");
		string = string.replaceAll("[^\\w]", "-").replaceAll("-{2,}", "-");
		// Get rid of any - at the start and end.
		string = string.replaceAll("-+$", "").replaceAll("^-+", "");

		if (underscore)
			string = string.replaceAll("-", "_");

		return (lowercase ? string.toLowerCase() : string);
	}
	
	//Edson: esta rotina não cobre todos os casos. Foi feita inicialmente
	//para pluralizar apenas nomes de espécies documentais
	public static String pluralizar(String s){
		StringBuilder pluralFinal = new StringBuilder();
		List<String> stopList = Arrays.asList("de", "do", "da");
		boolean atingiuStopList = false;
		
		for (String palavra : s.trim().split("\\s")){
			String palavraPlural = palavra;
			if (stopList.contains(palavraPlural))
				atingiuStopList = true;
			if (!atingiuStopList){
				palavraPlural = palavraPlural.replaceAll("m$", "ns").replaceAll("M$", "NS");
				palavraPlural = palavraPlural.replaceAll("ão$", "ões").replaceAll("ÃO$", "ÕES");
				palavraPlural = palavraPlural.replaceAll("r$", "es").replaceAll("R$", "RES");
				palavraPlural = palavraPlural.replaceAll("l$", "is").replaceAll("I$", "IS");
				palavraPlural = palavraPlural.replaceAll("a$", "as").replaceAll("A$", "AS");
				palavraPlural = palavraPlural.replaceAll("e$", "es").replaceAll("E$", "ES");
				palavraPlural = palavraPlural.replaceAll("o$", "os").replaceAll("O$", "OS");
			}
			pluralFinal.append((pluralFinal.length() > 0 ? " " : "") + palavraPlural); 
		}
		return pluralFinal.toString();
	}
	
	public static String semQuebraDeLinha(String string) {
		return string.replaceAll("[\\r\\n]+", " ");
	}
	
	public static String maximoCaracteres(String string, int max) {
		return string.length() > max ? (string.substring(0, max) + "...") : string;
	}
	
	public static String maximoCaracteres(String string, int max, int limit) {
		return string.length() > limit ? (string.substring(0, max) + "...") : string;
	}
	
	public static class FuncaoConfianca {
		public static final String REGEX_CARACTERES_PERMITIDOS = "[a-zA-ZáâãéêíóôõúçÁÂÃÉÊÍÓÔÕÚÇ 0-9-/.]+";		
	}
	
	public static class DpPessoa {
		public static final String NOME_REGEX_CARACTERES_PERMITIDOS = "[a-zA-ZáâãäéêëíïóôõöúüçñÁÂÃÄÉÊËÍÏÓÔÕÖÚÜÇÑ'' ]+";
		public static final String EMAIL_REGEX_PATTERN = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$";														 
	}

	public static boolean equals(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		if (s1 == null || s2 == null)
			return false;
		return s1.equals(s2);
	}
}
