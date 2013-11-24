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
package br.gov.jfrj.siga.servlets;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.SigaExceptionHandler;

/**
 * Hello World example as a Servlet.
 * 
 * @author blowagie
 */
public class Anexo extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4123818272231065322L;

	@Override
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException,
			ServletException {
		try {
			Long num = null;

			final Pattern p = Pattern.compile("/([0-9]+)/");
			final Matcher m = p.matcher(request.getRequestURI());
			if (m.find()) {
				num = Long.parseLong(m.group(1));
			}

			final ExMovimentacao mov = ExDao.getInstance().consultar(num,
					ExMovimentacao.class, false);
			// movDao.consultarConteudoBlob(mov);

			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			// setting the content type
			response.setContentType(mov.getConteudoTpMov());
			// the contentlength is needed for MSIE!!!
			response.setContentLength(mov.getConteudoBlobMov2().length);
			// write ByteArrayOutputStream to the ServletOutputStream
			final ServletOutputStream out = response.getOutputStream();
			out.write(mov.getConteudoBlobMov2());
			//out.flush();
			out.close();
		} catch (final Exception e) {
			SigaExceptionHandler seh = new SigaExceptionHandler();
			seh.logaExcecao(e);
//			e.printStackTrace();
//			System.err.println("document: " + e.getMessage());
		}
		// document.close();
	}

	@Override
	protected void doPost(final HttpServletRequest arg0,
			final HttpServletResponse arg1) throws ServletException,
			IOException {
		doGet(arg0, arg1);
	}
}
