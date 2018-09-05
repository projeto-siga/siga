package br.gov.jfrj.siga.tp.interceptor;

import java.lang.annotation.Annotation;

import javax.validation.Valid;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;

/**
 * Interceptor que realiza a validacao dos atributos anotados com @Valid (Bean validation). 
 * Verifica nos parametros do metodo a ser invocado no controller se existe algo com a anotacao mencionada. Se
 * sim, executa a validacao nesse parametro.
 * 
 * @author db1
 *
 */
@RequestScoped
@Intercepts(after = { MotivoLogInterceptor.class }, before = ExecuteMethodInterceptor.class)
public class BeanValidationInterceptor implements Interceptor {

	private MethodInfo info;
	private Validator validator;

	public BeanValidationInterceptor(Validator validator, MethodInfo info) {
		this.info = info;
		this.validator = validator;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		Object[] parametros = info.getParameters();

		for (int indiceDoParametro = 0; indiceDoParametro < parametros.length; indiceDoParametro++) {
			if (parametroTemAnotacaoParaValidacao(method, indiceDoParametro)) {
				validator.validate(parametros[indiceDoParametro]);
			}
		}
		stack.next(method, resourceInstance);
	}

	private Boolean parametroTemAnotacaoParaValidacao(ResourceMethod method, int indiceDoParametro) {
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
	@Override
	public boolean accepts(ResourceMethod method) {
		return method.getMethod().getParameterTypes().length > 0;
	}
}
