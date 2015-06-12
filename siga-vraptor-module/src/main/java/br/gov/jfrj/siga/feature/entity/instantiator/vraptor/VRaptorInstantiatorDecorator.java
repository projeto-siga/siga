package br.gov.jfrj.siga.feature.entity.instantiator.vraptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.iogi.parameters.Parameters;
import br.com.caelum.iogi.reflection.Target;
import br.com.caelum.iogi.spi.DependencyProvider;
import br.com.caelum.iogi.spi.ParameterNamesProvider;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.http.iogi.VRaptorInstantiator;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.validator.Message;

/**
 * Decorator que adiciona a funcionalidade de instanciar entidades JPA ao {@link VRaptorInstantiator}.
 * 
 * @author DB1
 * 
 */
@Component
@RequestScoped
public class VRaptorInstantiatorDecorator extends VRaptorInstantiator {
	/**
	 * Objeto responsavel por instanciar entidades JPA.
	 */
	private JpaEntityParameterInstantiator jpaEntityParameterInstantiator;

	public VRaptorInstantiatorDecorator(Converters converters, DependencyProvider provider, Localization localization, ParameterNamesProvider parameterNameProvider, HttpServletRequest request) {
		super(converters, provider, localization, parameterNameProvider, request);
		VRaptorInstantiator vRaptorInstantiator = new VRaptorInstantiator(converters, provider, localization, parameterNameProvider, request);
		this.jpaEntityParameterInstantiator = new JpaEntityParameterInstantiator(vRaptorInstantiator, converters, localization);
	}

	@Override
	public Object instantiate(Target<?> target, Parameters parameters) {
		if (jpaEntityParameterInstantiator.isAbleToInstantiate(target)) {
			return jpaEntityParameterInstantiator.instantiate(target, parameters);
		}
		return super.instantiate(target, parameters);
	}

	@Override
	public Object instantiate(Target<?> target, Parameters parameters, List<Message> errors) {
		if (jpaEntityParameterInstantiator.isAbleToInstantiate(target)) {
			return jpaEntityParameterInstantiator.instantiate(target, parameters, errors);
		}
		return super.instantiate(target, parameters, errors);
	}
}