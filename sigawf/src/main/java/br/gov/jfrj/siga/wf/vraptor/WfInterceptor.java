package br.gov.jfrj.siga.wf.vraptor;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.InstantiateInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.wf.util.WfExecutionEnvironment;

@Intercepts(before = InstantiateInterceptor.class)
@RequestScoped
public class WfInterceptor implements Interceptor {

	private HttpServletRequest request;

	public WfInterceptor(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Pega a sessão do JBPM e coloca-a no Hibernate.
	 */
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		WfExecutionEnvironment ee = new WfExecutionEnvironment();

		try {
			ContextoPersistencia.setUserPrincipal(this.request
					.getUserPrincipal().getName());
			ee.antes(null);
			stack.next(method, resourceInstance);
			ee.depois();

		} catch (final Exception ex) {
			ee.excecao();
			if (ex instanceof RuntimeException)
				throw (RuntimeException) ex;
			else
				throw new InterceptionException(ex);
		} finally {
			ContextoPersistencia.removeUserPrincipal();
			ee.finalmente();
			Thread.interrupted();
		}
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

}