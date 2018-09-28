package br.gov.jfrj.siga.wf.vraptor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.com.caelum.vraptor.ioc.ComponentFactory;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfHibernateUtil;

//@Component
//@ApplicationScoped
public class WfSessionFactoryCreator implements
		ComponentFactory<SessionFactory> {

	private SessionFactory sessionFactory = null;
	private Configuration conf = null;

	public void configurarHibernate(Configuration cfg,
			String hibernateConnectionUrl, Class<?>... classesAnotadas) {
		try {

			for (Class<?> clazz : classesAnotadas) {
				cfg.addAnnotatedClass(clazz);
			}

			// bruno.lacerda@avantiprima.com.br
			// configurarHibernateParaDebug(cfg);

			// cfg.configure();
			this.conf = cfg;
			this.sessionFactory = this.conf.buildSessionFactory();

		} catch (final Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static void configurarHibernateParaDebug(Configuration cfg) {
		boolean isDebugHibernateHabilitado = SigaBaseProperties
				.getBooleanValue("configura.hibernate.para.debug");
		if (isDebugHibernateHabilitado) {
			// ModeloDao.configurarHibernateParaDebug(cfg);
		}
	}

	private void registerTransactionClasses(Configuration cfg) {
		// bruno.lacerda@avantiprima.com.br
		this.registerTransactionClass("hibernate.transaction.factory_class",
				cfg);
		this.registerTransactionClass(
				"hibernate.transaction.manager_lookup_class", cfg);
	}

	private void registerTransactionClass(String propertyName, Configuration cfg) {
		String transactionFactoryClassName = System.getProperty(propertyName);
		if (transactionFactoryClassName != null
				&& transactionFactoryClassName.trim().length() > 0) {
			cfg.setProperty(propertyName, transactionFactoryClassName);
		}
	}

	public WfSessionFactoryCreator() {
		super();
	}

	@PostConstruct
	public void create() throws ExceptionInInitializerError {
		try {
			Wf.getInstance();
			Configuration cfg = WfDao
					.criarHibernateCfg("java:/jboss/datasources/SigaWfDS");

			registerTransactionClasses(cfg);

			WfHibernateUtil.configurarHibernate(cfg);
		} catch (final Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public SessionFactory getInstance() {
		return sessionFactory;
	}

	@PreDestroy
	public void destroy() {
		this.sessionFactory.close();
	}

}