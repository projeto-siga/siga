package br.gov.jfrj.siga.feature.entity.instantiator.vraptor;

import java.util.List;

import javax.persistence.Entity;

import br.com.caelum.iogi.Instantiator;
import br.com.caelum.iogi.parameters.Parameters;
import br.com.caelum.iogi.reflection.NewObject;
import br.com.caelum.iogi.reflection.Target;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.http.iogi.InstantiatorWithErrors;
import br.com.caelum.vraptor.http.iogi.VRaptorInstantiator;
import br.com.caelum.vraptor.validator.Message;
import br.gov.jfrj.siga.decorator.vraptor.exception.MissingEntityException;
import br.gov.jfrj.siga.decorator.vraptor.exception.MissingIdAttributeException;

/**
 * Base para as classes reponsaveis por instanciar entidades.
 * 
 * @author DB1
 *
 */
public abstract class AbstractJpaEntityInstantiator implements InstantiatorWithErrors, Instantiator<Object> {

	private Converters converters;
	private Localization localization;
	private VRaptorInstantiator delegate;

	public AbstractJpaEntityInstantiator(VRaptorInstantiator delegate, Converters converters, Localization localization) {
		this.delegate = delegate;
		this.converters = converters;
		this.localization = localization;
	}

	@Override
	public boolean isAbleToInstantiate(Target<?> target) {
		return target.getClassType().getAnnotation(Entity.class) != null;
	}

	@Override
	public Object instantiate(Target<?> target, Parameters parameters) {
		if (isAbleToInstantiate(target)) {
			try {
				JpaEntityConversionContext context = new JpaEntityConversionContext(converters, localization, target, parameters);
				return new NewObject(getAttributesIntantiator(), context.getEntityParameters(), context.load()).valueWithPropertiesSet();
			} catch (MissingIdAttributeException | MissingEntityException e) {
				return returnWhenMissing(parameters.focusedOn(target), target.getClassType());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return delegate.instantiate(target, parameters);
	}

	@Override
	public Object instantiate(Target<?> target, Parameters parameters, List<Message> errors) {
		return instantiate(target, parameters);
	}

	protected abstract Object returnWhenMissing(Parameters parameters, Class<?> type);
	
	protected abstract Instantiator<?> getAttributesIntantiator();

}