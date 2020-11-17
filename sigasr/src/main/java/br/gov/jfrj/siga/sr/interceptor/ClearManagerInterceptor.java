package br.gov.jfrj.siga.sr.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;

import org.hibernate.Session;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.jpa.JPATransactionInterceptor;


@RequestScoped
@Intercepts(after = JPATransactionInterceptor.class)
public class ClearManagerInterceptor  {

	private EntityManager em;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public ClearManagerInterceptor() {
		super();
		em = null;
	}

	public ClearManagerInterceptor(EntityManager em) {
		this.em = em;
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack)  {
		stack.next();
		em.clear();
		((Session) em.unwrap(org.hibernate.Session.class)).clear(); // to avoid automatic flushing
	}

	@Accepts
	public boolean accepts(ControllerMethod method) {
		return Boolean.TRUE;
	}
}
