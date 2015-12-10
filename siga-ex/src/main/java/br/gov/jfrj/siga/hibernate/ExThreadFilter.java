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

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.auditoria.filter.ThreadFilter;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class ExThreadFilter extends ThreadFilter {

	private static final Logger log = Logger.getLogger("br.gov.jfrj.siga.ex");

	/**
	 * Pega a sessão.
	 */
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		final StringBuilder csv = super.iniciaAuditoria(request);

		try {
			this.executaFiltro(request, response, chain);

		} catch (final Exception ex) {
			ExDao.rollbackTransacao();
			if (ex instanceof RuntimeException)
				throw (RuntimeException) ex;
			else
				throw new ServletException(ex);
		} finally {
			this.fechaSessaoHibernate();
			this.liberaInstanciaDao();
			Thread.interrupted();			
		}

		super.terminaAuditoria(csv);

	}

	private void executaFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws Exception, AplicacaoException {

		// HibernateUtil.getSessao();
		ModeloDao.freeInstance();
		ExDao.getInstance();
		Ex.getInstance().getConf().limparCacheSeNecessario();

		// Novo
		if (!ExDao.getInstance().sessaoEstahAberta())
			throw new AplicacaoException("Erro: sessão do Hibernate está fechada.");

		ExDao.iniciarTransacao();
		doFiltro(request, response, chain);
		ExDao.commitTransacao();
	}

	private void doFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws Exception {

		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			StringBuffer caminho = new StringBuffer();
			String parametros = "";
			if (request instanceof HttpServletRequest){
				HttpServletRequest httpReq = (HttpServletRequest)request;
				caminho = httpReq.getRequestURL();
				parametros = httpReq.getQueryString()==null?"":"?" + httpReq.getQueryString();
				caminho.append(parametros);
			}
			log.info("Ocorreu um erro durante a execução da operação: "+ e.getMessage() 
					+ "\ncaminho: " + caminho.toString());

			throw e;
		}
	}

	private void fechaSessaoHibernate() {
		try {
			HibernateUtil.fechaSessaoSeEstiverAberta();
		} catch (Exception ex) {
			log.error("Ocorreu um erro ao fechar uma sessão do Hibernate", ex);
		}
	}

	private void liberaInstanciaDao() {
		try {
			ExDao.freeInstance();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
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
