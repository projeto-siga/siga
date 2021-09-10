package br.gov.jfrj.siga.sr.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.sr.notifiers.Correio;
import br.gov.jfrj.siga.sr.notifiers.CorreioHolder;

@RequestScoped
@Intercepts
public class HandleCorreioInterceptor {

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
	public void  intercept(SimpleInterceptorStack stack) {
		try {
			CorreioHolder.set(correio);
			stack.next();
		} finally {
			CorreioHolder.unset();
		}
	}


    @Accepts
	public boolean accepts(ControllerMethod method) {
		return Boolean.TRUE;
	}
}
