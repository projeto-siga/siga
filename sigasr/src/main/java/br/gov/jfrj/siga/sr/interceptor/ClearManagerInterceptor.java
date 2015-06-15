package br.gov.jfrj.siga.sr.interceptor;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.util.jpa.JPATransactionInterceptor;

@Component
@Intercepts(after = JPATransactionInterceptor.class)
public class ClearManagerInterceptor implements Interceptor {

	private EntityManager em;

	public ClearManagerInterceptor(EntityManager em) {
		this.em = em;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		stack.next(method, resourceInstance);
		((Session) em.getDelegate()).clear(); // to avoid automatic flushing
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return Boolean.TRUE;
	}
}
