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

import org.jbpm.JbpmConfiguration;
import org.jbpm.persistence.JbpmPersistenceException;

/**
 * Classe que representa um construtor de contexto do sistema de workflow.
 * 
 * @author kpf
 * 
 */
public class WfContextBuilder {

	private static final ThreadLocal<WfJbpmContext> localContext = new ThreadLocal<WfJbpmContext>();
	private static JbpmConfiguration configuration = null;

	/**
	 * Retorna uma configuração do JBPM.
	 * 
	 * @return
	 */
	public static JbpmConfiguration getConfiguration() {
		return configuration;
	}

	public static void setConfiguration(JbpmConfiguration config) {
		if (configuration == null)
			configuration = config;
	}

	/**
	 * Cria uma nova configuração do JBPM.
	 */
	public static void createJbpmContext() {
		if (localContext.get() == null)
			localContext.set(new WfJbpmContext());
	}

	/**
	 * Retorna o contexto do JBPM.
	 * 
	 * @return
	 */
	public static WfJbpmContext getJbpmContext() {
		createJbpmContext();
		return localContext.get();
	}

	/**
	 * Fecha o contexto do JBPM.
	 */
	public static void closeContext() {
		WfJbpmContext context = getJbpmContext();
		try {
			context.getJbpmContext().close();
		} catch (JbpmPersistenceException e) {
			// Nato: swallow can't rollback exception
			if (!"cannot honor rollback request under external transaction manager"
					.equals(e.getMessage())) {
				throw e;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		localContext.remove();
	}

}
