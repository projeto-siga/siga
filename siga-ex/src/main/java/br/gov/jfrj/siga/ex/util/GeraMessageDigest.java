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
package br.gov.jfrj.siga.ex.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

	public static String executaHash(final byte[] textoPuro, final String algoritmo) throws NoSuchAlgorithmException {
		final MessageDigest md = MessageDigest.getInstance(algoritmo);
		md.update(textoPuro);
		return GeraMessageDigest.byteParaString(md.digest());
	}

}
