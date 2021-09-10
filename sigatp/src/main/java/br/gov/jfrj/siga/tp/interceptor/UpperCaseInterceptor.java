package br.gov.jfrj.siga.tp.interceptor;


import java.lang.reflect.Field;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

/**
 * Interceptor utilizado para transformar os campos String anotados com {@link UpperCase} 
 * 
 * @author DB1
 *
 */

@RequestScoped
@Intercepts(before=BeanValidationInterceptor.class)
public class UpperCaseInterceptor {
	
	private MethodInfo methodInfo;

	/**
	 * @deprecated CDI eyes only
	 */
	public UpperCaseInterceptor() {
		super();
		methodInfo = null;
	}
	
	@Inject
	public UpperCaseInterceptor(MethodInfo methodInfo) {
		this.methodInfo = methodInfo;
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack) {
		Object[] parametros = methodInfo.getParametersValues();
		if (parametros != null) {
			for (int indiceDoParametro = 0; indiceDoParametro < parametros.length; indiceDoParametro++) {
				if (parametros[indiceDoParametro] != null) {
					Field[] campos = parametros[indiceDoParametro].getClass().getDeclaredFields();
					
					for (Field campo:campos) {
						if (campo.isAnnotationPresent(UpperCase.class)) {
							if (campo.getType().getName().equals("java.lang.String")) {
								campo.setAccessible(true);
							//	Object value = campo.get(parametros[indiceDoParametro]);
								try {
									if (campo.get(parametros[indiceDoParametro]) != null) {
										campo.set(parametros[indiceDoParametro], campo.get(parametros[indiceDoParametro]).toString().toUpperCase());
									}
									campo.setAccessible(false);	
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		stack.next(); 
	}

	private String getNomedoMetodo(String campo) {
		return "get"+ Character.toUpperCase(campo.charAt(0)) + campo.substring(1);
	}


	public boolean accepts(ControllerMethod method) {
		 return true;
	}

}

