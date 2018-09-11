package br.gov.jfrj.siga.wf.util;

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

public class WfHibernateUtil {

	private static Logger logger = Logger.getLogger(WfHibernateUtil.class);

	private static SessionFactory sessionFactory = null;
	private static ServiceRegistry serviceRegistry = null;

	public static Configuration conf = null;

	private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

	private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

	private WfHibernateUtil() {
	}

	public static Session getSessao() {

		Session s = WfHibernateUtil.threadSession.get();

		try {
			if (s == null) {
				s = WfHibernateUtil.sessionFactory.openSession();
				WfHibernateUtil.threadSession.set(s);
				WfHibernateUtil.logger
						.debug("Nova sessão do hibernate aberta.");
			} else {
				WfHibernateUtil.logger
						.debug("Retornada sessão do hibernate aberta anteriormente.");
			}

		} catch (final HibernateException he) {
			WfHibernateUtil.logger.error("erro do hibernate ao obter a sessão");
		}

		return s;
	}

	public static void setSessao(Session s) {
		WfHibernateUtil.threadSession.set(s);
	}

	public static void iniciarTransacao() {

		// bruno.lacerda@avantiprima.com.br
		// Resolucao do erro Session is Closed ao comitar uma transacao pois se
		// o ThreadTransaction
		// nao for nulo a threadTransaction pode ter sido obtida em uma sessao
		// do hibernate diferente
		// da atual e por este motivo a sessao pode estar fechada.

		Transaction tx = WfHibernateUtil.threadTransaction.get();
		// Transaction tx = WfHibernateUtil.getSessao().beginTransaction();

		if (tx == null) {
			tx = WfHibernateUtil.getSessao().beginTransaction();

			// bruno.lacerda@avantiprima.com.br
			try {
				String strTimeout = SigaBaseProperties
						.getString("jta.transaction.timeout.value");
				if (StringUtils.isNumeric(strTimeout)) {
					tx.setTimeout(Integer.parseInt(strTimeout));
				}
			} catch (Exception e) {
				tx.setTimeout(120);
			}

		}
		WfHibernateUtil.threadTransaction.set(tx);
	}

	public static void commitTransacao() throws AplicacaoException {
		final Transaction tx = WfHibernateUtil.threadTransaction.get();
		try {
			if ((tx != null && !tx.wasCommitted() && !tx.wasRolledBack())) {
				tx.commit();
				WfHibernateUtil.threadTransaction.set(null);
				// fechaSessao();
			}

		} catch (final HibernateException hbex) {
			WfHibernateUtil.rollbackTransacao();
			throw new AplicacaoException(
					"Erro gravando transação no banco de dados", 0, hbex);
		}
	}

	public static void rollbackTransacao() {
		final Transaction tx = WfHibernateUtil.threadTransaction.get();
		try {
			WfHibernateUtil.threadTransaction.set(null);
			if ((tx != null && !tx.wasCommitted() && !tx.wasRolledBack())) {
				tx.rollback();
			}
		} finally {
			WfHibernateUtil.fechaSessao();
		}

	}

	public synchronized static void fechaSessao() {

		final Session s = WfHibernateUtil.threadSession.get();

		if (s != null) {
			try {
				s.close();
			} catch (final HibernateException he) {
				WfHibernateUtil.logger
						.error("erro ao fechar a sessão hibernate");
				WfHibernateUtil.logger.error(he.getMessage());
			}
			try {
				WfHibernateUtil.threadSession.remove();
			} catch (final Exception he) {
				WfHibernateUtil.logger
						.error("erro ao remover a sessão hibernate");
				WfHibernateUtil.logger.error(he.getMessage());
			}
		}

	}

	public static void removeSessao() {
		WfHibernateUtil.threadSession.remove();
	}

	public static void fechaSessaoSeEstiverAberta() {
		final Session s = WfHibernateUtil.threadSession.get();
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

	public static void configurarHibernate(String resource,
			Class<?>... classesAnotadas) {
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
				conf.setProperty("hibernate.connection.url",
						hibernateConnectionUrl);

			if (fUseDatasource != null) {

				if (fUseDatasource) {

					conf.setProperty("connection.datasource",
							conf.getProperty("siga.connection.datasource"));

					conf.getProperties().remove("hibernate.connection.url");
					conf.getProperties()
							.remove("hibernate.connection.username");
					conf.getProperties()
							.remove("hibernate.connection.password");
					conf.getProperties().remove(
							"hibernate.connection.driver_class");

				} else {

					conf.getProperties().remove("connection.datasource");

					conf.setProperty("hibernate.connection.url",
							conf.getProperty("siga.hibernate.connection.url"));
					conf.setProperty("hibernate.connection.username", conf
							.getProperty("siga.hibernate.connection.username"));
					conf.setProperty("hibernate.connection.password", conf
							.getProperty("siga.hibernate.connection.password"));
					conf.setProperty(
							"hibernate.connection.driver_class",
							conf.getProperty("siga.hibernate.connection.driver_class"));
				}
			}

			if (hibernateConnectionUrl != null
					&& hibernateConnectionUrl.equals("test")) {

				conf.getProperties().remove("connection.datasource");

				conf.setProperty("hibernate.connection.url",
						conf.getProperty("siga.test.hibernate.connection.url"));
				conf.setProperty("hibernate.connection.username", conf
						.getProperty("siga.test.hibernate.connection.username"));
				conf.setProperty("hibernate.connection.password", conf
						.getProperty("siga.test.hibernate.connection.password"));
				conf.setProperty(
						"hibernate.connection.driver_class",
						conf.getProperty("siga.test.hibernate.connection.driver_class"));
			}

			sessionFactory = conf.buildSessionFactory();

		} catch (final Throwable ex) {

			// Make sure you log the exception, as it might be swallowed
			System.out.println("WfHibernateUtil");
			ex.printStackTrace();
			WfHibernateUtil.logger.error(
					"Não foi possível configurar o hibernate.", ex);

			throw new ExceptionInInitializerError(ex);
		}
	}

	public static void configurarHibernate(Configuration configuration) {

		try {
			conf = configuration;
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (final Throwable ex) {

			// Make sure you log the exception, as it might be swallowed
			System.out.println("WfHibernateUtil");
			ex.printStackTrace();
			WfHibernateUtil.logger.error(
					"Não foi possível configurar o hibernate.", ex);

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

	public static Configuration criarConfiguracao(String resource,
			Class<?>... classesAnotadas) {

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
			WfHibernateUtil.logger.error(
					"Não foi possível configurar o hibernate.", ex);

			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null && getSessao() != null) {
			configurarHibernate(WfHibernateUtil.getSessao()); // Corrigindo erro
																// de
																// nullpointer
		}
		return sessionFactory;
	}

	// private static void configurarHibernateParaDebug(Configuration cfg) {
	// boolean isDebugHibernateHabilitado =
	// SigaBaseProperties.getBooleanValue("configura.hibernate.para.debug");
	// if ( isDebugHibernateHabilitado ) {
	// ModeloDao.configurarHibernateParaDebug( cfg );
	// }
	// }

}