package br.gov.jfrj.siga.sr.interceptor;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.sr.notifiers.Correio;
import br.gov.jfrj.siga.sr.notifiers.CorreioHolder;

@RequestScoped
@Intercepts
public class HandleCorreioInterceptor implements Interceptor {

	private Correio correio;

	public HandleCorreioInterceptor(Correio correio) {
		this.correio = correio;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		try {
			CorreioHolder.set(correio);
			stack.next(method, resourceInstance);
		} finally {
			CorreioHolder.unset();
		}
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return Boolean.TRUE;
	}
}
