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
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;

/**
 * Filtro que pega a sessão do JBPM e coloca-a no Hibernate.
 * 
 * @author kpf
 * 
 */
public class WfThreadFilter extends ThreadFilter {
	
	private static boolean fConfigured = false;

	private static final Object classLock = WfThreadFilter.class;
	
	private static final Logger log = Logger.getLogger( WfThreadFilter.class );

	/**
	 * Pega a sessão do JBPM e coloca-a no Hibernate.
	 */
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		final StringBuilder csv = super.iniciaAuditoria( request );
		
		this.configuraHibernate();

		try {
			
			this.executaFiltro( request, response, chain );

		} catch (final Exception ex) {
			WfDao.rollbackTransacao();
			super.logaExcecaoAoExecutarFiltro( request, ex );
			throw new ServletException(ex);
		} finally {
			this.fechaContextoWorkflow();
			this.fechaSessaoHibernate();
			this.liberaInstanciaDao();
		}
		
		super.terminaAuditoria( csv );
	}

	private void executaFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws Exception, AplicacaoException {
		
		HibernateUtil.getSessao();
		ModeloDao.freeInstance();
		WfDao.getInstance();
		Wf.getInstance().getConf().limparCacheSeNecessario();

		// Novo
		WfContextBuilder.getConfiguration();
		WfContextBuilder.createJbpmContext();

		// Novo
		if ( !WfDao.getInstance().sessaoEstahAberta() ) 
			throw new AplicacaoException(
					"Erro: sessão do Hibernate está fechada.");
		
		WfContextBuilder.getJbpmContext().getJbpmContext().setSession( WfDao.getInstance().getSessao() );

		// Velho
		// GraphSession s = WfContextBuilder.getJbpmContext()
		// .getGraphSession();
		// Field fld = GraphSession.class.getDeclaredField("session");
		// fld.setAccessible(true);
		// Session session = (Session) fld.get(s);
		// if (!session.isOpen())
		// throw new AplicacaoException(
		// "Erro: sessão do Hibernate está fechada.");
		// HibernateUtil.setSessao(session);
		// WfDao.getInstance();

		WfDao.iniciarTransacao();
		doFiltro( request, response, chain );
		WfDao.commitTransacao();
	}

	private void doFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain ) throws Exception {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			// TODO Verificar, pois que nem sempre que ocorre uma exceção no doFilter a mesma ocorreu por causa do timeout
			if ( !WfDao.getInstance().transacaoEstaAtiva() ) {
				throw new AplicacaoException("A aplicação não conseguiu efetuar a operação em tempo hábil.",0,e);
			}else{
				throw e;	
			}
		}
	}

	private void configuraHibernate() throws ExceptionInInitializerError {
		// Nato: usei um padrao de instanciacao de singleton para configurar a
		// sessionFactory do Hibernate
		// na primeira chamada ao filtro.
		if (!fConfigured) {
			synchronized (classLock) {
				if (!fConfigured) {
					try {
						Wf.getInstance();
						AnnotationConfiguration cfg = WfDao
								.criarHibernateCfg("java:/SigaWfDS");
						
						// bruno.lacerda@avantiprima.com.br
						// Configura listeners de auditoria de acordo com os parametros definidos no arquivo siga.auditoria.properties
						// SigaAuditor.configuraAuditoria( new SigaHibernateChamadaAuditor( cfg ) );
						
						// bruno.lacerda@avantiprima.com.br
						cfg.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JTATransactionFactory");
						cfg.setProperty("hibernate.transaction.manager_lookup_class", "org.hibernate.transaction.JBossTransactionManagerLookup");
						
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
	}
	
	private void fechaContextoWorkflow() {
		try {
			WfContextBuilder.closeContext();
		} catch (Exception ex) {
			log.error( "Ocorreu um erro ao fechar o contexto do Workflow", ex );
			ex.printStackTrace();
		}
	}
	
	private void fechaSessaoHibernate() {
		try {
			HibernateUtil.fechaSessaoSeEstiverAberta();
		} catch (Exception ex) {
			log.error( "Ocorreu um erro ao fechar uma sessão do Hibernate", ex );
			ex.printStackTrace();
		}
	}

	private void liberaInstanciaDao() {
		try {
			ModeloDao.freeInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
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
