package br.gov.jfrj.siga.sr.interceptor;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.gov.jfrj.siga.vraptor.SigaObjects;


public abstract class AbstractExceptionHandler {

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

//	@Accepts
//	public boolean accepts(ControllerMethod method) {
//		return Boolean.TRUE;
//	}
}