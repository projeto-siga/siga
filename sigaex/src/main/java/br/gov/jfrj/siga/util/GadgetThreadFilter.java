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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class GadgetThreadFilter implements Filter {

	private static long requisicoes = 0;
	private static long contadorGeral = 0;
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		String id = httpRequest.getRequestedSessionId();
		
		if ((contadorGeral % 30) == 0){
			requisicoes = 0;
			contadorGeral = 0;
		}
		
		if (httpRequest.getRequestURI().contains("gadget.action")) {
			if (requisicoes <= 7) {
				contadorGeral++;
				requisicoes++;
				chain.doFilter(request, response);
				requisicoes--;
//				System.out.println("Acabou gadget::: Sobram " + requisicoes +" requests");
			} else {
				PrintWriter out = response.getWriter();
				out.write("TRYAGAIN###" + httpRequest.getRequestURI());
			}
		} else
			chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
