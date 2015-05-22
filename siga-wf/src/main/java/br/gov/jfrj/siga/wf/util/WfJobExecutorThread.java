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

import java.lang.reflect.Field;

import javax.servlet.ServletException;

import org.hibernate.Session;
import org.jbpm.db.GraphSession;
import org.jbpm.job.Job;
import org.jbpm.job.executor.JobExecutorThread;

import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;

/**
 * Classe que representa um thread de execu��o de job
 * 
 * @author kpf
 * 
 */
public class WfJobExecutorThread extends JobExecutorThread {

	/**
	 * Construtor.
	 * 
	 * @param name
	 * @param jobExecutor
	 */
	public WfJobExecutorThread(String threadName, WfJobExecutor wfJobExecutor) {
		super(threadName, wfJobExecutor);
	}

	/**
	 * Executa o job.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void executeJob(Job job) throws Exception {
		GraphSession s = WfContextBuilder.getJbpmContext().getGraphSession();
		Session session = null;
		try {
			Field fld = GraphSession.class.getDeclaredField("session");
			fld.setAccessible(true);
			session = (Session) fld.get(s);

			if (session == null)
				throw new Exception(
						"Não foi possível obter a sessão do hibernate para executar o timer.");

			HibernateUtil.setSessao(session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WfDao.getInstance();

		super.executeJob(job);

		WfContextBuilder.closeContext();

		HibernateUtil.removeSessao();
		ModeloDao.freeInstance();

		// WfExecutionEnvironment ee = new WfExecutionEnvironment();
		// try {
		// Wf.setInstance(null);
		// ee.antes(session);
		// super.executeJob(job);
		// ee.depois();
		// } catch (Exception e) {
		// ee.excecao();
		// throw new ServletException(e);
		// } finally {
		// ee.finalmente();
		// }
	}
}
