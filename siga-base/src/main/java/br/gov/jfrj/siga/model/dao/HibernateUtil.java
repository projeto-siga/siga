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

/*
 * Criado em  01/12/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.model.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaBaseProperties;

public class HibernateUtil {

	private static Logger logger = Logger.getLogger(HibernateUtil.class);

	private static SessionFactory sessionFactory = null;
	private static ServiceRegistry serviceRegistry = null;

	public static Configuration conf = null;

	private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

	private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

	private HibernateUtil() {}

	public static Session getSessao() {
		
		Session s = HibernateUtil.threadSession.get();

		try {
			if (s == null) {
				s = HibernateUtil.sessionFactory.openSession();
				HibernateUtil.threadSession.set(s);
				HibernateUtil.logger.debug("Nova sessão do hibernate aberta.");
			} else {
				HibernateUtil.logger.debug("Retornada sessão do hibernate aberta anteriormente.");
			}
			
		} catch (final HibernateException he) {
			HibernateUtil.logger.error("erro do hibernate ao obter a sessão");
		}
		
		return s;
	}

	public static void setSessao(Session s) {
		HibernateUtil.threadSession.set(s);
	}

	public static void iniciarTransacao() {
		
		// bruno.lacerda@avantiprima.com.br
		// Resolucao do erro Session is Closed ao comitar uma transacao pois se o ThreadTransaction
		// nao for nulo a threadTransaction pode ter sido obtida em uma sessao do hibernate diferente 
		// da atual e por este motivo a sessao pode estar fechada.

		Transaction tx = HibernateUtil.threadTransaction.get();
		//Transaction tx = HibernateUtil.getSessao().beginTransaction();
		
		if (tx == null) {
			tx = HibernateUtil.getSessao().beginTransaction();
			
			// bruno.lacerda@avantiprima.com.br
			try{
				String strTimeout = SigaBaseProperties.getString( "jta.transaction.timeout.value" );
				if ( StringUtils.isNumeric( strTimeout ) ) {
					tx.setTimeout( Integer.parseInt( strTimeout ) );
				}
			} catch(Exception e) {
				tx.setTimeout(120);
			}
			
			
		}
		HibernateUtil.threadTransaction.set(tx);
	}

	public static void commitTransacao() throws AplicacaoException {
		final Transaction tx = HibernateUtil.threadTransaction.get();
		try {
			if ((tx != null && !tx.wasCommitted() && !tx.wasRolledBack())) {
				tx.commit();
				HibernateUtil.threadTransaction.set(null);
				// fechaSessao();
			}

		} catch (final HibernateException hbex) {
			HibernateUtil.rollbackTransacao();
			throw new AplicacaoException(
					"Erro gravando transação no banco de dados", 0, hbex);
		}
	}

	public static void rollbackTransacao() {
		final Transaction tx = HibernateUtil.threadTransaction.get();
		try {
			HibernateUtil.threadTransaction.set(null);
			if ((tx != null && !tx.wasCommitted() && !tx.wasRolledBack())) {
				tx.rollback();
			}
		} finally {
			HibernateUtil.fechaSessao();
		}

	}

	public synchronized static void fechaSessao() {
		
		final Session s = HibernateUtil.threadSession.get();

		if (s != null) {
			try {
				s.close();
			} catch (final HibernateException he) {
				HibernateUtil.logger.error("erro ao fechar a sessão hibernate");
				HibernateUtil.logger.error(he.getMessage());
			}
			try {
				HibernateUtil.threadSession.remove();
			} catch (final Exception he) {
				HibernateUtil.logger
						.error("erro ao remover a sessão hibernate");
				HibernateUtil.logger.error(he.getMessage());
			}
		}

	}

	public static void removeSessao() {
		HibernateUtil.threadSession.remove();
	}

	public static void fechaSessaoSeEstiverAberta() {
		final Session s = HibernateUtil.threadSession.get();
		if (s != null && s.isOpen())
			fechaSessao();
	}
	
	public static Configuration getConf() {
		return conf;
	}
	
	public static void configurarHibernate(String resource) {
		Class<?> c = null;
		configurarHibernate(resource, c);
	}
	
	public static void configurarHibernate(String resource,	Class<?>... classesAnotadas) {
		configurarHibernate(resource, null, classesAnotadas);
	}
	
	public static void configurarHibernate(String resource,
			String hibernateConnectionUrl, Class<?>... classesAnotadas) {
		
		try {
			
			Boolean fUseDatasource = null;
			String sUseDatasource = System.getProperty("siga.use.datasource");
			
			if (sUseDatasource != null) {
				fUseDatasource = Boolean.valueOf(sUseDatasource);
			}

			if (classesAnotadas != null)
				conf = criarConfiguracao(resource, classesAnotadas);
			else
				conf = criarConfiguracao(resource);
			
			if (hibernateConnectionUrl != null)
				conf.setProperty("hibernate.connection.url", hibernateConnectionUrl);
			
			if (fUseDatasource != null) {
				
				if (fUseDatasource) {
					
					conf.setProperty("connection.datasource", conf.getProperty("siga.connection.datasource"));
					
					conf.getProperties().remove("hibernate.connection.url");
					conf.getProperties().remove("hibernate.connection.username");
					conf.getProperties().remove("hibernate.connection.password");
					conf.getProperties().remove("hibernate.connection.driver_class");
					
				} else {
					
					conf.getProperties().remove("connection.datasource");
					
					conf.setProperty("hibernate.connection.url", conf.getProperty("siga.hibernate.connection.url"));
					conf.setProperty("hibernate.connection.username", conf.getProperty("siga.hibernate.connection.username"));
					conf.setProperty("hibernate.connection.password", conf.getProperty("siga.hibernate.connection.password"));
					conf.setProperty("hibernate.connection.driver_class",conf.getProperty("siga.hibernate.connection.driver_class"));
				}
			}

			if (hibernateConnectionUrl != null
					&& hibernateConnectionUrl.equals("test")) {
				
				conf.getProperties().remove("connection.datasource");
				
				conf.setProperty("hibernate.connection.url", conf.getProperty("siga.test.hibernate.connection.url"));
				conf.setProperty("hibernate.connection.username", conf.getProperty("siga.test.hibernate.connection.username"));
				conf.setProperty("hibernate.connection.password", conf.getProperty("siga.test.hibernate.connection.password"));
				conf.setProperty("hibernate.connection.driver_class", conf.getProperty("siga.test.hibernate.connection.driver_class"));
			}
			
			sessionFactory = conf.buildSessionFactory();
			
		} catch (final Throwable ex) {
			
			// Make sure you log the exception, as it might be swallowed
			System.out.println("HibernateUtil");
			ex.printStackTrace();
			HibernateUtil.logger.error("Não foi possível configurar o hibernate.", ex);
			
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static void configurarHibernate(Configuration configuration) {
		
		try {
			conf = configuration;
			serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (final Throwable ex) {
			
			// Make sure you log the exception, as it might be swallowed
			System.out.println("HibernateUtil");
			ex.printStackTrace();
			HibernateUtil.logger.error("Não foi possível configurar o hibernate.", ex);
			
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	/**
	 * @param resource
	 * @return
	 */
	public static Configuration criarConfiguracao(String resource) {
		Configuration conf = new Configuration();
		conf.configure(resource);
		
		return conf;
	}

	public static Configuration criarConfiguracao(String resource, Class<?>... classesAnotadas) {
		
		Configuration conf = new Configuration();
		
		for (Class<?> clazz : classesAnotadas) {
			conf.addAnnotatedClass(clazz);
		}
		
		// conf.addResource(resource);
		conf.configure(resource);
		
		return conf;
	}

	public static void configurarHibernate(Session session) {
		
		try {
			sessionFactory = session.getSessionFactory();
		} catch (final Throwable ex) {
			
			// Make sure you log the exception, as it might be swallowed
			HibernateUtil.logger.error("Não foi possível configurar o hibernate.", ex);
			
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null && getSessao() != null){
			configurarHibernate(HibernateUtil.getSessao());  //Corrigindo erro de nullpointer
		}
		return sessionFactory;
	}
	
//	private static void configurarHibernateParaDebug(Configuration cfg) {
//		boolean isDebugHibernateHabilitado = SigaBaseProperties.getBooleanValue("configura.hibernate.para.debug");
//		if ( isDebugHibernateHabilitado ) {
//			ModeloDao.configurarHibernateParaDebug( cfg );
//		}
//	}

}
