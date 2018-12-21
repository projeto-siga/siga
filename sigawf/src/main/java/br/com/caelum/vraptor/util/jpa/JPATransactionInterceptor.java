package br.com.caelum.vraptor.util.jpa;

import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

/**
 * Disables the JPATransactionInterceptor.
 * 
 * @author Renato Crivano
 */
@Component
@Intercepts
public class JPATransactionInterceptor implements Interceptor {
	public JPATransactionInterceptor() {
	}

	public void intercept(InterceptorStack stack, ResourceMethod method, Object instance) {
		stack.next(method, instance);
	}

	public boolean accepts(ResourceMethod method) {
		return false; // Will intercept no requests
	}
}
