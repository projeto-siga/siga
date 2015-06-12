package br.gov.jfrj.siga.feature.entity.instantiator.vraptor;

import br.com.caelum.iogi.Instantiator;
import br.com.caelum.iogi.parameters.Parameters;
import br.com.caelum.iogi.reflection.NewObject;
import br.com.caelum.iogi.reflection.Target;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.http.iogi.VRaptorInstantiator;

/**
 * Inicia uma entidade quando ela eh parametro do metodo (nao pode ser nulo).
 * 
 * @author DB1
 *
 */
public class JpaEntityParameterInstantiator extends AbstractJpaEntityInstantiator {

	private JpaEntityAttributeInstantiator jpaEntityAttributeInstantiator;

	public JpaEntityParameterInstantiator(VRaptorInstantiator delegate, Converters converters, Localization localization) {
		super(delegate, converters, localization);
		this.jpaEntityAttributeInstantiator = new JpaEntityAttributeInstantiator(delegate, converters, localization);
	}

	@Override
	public Object instantiate(Target<?> target, Parameters parameters) {
		return super.instantiate(target, parameters);
	}

	@Override
	protected Object returnWhenMissing(Parameters parameters, Class<?> type) {
		try {
			return new NewObject(jpaEntityAttributeInstantiator, parameters, type.newInstance()).valueWithPropertiesSet();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Instantiator<?> getAttributesIntantiator() {
		return jpaEntityAttributeInstantiator;
	}
}