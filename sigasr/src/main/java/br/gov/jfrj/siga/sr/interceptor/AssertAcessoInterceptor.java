package br.gov.jfrj.siga.sr.interceptor;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@RequestScoped
@Intercepts(after = ContextInterceptor.class)
public class AssertAcessoInterceptor extends AbstractExceptionHandler {

	public AssertAcessoInterceptor(SigaObjects so, HttpServletRequest request) {
		setSo(so);
		setRequest(request);
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		if (method.containsAnnotation(AssertAcesso.class))
			try {
				getSo().assertAcesso(new Perfil(method).getValor());
			} catch (Exception e) {
				tratarExcecoes(e);
			}

		stack.next(method, resourceInstance);
	}

	@Override
	protected void tratarExcecoes(Throwable e) {
		redirecionarParaErro(e);
	}

	@Override
	protected void redirecionarParaErro(Throwable e) {
		getRequest().setAttribute("exception", criarExcecaoComMesmoStackTrace(e));
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
			this.valor = method.getMethod().getAnnotation(AssertAcesso.class).value();
		}

		public String getValor() {
			return this.valor;
		}
	}
}
