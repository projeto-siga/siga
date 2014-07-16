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

import org.hibernate.SessionFactory;
import org.jbpm.JbpmConfiguration;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.wf.dao.WfDao;

/**
 * Classe que representa um construtor de contexto do sistema de workflow.
 * 
 * @author kpf
 * 
 */
public class WfContextBuilder {

	private static final ThreadLocal<WfJbpmContext> localContext = new ThreadLocal<WfJbpmContext>();

	private static JbpmConfiguration configuration = null;

	private static boolean fConfigured = false;

	private static final Object classLock = WfContextBuilder.class;

	/**
	 * Retorna uma configuração do JBPM.
	 * 
	 * @return
	 */
	public static JbpmConfiguration getConfiguration() {
		if (!fConfigured) {
			synchronized (classLock) {
				if (!fConfigured) {
					try {
						configuration = JbpmConfiguration.getInstance("jbpm.cfg.xml");
						SessionFactory fabricaDeSessao = WfDao.getInstance().getFabricaDeSessao();
						if (fabricaDeSessao == null)
							throw new AplicacaoException("Não foi possivel obter uma configuração do Jbpm pois a sessionFactory do Hibernate não estava criada.");
						
						configuration.createJbpmContext().setSessionFactory(fabricaDeSessao);
						configuration.startJobExecutor();
						fConfigured = true;
						return configuration;
					} catch (final Throwable ex) {
						// Make sure you log the exception, as it might be
						// swallowed
						// log.error("Não foi possível configurar o hibernate.",
						// ex);
						throw new RuntimeException(ex.getMessage());
					}
				}
			}
		}
		return configuration;
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		localContext.remove();
	}

}
