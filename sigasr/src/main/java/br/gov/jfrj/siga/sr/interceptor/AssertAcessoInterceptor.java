package br.gov.jfrj.siga.sr.interceptor;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@RequestScoped
@Intercepts(after = ContextInterceptor.class)
public class AssertAcessoInterceptor implements Interceptor {

	private SigaObjects sigaObj;
	private HttpServletRequest request;

	public AssertAcessoInterceptor(SigaObjects so, HttpServletRequest request) {
		this.sigaObj = so;
		this.request = request;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		try {
			sigaObj.assertAcesso(new Perfil(method).getValor());
			stack.next(method, resourceInstance);
		} catch (Exception e) {
			tratarExcecoes(e);
		}
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return Boolean.TRUE;
	}

	private void tratarExcecoes(Throwable e) {
		redirecionarParaErro(e);
	}

	private void redirecionarParaErro(Throwable e) {
		request.setAttribute("exception", criarExcecaoComMesmoStackTrace(e));
		throw new InterceptionException(e);
	}

	private Throwable criarExcecaoComMesmoStackTrace(Throwable e) {
		Exception translatedException = new Exception(e.getMessage());
		translatedException.setStackTrace(e.getStackTrace());
		return translatedException;
	}

	class Perfil {

		private String valor;

		public Perfil(ResourceMethod method) {
			if (method.containsAnnotation(AssertAcesso.class))
				this.valor = method.getMethod().getAnnotation(AssertAcesso.class).value();
		}

		public String getValor() {
			return this.valor;
		}
	}
}
