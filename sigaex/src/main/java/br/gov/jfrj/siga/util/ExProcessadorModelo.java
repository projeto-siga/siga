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
package br.gov.jfrj.siga.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.util.SwallowingHttpServletResponse;

import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.util.ProcessadorModelo;

public class ExProcessadorModelo implements ProcessadorModelo {
	
	public ExProcessadorModelo() {
	}

	/**
	 * Processar um template JSP e retornar o resultado na forma de uma string
	 * 
	 * @param doc
	 * @param mov
	 * @param acao
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String processarModelo(CpOrgaoUsuario ou, Map<String, Object> attrs, Map<String, Object> params) throws Exception {
		MyHttpRequest rw = createMyHttpRequest(attrs, params);
		ServletContext context = CurrentRequest.get().getContext();
		HttpServletResponse response = CurrentRequest.get().getResponse();
		
		try (ByteArrayOutputStream bout = new ByteArrayOutputStream(); Writer w = new OutputStreamWriter(bout)) {
			SwallowingHttpServletResponse responseToRead = new SwallowingHttpServletResponse(response, w, "iso-8859-1");
			
			javax.servlet.RequestDispatcher dispatcher = context.getRequestDispatcher("/WEB-INF/page/exDocumento/processa_modelo.jsp");
			dispatcher.include(rw, responseToRead);
			w.flush();
			String s = bout.toString();
			return s;
		}
	}

	@SuppressWarnings("unchecked")
	private MyHttpRequest createMyHttpRequest(Map<String, Object> attrs, Map<String, Object> params) {
		MyHttpRequest requestWrapper = new MyHttpRequest(CurrentRequest.get().getRequest());
		
		for (String attr : attrs.keySet()) {
			requestWrapper.setAttribute(attr, attrs.get(attr));
		}
		Map<String, String> p = requestWrapper.getParameterMap();
		for (String s : params.keySet()) {
			p.put(s, (String) params.get(s));
		}
		return requestWrapper;
	}
}

class MyHttpRequest extends HttpServletRequestWrapper {
	@SuppressWarnings("rawtypes")
	private Map parametersMap;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MyHttpRequest(HttpServletRequest request) {
		super(request);
		this.parametersMap = new HashMap(request.getParameterMap());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getParameterMap() {
		return parametersMap;
	}
}