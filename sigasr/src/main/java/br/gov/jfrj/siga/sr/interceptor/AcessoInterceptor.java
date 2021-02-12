package br.gov.jfrj.siga.sr.interceptor;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@RequestScoped
@Intercepts(after = { AssertAcessoInterceptor.class })
public class AcessoInterceptor  {
    
    private Result result;
    private SigaObjects so;

	/**
	 * @deprecated CDI eyes only
	 */
	public AcessoInterceptor() {
		super();
		result = null;
		so = null;
	}
    
    @Inject
	public AcessoInterceptor(Result result, SigaObjects so) {
        this.result = result;
        this.so = so;
    }
    

    @AroundCall
    public void intercept(SimpleInterceptorStack stack) {
    	SrSigaPermissaoPerfil.setaPermissoes(so, result);
    	stack.next();
    }

    @Accepts
    public boolean accepts(ControllerMethod method) {
        return Boolean.TRUE;
    }

}
