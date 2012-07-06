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
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class HibernateThreadFilter extends ThreadFilter {

	private static boolean fConfigured = false;

	private static final Object classLock = HibernateThreadFilter.class;
	
	private static final Logger log = Logger.getLogger( HibernateThreadFilter.class );
	
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

		StringBuilder csv = super.iniciaAuditoria( request );
		
		this.configuraHibernate();
		
		try {
			
			this.executaFiltro( request, response, chain );
			
		} catch (final Throwable ex) {
			this.efetuaRollbackTransacao( ex );
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
						log.error( "Não foi possível configurar o hibernate. ", ex );
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
		CpDao.getInstance();
		Cp.getInstance().getConf().limparCacheSeNecessario();
		
		if ( !CpDao.getInstance().sessaoEstahAberta() )
			throw new AplicacaoException(
					"Erro: sessão do Hibernate está fechada.");
		
		CpDao.iniciarTransacao();
		doFiltro( request, response, chain );
		CpDao.commitTransacao();
	}

	private void doFiltro(final ServletRequest request,
			final ServletResponse response, final FilterChain chain ) throws AplicacaoException {
		try {
			chain.doFilter(request, response);
		} catch (Throwable e) {
			if ( !CpDao.getInstance().transacaoEstaAtiva() ) {
				throw new AplicacaoException( "A aplicação não conseguiu efetuar a operação em tempo hábil." );
			}
		}
	}
	
	private void efetuaRollbackTransacao(final Throwable ex) throws ServletException {
		CpDao.rollbackTransacao();
		log.error( ex.getMessage(), ex );
		ex.printStackTrace();
		throw new ServletException(ex);
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
			CpDao.freeInstance();
		} catch (Exception ex) {
			log.error( ex.getMessage(), ex );
			ex.printStackTrace();
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
