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
package br.gov.jfrj.siga.ex.util;

import org.jboss.logging.Logger;

import javax.servlet.*;
import java.io.IOException;

public class LogThreadFilter implements Filter {
	private static final Logger logger = Logger.getLogger(LogThreadFilter.class);

	/*
	static {
		try {
			BasicConfigurator.configure();
			Appender fileAppender = new FileAppender(new PatternLayout(
					"(%d{dd/MM/yy HH:mm:ss})	%m %n"), SigaExProperties.getString( "log.thread.filter.dir" ) + "/sigaex.log");
			logger.setLevel(Level.INFO);
			logger.addAppender(fileAppender);
		} catch (IOException ioe) {
			logger.warn( "Erro log sigaex: ", ioe);
			ioe.printStackTrace();
		}
	}
	*/

	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		
		try {
			chain.doFilter(request, response);
		} catch (RuntimeException rte) {
            logger.error( "Ocorreu um erro ao executar o filtro. ", rte );
			//rte.printStackTrace();
			throw new ServletException( rte );
		}
		
		/*try {
		 	long horaIni = System.currentTimeMillis();
			logger.info(
					new FormatadorLogOperacaoWeb((HttpServletRequest) request,
							(System.currentTimeMillis() - horaIni)));
		} catch (Throwable t) {
			System.out.println("Erro log sigaex: ");
			t.printStackTrace();
		}*/
	}

	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

}
