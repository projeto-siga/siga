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

import org.apache.log4j.Logger;
import org.hibernate.cfg.AnnotationConfiguration;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.auditoria.filter.ThreadFilter;
import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaAuditor;
import br.gov.jfrj.siga.base.auditoria.hibernate.auditor.SigaHibernateChamadaAuditor;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class ExThreadFilter extends ThreadFilter {

	private static boolean fConfigured = false;

	private static final Object classLock = ExThreadFilter.class;
	
	private static final Logger log = Logger.getLogger( ExThreadFilter.class );
	
	/**
	 * Pega a sessão.
	 */
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		
		final StringBuilder csv = super.iniciaAuditoria( request );
		
		this.configuraHibernate();
		
		try {
			
			this.executaFiltro( request, response, chain );

		} catch (final Exception ex) {
			 ExDao.rollbackTransacao();
             super.logaExcecaoAoExecutarFiltro( request, ex );
             throw new ServletException(ex);
		} finally {
			this.fechaSessaoHibernate();
			this.liberaInstanciaDao();
		}
		
		super.terminaAuditoria( csv );
		
	}

	private void configuraHibernate() throws ExceptionInInitializerError {
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

						// bruno.lacerda@avantiprima.com.br
						// Configura listeners de auditoria de acordo com os parametros definidos no arquivo siga.auditoria.properties
						SigaAuditor.configuraAuditoria( new SigaHibernateChamadaAuditor( cfg ) );
						
						registerTransactionClasses(cfg);
						
						HibernateUtil.configurarHibernate(cfg, "");
						fConfigured = true;
					} catch (final Throwable ex) {
						// Make sure you log the exception, as it might be
						// swallowed
						// ex);
						log.error( "Não foi possível configurar o Hibernate. ", ex );
						ex.printStackTrace();
						throw new ExceptionInInitializerError(ex);
					}
				}
			}
		}
	}

	private void executaFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws Exception, AplicacaoException {
		
		HibernateUtil.getSessao();
		ModeloDao.freeInstance();
		ExDao.getInstance();
		Ex.getInstance().getConf().limparCacheSeNecessario();

		// Novo
		if ( !ExDao.getInstance().sessaoEstahAberta() )
			throw new AplicacaoException(
					"Erro: sessão do Hibernate está fechada.");

		ExDao.iniciarTransacao();
		doFiltro( request, response, chain );
		ExDao.commitTransacao();
	}

	private void doFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain) throws Exception {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			// TODO Verificar, pois que nem sempre que ocorre uma exceção no doFilter a mesma ocorreu por causa do timeout
			if ( !ExDao.getInstance().transacaoEstaAtiva() ) {
				throw new AplicacaoException("A aplicação não conseguiu efetuar a operação em tempo hábil.",0,e);
			}else{
				throw e;	
			}
		}
	}
	
	private void fechaSessaoHibernate() {
		try {
			HibernateUtil.fechaSessaoSeEstiverAberta();
		} catch (Exception ex) {
			log.error( "Ocorreu um erro ao fechar uma sessão do Hibernate", ex );
			// ex.printStackTrace();
		}
	}
	
	private void liberaInstanciaDao() {
		try {
			ExDao.freeInstance();
		} catch (Exception ex) {
			log.error( ex.getMessage(), ex );
			// ex.printStackTrace();
		}
	}

	/**
	 * Executa ao destruir o filtro.
	 */
	public void destroy() {}

	/**
	 * Executa ao inciar o filtro.
	 */
	public void init(FilterConfig arg0) throws ServletException {}
}
