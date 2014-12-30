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
package br.gov.jfrj.siga.cd.servlet;

import static br.gov.jfrj.siga.cd.Constants.HASH_SHA1;
import static br.gov.jfrj.siga.cd.Constants.HASH_SHA1_WRONG;
import static br.gov.jfrj.siga.cd.Constants.PKCS7;
import static br.gov.jfrj.siga.cd.Constants.SIGNING_CALENDAR;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.jfrj.siga.cd.service.impl.CdServiceImpl;

public class CdTestesServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CdServiceImpl service = new CdServiceImpl();
		
		try{
			service.validarAssinatura(HASH_SHA1, PKCS7, SIGNING_CALENDAR.getTime(), false);
			service.validarAssinatura(HASH_SHA1_WRONG, PKCS7, SIGNING_CALENDAR.getTime(), false);
			service.validarAssinatura(HASH_SHA1, PKCS7, new Date(new Date().getTime() + 3600), false);
			service.recuperarCPF(PKCS7);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		resp.setHeader("Content-Type", "text/plain");
		resp.getWriter().print("OK!");
	}
}
