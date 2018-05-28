package br.gov.jfrj.siga.tp.interceptor;

import net.sf.oval.Validator;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

/**
 * Interceptor utilizado para transformar os campos String anotados com {@link UpperCase} 
 * 
 * @author DB1
 *
 */

@RequestScoped
@Intercepts(after=MotivoLogInterceptor.class)
public class UpperCaseInterceptor implements Interceptor{
	
	private MethodInfo methodInfo;

	public UpperCaseInterceptor(MethodInfo methodInfo) {
		this.methodInfo = methodInfo;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		Validator validator = new Validator();
		Object[] parametros = methodInfo.getParameters();
		if (parametros != null) {
			for (int indiceDoParametro = 0; indiceDoParametro < parametros.length; indiceDoParametro++) {
				if (parametros[indiceDoParametro] != null) {
					validator.validate(parametros[indiceDoParametro]);
				}
			}
		}
		stack.next(method, resourceInstance); 
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		 return true;
	}

}
