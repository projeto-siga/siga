package br.com.caelum.iogi;

import javax.persistence.EntityManager;

import br.com.caelum.iogi.DependenciesInjector;
import br.com.caelum.iogi.Instantiator;
import br.com.caelum.iogi.exceptions.InvalidTypeException;
import br.com.caelum.iogi.parameters.Parameter;
import br.com.caelum.iogi.parameters.Parameters;
import br.com.caelum.iogi.reflection.NewObject;
import br.com.caelum.iogi.reflection.Target;
import br.com.caelum.iogi.spi.DependencyProvider;
import br.com.caelum.iogi.spi.ParameterNamesProvider;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Objeto;

public class ObjetoObjectInstantiator implements Instantiator<Object> {

	private final Instantiator<Object> argumentInstantiator;
	private final DependenciesInjector dependenciesInjector;
	private final ParameterNamesProvider parameterNamesProvider;

	public ObjetoObjectInstantiator(
			final Instantiator<Object> argumentInstantiator,
			final DependencyProvider dependencyProvider,
			final ParameterNamesProvider parameterNamesProvider) {
		this.argumentInstantiator = argumentInstantiator;
		this.dependenciesInjector = new DependenciesInjector(dependencyProvider);
		this.parameterNamesProvider = parameterNamesProvider;
	}

	public boolean isAbleToInstantiate(final Target<?> target) {
		return true;
	}

	public Object instantiate(final Target<?> target,
			final Parameters parameters) {
		expectingAConcreteTarget(target);

		final Parameters parametersForTarget = parameters.focusedOn(target);

		Object obj = null;

		if (target.getType() instanceof Class
				&& Objeto.class
						.isAssignableFrom(((Class) target.getType()))) {
			Long id = null;

			String keyName = target.getName() + ".id";
			for (Parameter p : parameters.forTarget(target)) {
				if (keyName.equals(p.getName()) && p.getValue() != null
						&& p.getValue().length() > 0) {
					id = Long.valueOf(p.getValue());
				}
			}

			// Desabilitei a leitura a partir da id para simplificar o
			// polimorfismo
			// if (key == null) {
			// try {
			// key = Key.create(obj);
			// } catch (IllegalArgumentException e) {
			// if (e.getMessage()
			// .startsWith(
			// "You cannot create a Key for an object with a null @Id"))
			// return obj;
			// }
			// }

			try {
				if (id != null) {
					EntityManager em = ContextoPersistencia.em();
					obj = em.find(target.getClassType(), id);
					if (obj != null)
						em.detach(obj); // Detached para que não seja acidentalmente
					// salva no banco de dados
				}
			} catch (Exception e) {
				throw new RuntimeException(
						"Não foi possível ler um objeto do banco de dados.", e);
			}

			// if (obj == null) {
			// String className = target.getName() + ".class";
			// for (Parameter p : parameters.forTarget(target)) {
			// if (className.equals(p.getName())) {
			// try {
			// obj =
			// this.getClass().getClassLoader().loadClass(p.getValue()).newInstance();
			// } catch (InstantiationException | IllegalAccessException
			// | ClassNotFoundException e) {
			// throw new RuntimeException("Can't instantiate entity of class " +
			// p.getValue(), e);
			// }
			// }
			// }
			// }

			if (obj == null)
				obj = target
						.constructors(parameterNamesProvider,
								dependenciesInjector)
						.compatibleWith(parametersForTarget).largest()
						.instantiate(argumentInstantiator)
						.valueWithPropertiesSet();

			NewObject nobj = new NewObject(argumentInstantiator,
					parametersForTarget, obj);

			return nobj.valueWithPropertiesSet();
		} else {
			obj = target
					.constructors(parameterNamesProvider, dependenciesInjector)
					.compatibleWith(parametersForTarget).largest()
					.instantiate(argumentInstantiator).valueWithPropertiesSet();
			return obj;
		}

	}

	private <T> void expectingAConcreteTarget(final Target<T> target) {
		if (!target.isInstantiable())
			throw new InvalidTypeException(
					"Cannot instantiate abstract type %s",
					target.getClassType());
	}

}