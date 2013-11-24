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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.util.SwallowingHttpServletResponse;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.util.ProcessadorModelo;

public class ExProcessadorModelo implements ProcessadorModelo {

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
	public String processarModelo(CpOrgaoUsuario ou, Map<String, Object> attrs,
			Map<String, Object> params) throws Exception {

		// System.out.println(System.currentTimeMillis() + " - INI
		// processarModelo");

		ServletContext sc = com.opensymphony.webwork.ServletActionContext
				.getServletContext();
		HttpServletResponse r = com.opensymphony.webwork.ServletActionContext
				.getResponse();
		MyHttpRequest rw = new MyHttpRequest(
				com.opensymphony.webwork.ServletActionContext.getRequest());

		rw.clearAttributes();

		for (String s : attrs.keySet()) {
			rw.setAttribute(s, attrs.get(s));
		}

		Map<String, String> p = rw.getParameterMap();
		p.clear();
		for (String s : params.keySet()) {
			p.put(s, (String) params.get(s));
		}

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		Writer w = new OutputStreamWriter(bout);
		SwallowingHttpServletResponse r2 = new SwallowingHttpServletResponse(r,
				w, "iso-8859-1");

		javax.servlet.RequestDispatcher dispatcher = sc
				.getRequestDispatcher("/paginas/expediente/processa_modelo.jsp");
		// request.setAttribute("Language","Java");
		dispatcher.include(rw, r2);
		w.flush();
		String s = bout.toString();

		// System.out.println(System.currentTimeMillis() + " - FIM
		// processarModelo");
		return s;
	}

}

class MyHttpRequest extends HttpServletRequestWrapper {
	ExDocumento doc;

	ExMovimentacao mov;

	String queryString;

	String form;

	HashMap parameterMap;

	public MyHttpRequest(HttpServletRequest arg0
	// , ExDocumento doc,
	// ExMovimentacao mov
	) {
		super(arg0);
		this.doc = doc;
		this.mov = mov;
		// TODO Auto-generated constructor stub
		this.parameterMap = new HashMap(super.getParameterMap());
	}

	public void clearAttributes() {
		Enumeration e = getAttributeNames();
		List<String> l = new ArrayList<String>();
		while (e.hasMoreElements()) {
			String s = e.nextElement().toString();
			l.add(s);
		}
		for (String s : l) {
			removeAttribute(s);
		}
	}

	// @Override
	// public String getQueryString() {
	// return "processar_modelo=1&idDoc="
	// + mov.getExDocumento().getIdDoc() + "&id=" + mov.getIdMov());;
	// }

	@Override
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		return parameterMap;
	}

}
