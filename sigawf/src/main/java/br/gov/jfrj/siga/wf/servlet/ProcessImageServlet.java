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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;

import br.gov.jfrj.siga.wf.util.WfContextBuilder;

/**
 * Servlet que desenha o gráfico do processo.
 * 
 * @author kpf
 * 
 */
public class ProcessImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Pega o desenho do process definition e coloca no output stream.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long processDefinitionId = Long.parseLong(request.getParameter("pdId"));
		JbpmContext jbpmContext = WfContextBuilder.getJbpmContext()
				.getJbpmContext();
		ProcessDefinition processDefinition = jbpmContext.getGraphSession()
				.loadProcessDefinition(processDefinitionId);
		byte[] bytes = processDefinition.getFileDefinition().getBytes(
				"processimage.jpg");
		if (request.getParameter("tkId") != null) {
			Long taskId = Long.parseLong(request.getParameter("tkId"));
			
		}
		try (OutputStream out = response.getOutputStream()) {
			out.write(bytes);
			out.flush();
		}
	}
}
