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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

public class ConexaoHTTP {

	public static String get(String URL, HashMap<String, String> header)
			throws AplicacaoException {
		return get(URL, header, null, null);
	}

	public static String get(String URL, HashMap<String, String> header, Integer timeout, String payload)
			throws AplicacaoException {

		try {

			HttpURLConnection conn = (HttpURLConnection) new URL(URL)
					.openConnection();
			
			if (timeout != null) {
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
			}
			
			//conn.setInstanceFollowRedirects(true);

			if (header != null) {
				for (String s : header.keySet()) {
						conn.setRequestProperty(s, header.get(s));
				}
			}	

			System.setProperty("http.keepAlive", "false");
			
			if (payload != null) {
				byte ab[] = payload.getBytes("UTF-8");
				conn.setRequestMethod("POST");
				// Send post request
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(ab);
				os.flush();
				os.close();
			}

			//StringWriter writer = new StringWriter();
			//IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
			//return writer.toString();
			return IOUtils.toString(conn.getInputStream(), "UTF-8");
			
		} catch (IOException ioe) {
			throw new AplicacaoException("Não foi possível abrir conexão", 1,
					ioe);
		}

	}

	public static String get(String url) throws AplicacaoException {
		return get(url, new HashMap<String, String>());
	}
}
