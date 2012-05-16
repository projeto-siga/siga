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
package br.gov.jfrj.siga.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.cfg.AnnotationConfiguration;

import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaAuditor;
import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaHibernateChamadaAuditor;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class HibernateThreadFilter implements Filter {

	private static Log log = LogFactory.getLog(HibernateThreadFilter.class);

	private static boolean fConfigured = false;

	private static final Object classLock = HibernateThreadFilter.class;
	
	// static {
	// try {
	// HibernateUtil.configurarHibernate("/br/gov/jfrj/siga/hibernate/hibernate.cfg.xml");
	// } catch (final Throwable ex) {
	// // Make sure you log the exception, as it might be swallowed
	// log.error("Não foi possível configurar o hibernate.", ex);
	// throw new ExceptionInInitializerError(ex);
	// }
	// }

	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		// Nato: usei um padrao de instanciacao de singleton para configurar a
		// sessionFactory do Hibernate
		// na primeira chamada ao filtro.
		if (!fConfigured) {
			synchronized (classLock) {
				if (!fConfigured) {
					try {
						// TODO: _LAGS - Trocar para obter os parametros do "web.xml"
						AnnotationConfiguration cfg = CpDao
								.criarHibernateCfg("java:/SigaCpDS");
						
						// bruno.lacerda@avantiprima.com.br
						// Configura listeners de auditoria de acordo com os parametros definidos no arquivo siga.auditoria.properties
						SigaAuditor.configuraAuditoria( new SigaHibernateChamadaAuditor( cfg ) );
						
						HibernateUtil.configurarHibernate(cfg, "");
						fConfigured = true;
					} catch (final Throwable ex) {
						// Make sure you log the exception, as it might be
						// swallowed
						log.error("Não foi possível configurar o hibernate.",
								ex);
						throw new ExceptionInInitializerError(ex);
					}
				}
			}
		}
		
		try {
			HibernateUtil.getSessao();
			ModeloDao.freeInstance();
			CpDao.getInstance();
			Cp.getInstance().getConf().limparCacheSeNecessario();
			HibernateThreadFilter.log.debug("Starting a database transaction");
			// HibernateUtil.iniciarTransacao();

			// Call the next filter (continue request processing)
			chain.doFilter(request, response);

			// Commit and cleanup
			HibernateThreadFilter.log
					.debug("Committing the database transaction");
			// HibernateUtil.commitTransacao();
			HibernateUtil.fechaSessao();
		} catch (final StaleObjectStateException staleEx) {
			HibernateThreadFilter.log
					.error("This interceptor does not implement optimistic concurrency control!");
			HibernateThreadFilter.log
					.error("Your application will not work until you add compensation actions!");
			// Rollback, close everything, possibly compensate for any permanent
			// changes
			// during the conversation, and finally restart business
			// conversation. Maybe
			// give the user of the application a chance to merge some of his
			// work with
			// fresh data... what you do here depends on your applications
			// design.
			throw staleEx;
		} catch (final Throwable ex) {
			// Rollback only
			ex.printStackTrace();
			try {
				// if
				// (HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().isActive())
				// {
				HibernateThreadFilter.log
						.debug("Trying to rollback database transaction after exception");
				HibernateUtil.rollbackTransacao();
				// }
			} catch (final Throwable rbEx) {
				HibernateThreadFilter.log
						.error(
								"Could not rollback transaction after exception!",
								rbEx);
			}

			// Let others handle it... maybe another interceptor for exceptions?
			throw new ServletException(ex);
		} finally {
			CpDao.freeInstance();
		}
	}

	public void init(final FilterConfig filterConfig) throws ServletException {
		HibernateThreadFilter.log
				.debug("Initializing filter, obtaining Hibernate SessionFactory from HibernateUtil");
		// sf = HibernateUtil.getSessionFactory();
	}

	public void destroy() {
	}

}
