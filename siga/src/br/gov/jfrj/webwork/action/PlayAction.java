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
 * Criado em 23/11/2005
 */

package br.gov.jfrj.webwork.action;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.EncoderException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.ConexaoHTTP;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

import com.opensymphony.xwork.Action;

public class PlayAction extends SigaActionSupport {

	@Override
	public String execute() throws Exception {
		return Action.SUCCESS;
	}

	public String aUsuarioAutenticado() {
		return Action.SUCCESS;
	}

	/**
	 * Recebe chamadas Ajax do Siga e repassa a aplicações Play. É necessário um
	 * proxy porque nem sempre a aplicação Play chamada está na mesma porta que
	 * o Siga.
	 * 
	 * @return
	 * @throws AplicacaoException
	 */
	public String aAjaxProxy() throws AplicacaoException {
		try {
			String url = SigaBaseProperties.getString("siga." + param("modulo") +"."
					+ SigaBaseProperties.getString("ambiente") + ".url.interna");
			if (!url.endsWith("/")) 
				url += "/";
			url += param("action");

			HashMap<String, String> header = new HashMap<String, String>();
			Enumeration<String> headerNames = getRequest().getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				if (!headerName.equals("host"))
					header.put(headerName, getRequest().getHeader(headerName));
			}

			getRequest()
					.setAttribute(
							"result",
							new String(ConexaoHTTP.get(url, header).getBytes()));

			return Action.SUCCESS;
		} catch (Exception e) {

		}
		return "";
	}
}