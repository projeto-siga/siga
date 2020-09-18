package br.gov.jfrj.siga.tp.interceptor;

import java.lang.annotation.Annotation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.validator.Validator;


/**
 * Interceptor que realiza a validacao dos atributos anotados com @Valid (Bean validation). 
 * Verifica nos parametros do metodo a ser invocado no controller se existe algo com a anotacao mencionada. Se
 * sim, executa a validacao nesse parametro.
 * 
 * @author db1
 *
 */
@RequestScoped
@Intercepts(after = { MotivoLogInterceptor.class })
public class BeanValidationInterceptor  {

	private MethodInfo info;
	private Validator validator;

	/**
	 * @deprecated CDI eyes only
	 */
	public BeanValidationInterceptor() {
		super();
		info = null;
		validator = null;
	}
	
	@Inject
	public BeanValidationInterceptor( Validator validator, MethodInfo info) {
		this.info = info;
		this.validator = validator;
	}


	@AroundCall
	public void intercept(SimpleInterceptorStack stack, ControllerMethod method)  {
		Object[] parametros = info.getParametersValues();

		for (int indiceDoParametro = 0; indiceDoParametro < parametros.length; indiceDoParametro++) {
			if (parametroTemAnotacaoParaValidacao(method, indiceDoParametro)) {
				validator.validate(parametros[indiceDoParametro]);
			}
		}
		stack.next();
	}

	private Boolean parametroTemAnotacaoParaValidacao(ControllerMethod method, int indiceDoParametro) {
		Annotation[][] anotacoesDoParametro = method.getMethod().getParameterAnnotations();

		for (Annotation anotacao : anotacoesDoParametro[indiceDoParametro]) {
			if (anotacao instanceof Valid) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Executa apenas se o metodo possui parametros.
	 */
	@Accepts
	public boolean accepts(ControllerMethod method) {
		return method.getMethod().getParameterTypes().length > 0;
	}
}
