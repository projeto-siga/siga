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

public class SigaHTTPTest {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		final SigaHTTP http = new SigaHTTP();
		String url = "http://172.16.1.110/search?q=renato%2Bcrivano&site=siga&client=json&proxystylesheet=json&oe=UTF-8&ie=UTF-8&getfields=*&filter=0&output=xml_no_dtd&lr=lang_pt&sort=D%3AL%3Ad1&access=a";
		// String url = "https://www.google.com";
		String response = http.handleAuthentication(url, null,
				"siga.jfrj.jus.br", "tMQuyF4E5INF1shyGw3eQYsY.sigadoc");

		System.out.println(response);
	}
}
