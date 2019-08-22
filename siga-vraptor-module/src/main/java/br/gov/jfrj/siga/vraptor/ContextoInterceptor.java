package br.gov.jfrj.siga.vraptor;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.FlushMode;
import org.hibernate.Session;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.proxy.CDIProxies;
import br.gov.jfrj.siga.model.ContextoPersistencia;

@Intercepts(after = AplicacaoExceptionInterceptor.class)
@RequestScoped
public class ContextoInterceptor implements Interceptor {

	public ContextoInterceptor(HttpServletRequest request) {
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method, Object resourceInstance)
			throws InterceptionException {
		try {
			stack.next(method, resourceInstance);
		} finally {
			ContextoPersistencia.setDt(null);
		}
	}

	@Override
	public boolean accepts(ControllerMethod method) {
		return true;
	}
}