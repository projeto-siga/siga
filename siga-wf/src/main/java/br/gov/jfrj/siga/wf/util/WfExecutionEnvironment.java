package br.gov.jfrj.siga.wf.util;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;

public class WfExecutionEnvironment {
	private static final Logger log = Logger
			.getLogger(WfExecutionEnvironment.class);

	private void fechaContextoWorkflow() {
		try {
			WfContextBuilder.closeContext();
		} catch (Exception ex) {
			log.error(
					"[fechaContextoWorkflow] - Ocorreu um erro ao fechar o contexto do Workflow",
					ex);
			// ex.printStackTrace();
		}
	}

	private void liberaInstanciaDao() {
		try {
			ModeloDao.freeInstance();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			// ex.printStackTrace();
		}
	}

	public void registerTransactionClasses(Configuration cfg) {
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

	public void finalmente() {
		this.fechaContextoWorkflow();
		ContextoPersistencia.em().close();
		ContextoPersistencia.setEntityManager(null);
		this.liberaInstanciaDao();
	}

	public void excecao() {
		if (ContextoPersistencia.em().getTransaction().isActive())
			ContextoPersistencia.em().getTransaction().rollback();
	}

	public void depois() {
		ContextoPersistencia.em().getTransaction().commit();
	}

	public void antes(Session session) throws Exception {
		EntityManager em = ContextoPersistencia.emf.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		em.getTransaction().begin();

		ModeloDao.freeInstance();
		WfDao.getInstance();
		Wf.getInstance().getConf().limparCacheSeNecessario();

		// Novo
		if (session == null) {
			WfContextBuilder.getConfiguration();
			WfContextBuilder.createJbpmContext();
		}

		// Novo
		if (!WfDao.getInstance().sessaoEstahAberta())
			throw new AplicacaoException(
					"Erro: sessão do Hibernate está fechada.");

		if (session == null)
			WfContextBuilder.getJbpmContext().getJbpmContext()
					.setSession(WfDao.getInstance().getSessao());
	}

}
