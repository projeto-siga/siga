package br.gov.jfrj.siga.sr.interceptor;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public abstract class AbstractExceptionHandler implements Interceptor {

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

	protected abstract void tratarExcecoes(Throwable e);

	protected abstract void redirecionarParaErro(Throwable e);

	@Override
	public boolean accepts(ResourceMethod method) {
		return Boolean.TRUE;
	}
}