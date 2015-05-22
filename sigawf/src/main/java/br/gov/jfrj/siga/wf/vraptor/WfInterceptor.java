package br.gov.jfrj.siga.wf.vraptor;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.InstantiateInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.wf.util.WfExecutionEnvironment;

@Intercepts(before = InstantiateInterceptor.class)
@RequestScoped
public class WfInterceptor implements Interceptor {

	/**
	 * Pega a sessão do JBPM e coloca-a no Hibernate.
	 */
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		WfExecutionEnvironment ee = new WfExecutionEnvironment();

		try {
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
			ee.finalmente();
			Thread.interrupted();
		}
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

}