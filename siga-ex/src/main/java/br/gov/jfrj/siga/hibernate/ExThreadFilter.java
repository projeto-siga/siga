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

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class ExThreadFilter implements Filter {

	private FilterConfig config;

	@Override
	public void init(FilterConfig config) {
		this.config = config;
	}

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		EntityManager em = ExStarter.emf.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		// Inicialização padronizada
		CurrentRequest.set(new RequestInfo(config.getServletContext(), (HttpServletRequest) request,
				(HttpServletResponse) response));
		ModeloDao.freeInstance();
		ExDao.getInstance();
		try {
			Ex.getInstance().getConf().limparCacheSeNecessario();
		} catch (Exception e1) {
			throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
		}

		em.getTransaction().begin();

		try {
			chain.doFilter(request, response);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();

			throw new ServletException(e);
		} finally {
			em.close();
			ContextoPersistencia.setEntityManager(null);
		}
	}

	@Override
	public void destroy() {
	}
}
