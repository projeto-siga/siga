package br.gov.jfrj.siga.sr.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@RequestScoped
@Intercepts
public class AssertAcessoInterceptor extends AbstractExceptionHandler {

	private ControllerMethod method;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public AssertAcessoInterceptor() {
		super();
	}
	
	@Inject
	public AssertAcessoInterceptor(SigaObjects so, HttpServletRequest request) {
		setSo(so);
		setRequest(request);
	}

	
	private SigaObjects so;
	private HttpServletRequest request;

	public SigaObjects getSo() {
		return so;
	}

	public void setSo(SigaObjects so) {
		this.so = so;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	@Accepts
	public boolean accepts(ControllerMethod method) {
		if(method.containsAnnotation(AssertAcesso.class)) {
			this.method = method;
			return true;
		}
		return false;
	}


	protected void tratarExcecoes(Throwable e) {
		redirecionarParaErro(e);
	}


	protected void redirecionarParaErro(Throwable e) {
		getRequest().setAttribute("exception", criarExcecaoComMesmoStackTrace(e));
		throw new InterceptionException(e);
	}

	private Throwable criarExcecaoComMesmoStackTrace(Throwable e) {
		Exception translatedException = new Exception(e.getMessage());
		translatedException.setStackTrace(e.getStackTrace());
		return translatedException;
	}
	
	@AroundCall
	public void intercept(SimpleInterceptorStack stack) throws InterceptionException {
		try {
			getSo().assertAcesso(new Perfil(method).getValor());
		}
		catch(Exception e) {
			tratarExcecoes(e);
		}
		stack.next();
	}

	
	class Perfil {

		private String valor;

		public Perfil(ControllerMethod method) {
			this.valor = method.getMethod().getAnnotation(AssertAcesso.class).value();
		}

		public String getValor() {
			return this.valor;
		}
	}

}
