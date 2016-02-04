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
/*
 * JBoss, Home of Professional Open Source Copyright 2005, JBoss Inc., and individual contributors as indicated by the @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this software; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package br.gov.jfrj.siga.wf.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.instantiation.Delegation;
import org.jbpm.taskmgmt.def.Swimlane;
import org.jbpm.taskmgmt.def.Task;

import br.gov.jfrj.siga.wf.util.WfContextBuilder;

/**
 * Servlet que recebe um arquivo de definição de processo (Process Definition) e
 * armazena-o no banco de dados. A definição de processo recebida passa a ser a
 * definição vigente.
 * 
 * @author kpf
 * 
 */
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Realiza o tratatemento do request. Verifica se o arquivo enviado é uma
	 * definição de processo válida e realiza o seu deploy.
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html");
		System.out.println("Publicando ...");
		response.getWriter().println(handleRequest(request));
	}

	/**
	 * Escreve o input stream (para debug)
	 * 
	 * @param request
	 * @throws IOException
	 */
	public void printInput(HttpServletRequest request) throws IOException {
		InputStream inputStream = request.getInputStream();
		StringBuffer buffer = new StringBuffer();
		int read;
		while ((read = inputStream.read()) != -1) {
			buffer.append((char) read);
		}
		log.debug(buffer.toString());
	}

	/**
	 * Verifica se o arquivo enviado é um process definition. Se for, realiza o
	 * seu deploy.
	 * 
	 * @param request
	 * @return
	 */
	private String handleRequest(HttpServletRequest request) {
		if (!FileUpload.isMultipartContent(request)) {
			log.debug("Not a multipart request");
			return "Not a multipart request";
		}
		try {
			// Nato: Modo novo, mas que esta voltando uma lista vazia pois o
			// webwork está interceptando o request e lendo o arquivo
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// Configure the factory here, if desired.
			ServletFileUpload upload = new ServletFileUpload(factory);
			// Configure the uploader here, if desired.
			List list = upload.parseRequest(request);

			// Nato: modo antigo
			// DiskFileUpload fileUpload = new DiskFileUpload();
			// List list = fileUpload.parseRequest(request);

			// MultiPartRequestWrapper req = (MultiPartRequestWrapper)request;
			// req.get

			Iterator iterator = list.iterator();
			if (!iterator.hasNext()) {
				log.debug("No process file in the request");
				return "No process file in the request";
			}
			FileItem fileItem = (FileItem) iterator.next();
			if (fileItem.getContentType().indexOf(
					"application/x-zip-compressed") == -1) {
				log.debug("Not a process archive");
				return "Not a process archive";
			}
			return doDeployment(fileItem);
		} catch (FileUploadException e) {
			e.printStackTrace();
			return "FileUploadException";
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error(e);
		}
	}

	/**
	 * Coloca as informações do arquivo da definição do processo no banco de
	 * dados.
	 * 
	 * @param fileItem
	 * @return
	 */
	private String doDeployment(FileItem fileItem) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream(fileItem
					.getInputStream());
			JbpmContext jbpmContext = WfContextBuilder.getJbpmContext()
					.getJbpmContext();
			ProcessDefinition processDefinition = ProcessDefinition
					.parseParZipInputStream(zipInputStream);
			jbpmContext.deployProcessDefinition(processDefinition);
			zipInputStream.close();
			long id = processDefinition.getId();

			String sReturn = "Deployed archive " + processDefinition.getName()
					+ " successfully";

			// ProcessDefinition pi = jbpmContext.getGraphSession()
			// .loadProcessDefinition(id);

			Delegation d = new Delegation(
					"br.gov.jfrj.siga.wf.util.WfAssignmentHandler");

			for (Swimlane s : ((Collection<Swimlane>) processDefinition
					.getTaskMgmtDefinition().getSwimlanes().values())) {
				if (s.getTasks() != null)
					for (Object t : s.getTasks()) {
						System.out.println(((Task) t).toString());
					}
				if (s.getAssignmentDelegation() == null)
					s.setAssignmentDelegation(d);
			}

			for (Task t : ((Collection<Task>) processDefinition
					.getTaskMgmtDefinition().getTasks().values())) {
				if (t.getSwimlane() == null
						&& t.getAssignmentDelegation() == null)
					t.setAssignmentDelegation(d);
			}

			return sReturn;
		} catch (IOException e) {
			return "IOException";
		}
	}

	private static Log log = LogFactory.getLog(UploadServlet.class);

}