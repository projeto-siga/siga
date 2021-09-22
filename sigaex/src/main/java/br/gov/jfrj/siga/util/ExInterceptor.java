package br.gov.jfrj.siga.util;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.guice.RequestScoped;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.jpa.JPATransactionInterceptor;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;

@RequestScoped
@Intercepts(before = JPATransactionInterceptor.class)
public class ExInterceptor {

	private final EntityManager manager;
	private final Validator validator;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final ServletContext context;

	/**
	 * @deprecated CDI eyes only
	 */
	public ExInterceptor() {
		super();
		manager = null;
		validator = null;
		request = null;
		response = null;
		context = null;
	}

	@Inject
	public ExInterceptor(EntityManager manager, Validator validator, ServletContext context, HttpServletRequest request,
			HttpServletResponse response) {
		this.manager = manager;
		this.validator = validator;
		this.request = request;
		this.response = response;
		this.context = context;
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack) {

		// EntityManager em = ExStarter.emf.createEntityManager();

		ContextoPersistencia.setEntityManager(this.manager);

		// Inicialização padronizada
		CurrentRequest.set(new RequestInfo(this.context, this.request, this.response));

		ModeloDao.freeInstance();
		ExDao.getInstance();
		try {
			Ex.getInstance().getConf().limparCacheSeNecessario();
		} catch (Exception e1) {
			throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
		}

		try {
			stack.next();
		} catch (Exception e) {
			throw e;
		} finally {
			// Renato Crivano: precisei desabilitar a linha abaixo porque parece que o vRaptor4
			// retorna do stack.next() antes de processar o JSP.
			//
			// ContextoPersistencia.setEntityManager(null);
		}
	}

	public boolean accepts(ControllerMethod method) {
		return true; // Will intercept all requests
	}
}