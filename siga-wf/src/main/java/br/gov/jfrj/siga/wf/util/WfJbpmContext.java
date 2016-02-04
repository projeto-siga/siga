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
package br.gov.jfrj.siga.wf.util;




import org.jbpm.JbpmContext;
import org.jbpm.db.GraphSession;

/**
 * Classe que representa o contexto do JBPM.
 * @author kpf
 *
 */
public class WfJbpmContext {

	protected JbpmContext jbpmContext;
	protected GraphSession graphSession;

	/**
	 * Construtor.
	 */
	public WfJbpmContext() {
		super();
	}

	/**
	 * Retorna o contexto do JBPM.
	 * @return
	 */
	public JbpmContext getJbpmContext() {
		if (jbpmContext == null)
			jbpmContext = WfContextBuilder.getConfiguration().createJbpmContext();
		return jbpmContext;
	}

	/**
	 * Retorna a sessão do grafo.
	 * @return
	 */
	public GraphSession getGraphSession() {
		if (graphSession == null)
			graphSession = getJbpmContext().getGraphSession();
		return graphSession;
	}

}
