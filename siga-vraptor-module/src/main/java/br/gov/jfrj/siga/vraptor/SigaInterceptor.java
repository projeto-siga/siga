package br.gov.jfrj.siga.vraptor;

import javax.servlet.ServletContext;
import javax.servlet.jsp.jstl.core.Config;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.base.SigaMessages;

@Component
@ApplicationScoped
@Intercepts
public class SigaInterceptor implements Interceptor {

	public SigaInterceptor(ServletContext context) {
		String messages = SigaMessages.getLocalizationContext();
		Config.set(context, Config.FMT_LOCALIZATION_CONTEXT, messages);
	}


	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance)
			throws InterceptionException {
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return false;
	}

}
