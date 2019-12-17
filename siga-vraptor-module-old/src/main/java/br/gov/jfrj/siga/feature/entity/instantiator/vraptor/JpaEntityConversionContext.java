package br.gov.jfrj.siga.feature.entity.instantiator.vraptor;

import java.io.Serializable;

import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.SingularAttribute;

import br.com.caelum.iogi.parameters.Parameter;
import br.com.caelum.iogi.parameters.Parameters;
import br.com.caelum.iogi.parameters.ParametersHelper;
import br.com.caelum.iogi.reflection.Target;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.core.Localization;
import br.gov.jfrj.siga.decorator.vraptor.exception.MissingEntityException;
import br.gov.jfrj.siga.decorator.vraptor.exception.MissingIdAttributeException;
import br.gov.jfrj.siga.model.ContextoPersistencia;

/**
 * Contexto para conversao de uma entidade atraves de seu id.
 * 
 * @author DB1
 *
 */
public class JpaEntityConversionContext {
	private Class<?> entityClass;
	private Parameter entityIdParameter;
	private Parameters entityParameters;
	private SingularAttribute<?, ?> entityIdSingularAttribute;
	private Converters converters;
	private Localization localization;

	public JpaEntityConversionContext(Converters converters, Localization localization, Target<?> target, Parameters parameters) throws MissingIdAttributeException {
		this.converters = converters;
		this.localization = localization;
		this.entityClass = target.getClassType();
		this.entityParameters = parameters.focusedOn(target);
		this.entityIdSingularAttribute = findIdAtrribute();
		this.entityIdParameter = findEntityIdParameter(target);

		if (entityIdParameterIsMissing()) {
			throw new MissingIdAttributeException();
		}
	}

	private Parameter findEntityIdParameter(Target<?> target) {
		Parameter parameter = ParametersHelper.findIdParameter(entityParameters, entityIdSingularAttribute.getName());

		if (parameter == null) {
			parameter = ParametersHelper.findIdParameter(entityParameters, target.getName());
		}
		return parameter;
	}

	public Object load() throws MissingEntityException, MissingIdAttributeException {
		Serializable idValue = getIdValue();

		if (idValue != null) {
			Object object = ContextoPersistencia.em().find(entityClass, getIdValue());
			if (object == null)
				throw new MissingEntityException();
			return object;
		}
		throw new MissingIdAttributeException();
	}

	private boolean entityIdParameterIsMissing() {
		return entityIdParameter == null;
	}

	private SingularAttribute<?, ?> findIdAtrribute() {
		IdentifiableType<?> entity = ContextoPersistencia.em().getMetamodel().entity(entityClass);

		while (entity != null) {
			try {
				return entity.getDeclaredId(entity.getIdType().getJavaType());
			} catch (IllegalArgumentException e) {
				entity = entity.getSupertype();
			}
		}
		throw new RuntimeException("Atributo @Id nao encontrado!");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Serializable getIdValue() {
		Class<?> javaType = entityIdSingularAttribute.getType().getJavaType();
		Converter converter = converters.to(javaType);
		if (converter == null) {
			throw new RuntimeException("Nenhum conversor encontrado para o tipo " + javaType);
		}
		return (Serializable) converter.convert(entityIdParameter.getValue(), javaType, localization.getBundle());
	}

	public Parameters getEntityParameters() {
		return entityParameters;
	}
}