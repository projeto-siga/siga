package br.gov.jfrj.siga.vraptor;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.ApplicationLogicException;
import br.com.caelum.vraptor.interceptor.ExceptionHandlerInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.base.AplicacaoException;

import com.google.common.base.Throwables;

@Intercepts(before = ExceptionHandlerInterceptor.class)
@RequestScoped
public class AplicacaoExceptionInterceptor implements Interceptor {

	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public AplicacaoExceptionInterceptor(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		try {
			stack.next(method, resourceInstance);
		} catch (ApplicationLogicException e) {
			Throwable rootCause = Throwables.getRootCause(e);
			if (rootCause instanceof AplicacaoException) {
				request.setAttribute("exceptionGeral", rootCause);
				String stackTrace = Arrays.toString(rootCause.getStackTrace())
						.replace(",", "\n");
				request.setAttribute("exceptionStackGeral", stackTrace);
				try {
					response.sendError(400, rootCause.getMessage());
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
			} else
			    throw e;

		}
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

}