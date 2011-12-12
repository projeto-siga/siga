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
package br.gov.jfrj.siga.hibernate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.cfg.AnnotationConfiguration;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class ExThreadFilter implements Filter {

	private static boolean fConfigured = false;

	private static final Object classLock = ExThreadFilter.class;

	/**
	 * Pega a sessão.
	 */
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
						Ex.getInstance();
						AnnotationConfiguration cfg = ExDao
								.criarHibernateCfg("java:/SigaExDS");
						HibernateUtil.configurarHibernate(cfg, "");
						fConfigured = true;
					} catch (final Throwable ex) {
						// Make sure you log the exception, as it might be
						// swallowed
						// log.error("Não foi possível configurar o hibernate.",
						// ex);
						throw new ExceptionInInitializerError(ex);
					}
				}

			}
		}

		try {
			HibernateUtil.getSessao();
			ModeloDao.freeInstance();
			ExDao.getInstance();
			Ex.getInstance().getConf().limparCacheSeNecessario();

			// Novo
			Session session = ExDao.getInstance().getSessao();
			if (!session.isOpen())
				throw new AplicacaoException(
						"Erro: sessão do Hibernate está fechada.");

			ExDao.iniciarTransacao();

			try {
				chain.doFilter(request, response);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
			ExDao.commitTransacao();

		} catch (final Throwable ex) {
			ExDao.rollbackTransacao();
			ex.printStackTrace();
			throw new ServletException(ex);
		} finally {
			try {
				HibernateUtil.fechaSessaoSeEstiverAberta();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				ModeloDao.freeInstance();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Executa ao destruir o filtro.
	 */
	public void destroy() {

	}

	/**
	 * Executa ao inciar o filtro.
	 */
	public void init(FilterConfig arg0) throws ServletException {

	}
}
