package br.gov.jfrj.siga.wf.vraptor;

import org.apache.log4j.Logger;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.InstantiateInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.SessionScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

@Intercepts(before = InstantiateInterceptor.class)
@SessionScoped
public class WfInterceptor implements Interceptor {
	private static boolean fConfigured = false;

	private static final Object classLock = WfInterceptor.class;

	private static final Logger log = Logger.getLogger(WfInterceptor.class);

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

						registerTransactionClasses(cfg);

						HibernateUtil.configurarHibernate(cfg, "");
						fConfigured = true;
					} catch (final Throwable ex) {
						// Make sure you log the exception, as it might be
						// swallowed
						log.error(
								"[configuraHibernate] - Não foi possível configurar o hibernate.",
								ex);
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
			log.error(
					"[fechaContextoWorkflow] - Ocorreu um erro ao fechar o contexto do Workflow",
					ex);
			// ex.printStackTrace();
		}
	}

	private void fechaSessaoHibernate() {
		try {
			HibernateUtil.fechaSessaoSeEstiverAberta();
		} catch (Exception ex) {
			log.error(
					"[fechaSessaoHibernate] - Ocorreu um erro ao fechar uma sessão do Hibernate",
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

	/**
	 * Pega a sessão do JBPM e coloca-a no Hibernate.
	 */
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		try {
			antes();
			stack.next(method, resourceInstance);
			depois();

		} catch (final Exception ex) {
			excecao();
			if (ex instanceof RuntimeException)
				throw (RuntimeException) ex;
			else
				throw new InterceptionException(ex);
		} finally {
			finalmente();
			Thread.interrupted();
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
		this.configuraHibernate();

		HibernateUtil.getSessao();
		ModeloDao.freeInstance();
		WfDao.getInstance();
		Wf.getInstance().getConf().limparCacheSeNecessario();

		// Novo
		WfContextBuilder.getConfiguration();
		WfContextBuilder.createJbpmContext();

		// Novo
		if (!WfDao.getInstance().sessaoEstahAberta())
			throw new AplicacaoException(
					"Erro: sessão do Hibernate está fechada.");

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
		// "Erro: sessão do Hibernate está fechada.");
		// HibernateUtil.setSessao(session);
		// WfDao.getInstance();

		WfDao.iniciarTransacao();
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

}