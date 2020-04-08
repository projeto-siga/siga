package br.gov.jfrj.siga.vraptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.jsp.jstl.core.Config;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.base.SigaMessages;

@RequestScoped
@Intercepts
public class SigaInterceptor {

	/**
	 * @deprecated CDI eyes only
	 */
	public SigaInterceptor() {
	}

	@Inject
	public SigaInterceptor(ServletContext context) {
		String messages = SigaMessages.getLocalizationContext();
		Config.set(context, Config.FMT_LOCALIZATION_CONTEXT, messages);
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack) throws InterceptionException {
		stack.next();
	}

	public boolean accepts(ControllerMethod method) {
		return false;
	}

}
