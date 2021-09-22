package br.gov.jfrj.siga.vraptor;

import javax.enterprise.inject.Specializes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.FlushMode;
import org.hibernate.Session;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.jpa.event.AfterCommit;
import br.com.caelum.vraptor.jpa.event.AfterRollback;
import br.com.caelum.vraptor.jpa.event.BeforeCommit;
import br.com.caelum.vraptor.proxy.CDIProxies;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.model.ContextoPersistencia;

/**
 * An interceptor that manages Entity Manager Transaction. All requests are
 * intercepted and a transaction is created before execution. If the request has
 * no erros, the transaction will commited, or a rollback occurs otherwise.
 * 
 * @author Lucas Cavalcanti
 */
@Specializes
@Intercepts
public class SigaTransacionalInterceptor extends br.com.caelum.vraptor.jpa.JPATransactionInterceptor {

	private final BeanManager beanManager;
	private final EntityManager manager;
	private final Validator validator;
	private final MutableResponse response;
	private HttpServletRequest request;
	private ControllerMethod method;

	private final static ThreadLocal<SigaTransacionalInterceptor> current = new ThreadLocal<SigaTransacionalInterceptor>();

	/**
	 * @deprecated CDI eyes only.
	 */
	protected SigaTransacionalInterceptor() {
		this(null, null, null, null, null);
	}

	@Inject
	public SigaTransacionalInterceptor(BeanManager beanManager, EntityManager manager, Validator validator,
			MutableResponse response, HttpServletRequest request) {
		this.beanManager = beanManager;
		this.manager = manager;
		this.validator = validator;
		this.response = response;
		this.request = request;
		current.set(this);
	}

	@Accepts
	public boolean accepts(ControllerMethod method) {
		  this.method = method;
		  return true;
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack) {
		addRedirectListener();

		try {
	 		if (this.method.containsAnnotation(Transacional.class)) {
				EntityTransaction transaction = manager.getTransaction();
				transaction.begin();
			} 
			
//			 System.out.println("*** " + (manager.getTransaction() ==
//					 null || !manager.getTransaction().isActive() ? "NÃO" : "") + " TRANSACIONAL" + this.method.toString() + " - ");

			stack.next();

			commit(manager.getTransaction());
		} finally {
			ContextoPersistencia.removeAll();
			EntityTransaction transaction = manager.getTransaction();
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
				beanManager.fireEvent(new AfterRollback());
			}
		}
	}

	private void commit(EntityTransaction trn) {
		if (trn == null)
			return;

		if (trn.isActive())
			beanManager.fireEvent(new BeforeCommit());

		if (!validator.hasErrors() && trn.isActive()) {
			trn.commit();
			beanManager.fireEvent(new AfterCommit());
		}
	}

	/**
	 * We force the commit before the redirect, this way we can abort the redirect
	 * if a database error occurs.
	 */
	private void addRedirectListener() {
		response.addRedirectListener(new MutableResponse.RedirectListener() {
			@Override
			public void beforeRedirect() {
				commit(manager.getTransaction());
			}
		});
	}

	public static void upgradeParaTransacional() {
		SigaTransacionalInterceptor thiz = current.get();
		EntityTransaction transaction = thiz.manager.getTransaction();
		if (!transaction.isActive()) {
			// System.out.println("*** UPGRADE para Transacional - " + thiz.method.toString());
			transaction.begin();
		}
	}

	public static void downgradeParaNaoTransacional() {
		SigaTransacionalInterceptor thiz = current.get();
		EntityTransaction transaction = thiz.manager.getTransaction();
		if (thiz != null && transaction != null) {
			thiz.commit(transaction);
			// System.out.println("*** DOWNGRADE para NÃO Transacional - " +
			// thiz.method.toString());
		}
	}
}