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
package br.gov.jfrj.siga.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

public class ConexaoHTTP {

	public static String get(String URL, HashMap<String, String> header)
			throws AplicacaoException {

		try {

			HttpURLConnection conn = (HttpURLConnection) new URL(URL)
					.openConnection();

			//conn.setInstanceFollowRedirects(true);

			for (String s : header.keySet()) {
					conn.setRequestProperty(s, header.get(s));
			}

			System.setProperty("http.keepAlive", "false");

			StringWriter writer = new StringWriter();
			IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
			return writer.toString();
			
		} catch (IOException ioe) {
			throw new AplicacaoException("Não foi possível abrir conexão", 1,
					ioe);
		}

	}

	public static String get(String url) throws AplicacaoException {
		return get(url, new HashMap<String, String>());
	}
}
