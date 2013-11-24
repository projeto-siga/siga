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
package br.gov.jfrj.siga.wf.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import br.gov.jfrj.siga.wf.service.impl.WfServiceImpl;

/**
 * Inicia a classe WfServiceImpl.java como um webservice.
 * 
 * @author kpf
 * 
 */
public class WfWsServlet extends CXFNonSpringServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Inicia a classe WfServiceImpl.java como um webservice.
	 */
	@Override
	public void loadBus(ServletConfig servletConfig) throws ServletException {
		super.loadBus(servletConfig);

		// You could add the endpoint publish codes here
		Bus bus = getBus();
		BusFactory.setDefaultBus(bus);
		Endpoint.publish("/WfService", new WfServiceImpl());

		// You can als use the simple frontend API to do this
		// ServerFactoryBean factory = new ServerFactoryBean();
		// factory.setBus(bus);
		// factory.setServiceClass(WfServiceImpl.class);
		// factory.setAddress("/WfService");
		// factory.create();
	}
}
