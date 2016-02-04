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

import org.jboss.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

public class SalvamentoAutomaticoThreadFilter implements Filter {

	private static ArrayList<String> requisicoes = new ArrayList<String>();
	
	private static final Logger log = Logger.getLogger( SalvamentoAutomaticoThreadFilter.class );

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		String id = httpRequest.getRequestedSessionId();
		
		try {
			if (httpRequest.getParameter("auto_save") != null) {
				if (requisicoes.size() <= 3 && !requisicoes.contains(id)) {
					requisicoes.add(id);
					chain.doFilter(request, response);
					requisicoes.remove(id);
				}
			} else {			
				chain.doFilter(request, response);
			}
		} catch (Throwable ex) {
			log.error( ex.getMessage(), ex );
			ex.printStackTrace();
		}

	}

	public void init(FilterConfig arg0) throws ServletException {}
	public void destroy() {}

}
