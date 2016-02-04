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
import org.jbpm.job.executor.JobExecutor;

/**
 * Classe que representa o executor de jobs do JBPM. Esta classe é definida em
 * siga-wf/src/jbpm.cfg.xml.
 * 
 * @author kpf
 * 
 */
public class WfJobExecutor extends JobExecutor {

	/**
	 * Cria uma thread para a execução de job.
	 */
	@Override
	protected Thread createThread(String threadName) {
		Thread t = new WfJobExecutorThread(threadName, this);
		
		if (t.getContextClassLoader() != Thread.currentThread().getContextClassLoader())
			t.setContextClassLoader(Thread.currentThread().getContextClassLoader());
		return t;
	}

}
