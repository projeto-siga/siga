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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogThreadFilter implements Filter {

	private static Logger logger = Logger.getLogger(LogThreadFilter.class);

	static {
		try {
			BasicConfigurator.configure();
			Appender fileAppender = new FileAppender(new PatternLayout(
					"(%d{dd/MM/yy HH:mm:ss})	%m %n"), "sigaex.log");
			logger.setLevel(Level.INFO);
			logger.addAppender(fileAppender);
		} catch (IOException e) {
			System.out.println("Erro log sigaex: ");
			e.printStackTrace();
		}
	}

	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		long horaIni = System.currentTimeMillis();
		chain.doFilter(request, response);
		/*try {
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
