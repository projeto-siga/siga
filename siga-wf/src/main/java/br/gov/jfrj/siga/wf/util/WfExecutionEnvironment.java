package br.gov.jfrj.siga.wf.util;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.base.AplicacaoException;
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

	private void fechaSessaoHibernate() {
		try {
			WfHibernateUtil.fechaSessaoSeEstiverAberta();
		} catch (Exception ex) {
			log.error(
					"[fechaSessaoHibernate] - Ocorreu um erro ao fechar uma sessï¿½o do Hibernate",
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
		this.fechaSessaoHibernate();
		this.liberaInstanciaDao();
	}

	public void excecao() {
		WfDao.rollbackTransacao();
	}

	public void depois() {
		WfDao.commitTransacao();
	}

	public void antes() throws Exception {
		WfHibernateUtil.getSessao();
		ModeloDao.freeInstance();
		WfDao.getInstance();
		Wf.getInstance().getConf().limparCacheSeNecessario();

		WfContextBuilder.getConfiguration();
		WfContextBuilder.createJbpmContext();

		if (!WfDao.getInstance().sessaoEstahAberta())
			throw new AplicacaoException(
					"Erro: sessï¿½o do Hibernate estï¿½ fechada.");

		WfContextBuilder.getJbpmContext().getJbpmContext()
				.setSession(WfDao.getInstance().getSessao());

		// Velho
		// GraphSession s = WfContextBuilder.getJbpmContext()
		// .getGraphSession();
		// Field fld = GraphSession.class.getDeclaredField("session");
		// fld.setAccessible(true);
		// Session session = (Session) fld.get(s);
		// if (!session.isOpen())
		// throw new AplicacaoException(
		// "Erro: sessï¿½o do Hibernate estï¿½ fechada.");
		// HibernateUtil.setSessao(session);
		// WfDao.getInstance();

		// if (session.getTransaction() != null)
		// return;

		WfDao.iniciarTransacao();
	}

}
