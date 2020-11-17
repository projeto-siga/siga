package br.gov.jfrj.siga.sr.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.gov.jfrj.siga.sr.notifiers.Correio;
import br.gov.jfrj.siga.sr.notifiers.CorreioHolder;

@RequestScoped
@Intercepts
public class HandleCorreioInterceptor  {

	private Correio correio;

	/**
	 * @deprecated CDI eyes only
	 */
	public HandleCorreioInterceptor() {
		super();
		correio = null;
	}
	
	@Inject
	public HandleCorreioInterceptor(Correio correio) {
		this.correio = correio;
	}

	@AroundCall
	public void intercept(InterceptorStack stack, ControllerMethod method, Object resourceInstance) throws InterceptionException {
		try {
			CorreioHolder.set(correio);
			stack.next(method, resourceInstance);
		} finally {
			CorreioHolder.unset();
		}
	}

	@Accepts
	public boolean accepts(ControllerMethod method) {
		return Boolean.TRUE;
	}
}
