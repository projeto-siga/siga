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

/*
 * Criado em  05/01/2006
 *
 */
package br.gov.jfrj.siga.base;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.bouncycastle.util.encoders.Hex;

public class GeraMessageDigest {

	public static String byteParaString(final byte hash[]) {
		final StringBuilder sb = new StringBuilder(hash.length * 2);
		for (byte element : hash) {
			sb.append(GeraMessageDigest.converterDigito((element >> 4)));
			sb.append(GeraMessageDigest.converterDigito((element & 0x0f)));
		}
		return sb.toString();
	}

	private static char converterDigito(int caracter) {
		caracter &= 0x0f;
		if (caracter >= 10)
			return ((char) (caracter - 10 + 'a'));
		else
			return ((char) (caracter + '0'));

	}

	public static String executaHash(final byte[] textoPuro,
			final String algoritmo) throws NoSuchAlgorithmException {
		final MessageDigest md = MessageDigest.getInstance(algoritmo);
		md.update(textoPuro);
		return GeraMessageDigest.byteParaString(md.digest());
	}

	public static String geraSenha(int tamanhoSenha) {
		final StringBuilder caracteres = new StringBuilder();

		for (int i = 0; i < 26; i++) {

			final char minuscula = (char) ('a' + i);
			final char maiuscula = (char) ('A' + i);
			caracteres.append(minuscula);
			caracteres.append(maiuscula);

		}
		// Exclusão das letras o e O na geração de senha
		caracteres.deleteCharAt(caracteres.indexOf("o"));
		caracteres.deleteCharAt(caracteres.indexOf("O"));

		// exclusão do número zero na geração de senha
		for (int i = 0; i < 10 - 1; i++) {
			caracteres.append('1' + i);
		}

		boolean contemMinusculas;
		boolean contemMaiusculas;
		boolean contemNumeros;

		final Random random = new Random();
		StringBuilder novaSenha;

		do {
			novaSenha = new StringBuilder();
			contemMinusculas = false;
			contemMaiusculas = false;
			contemNumeros = false;
			for (int i = 0; i < tamanhoSenha; i++) {
				novaSenha.append(caracteres.charAt(random.nextInt(caracteres
						.length())));
			}

			for (int i = 0; i < novaSenha.length(); i++) {
				Character c = novaSenha.charAt(i);
				if (c.isLowerCase(c)) {
					contemMinusculas = true;
				}
				if (c.isUpperCase(c)) {
					contemMaiusculas = true;
				}
				if (c.isDigit(c)) {
					contemNumeros = true;
				}

			}

		} while (!contemMinusculas || !contemMaiusculas || !contemNumeros);

		return novaSenha.toString();
	}
	
	public static String geraSenha() {
		return geraSenha(8);
	}
	
	public static String calcSha256(String strPlain) throws NoSuchAlgorithmException {	
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(strPlain.getBytes(StandardCharsets.UTF_8));
		String sha256hex = new String(Hex.encode(hash));
		return sha256hex;
	}
}
