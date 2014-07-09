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
package br.gov.jfrj.siga.gi.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import br.gov.jfrj.siga.gi.service.impl.GiServiceImpl;

public class GiWsServlet extends CXFNonSpringServlet {
	private static final long serialVersionUID = 1L;

	public void loadBus(ServletConfig servletConfig) {
		super.loadBus(servletConfig);
		
		// Desta forma dava erro na versão EAP 5.1.2: 
		// Invalid endpoint address: /GiService
		//
		// You could add the endpoint publish codes here
		Bus bus = getBus();
		BusFactory.setDefaultBus(bus);
		Endpoint.publish("/GiService", new GiServiceImpl());

		// You can als use the simple frontend API to do this
		// Desta forma funciona na versão EAP 5.1.2
		// ServerFactoryBean factory = new ServerFactoryBean();
		// factory.setBus(bus);
		// factory.getInInterceptors().add(new LoggingInInterceptor());
		// factory.getOutInterceptors().add(new LoggingOutInterceptor());
		// factory.setServiceClass(GiServiceImpl.class);
		// factory.setAddress("/GiService");
		// factory.create();
	}
}
