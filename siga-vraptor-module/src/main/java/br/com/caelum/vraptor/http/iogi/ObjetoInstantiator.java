package br.com.caelum.vraptor.http.iogi;

/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.ImmutableList;

import br.com.caelum.iogi.Instantiator;
import br.com.caelum.iogi.MultiInstantiator;
import br.com.caelum.iogi.ObjetoObjectInstantiator;
import br.com.caelum.iogi.collections.ArrayInstantiator;
import br.com.caelum.iogi.collections.ListInstantiator;
import br.com.caelum.iogi.conversion.FallbackConverter;
import br.com.caelum.iogi.conversion.StringConverter;
import br.com.caelum.iogi.exceptions.ConversionException;
import br.com.caelum.iogi.parameters.Parameter;
import br.com.caelum.iogi.parameters.Parameters;
import br.com.caelum.iogi.reflection.NewObject;
import br.com.caelum.iogi.reflection.Target;
import br.com.caelum.iogi.spi.DependencyProvider;
import br.com.caelum.iogi.spi.ParameterNamesProvider;
import br.com.caelum.vraptor.converter.Converter;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.http.InvalidParameterException;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.ValidationException;

@Specializes
public class ObjetoInstantiator implements InstantiatorWithErrors,
		Instantiator<Object> {
	private final MultiInstantiator multiInstantiator;
	private List<Message> errors;
	private final DependencyProvider provider;

	@Inject
	public ObjetoInstantiator(Converters converters,
			DependencyProvider provider, 
			ParameterNamesProvider parameterNameProvider,
			HttpServletRequest request) {
		this.provider = provider;
		
		ObjetoObjectInstantiator objectInstantiator = new ObjetoObjectInstantiator(
				this, provider, parameterNameProvider);
		List<Instantiator<?>> instantiatorList = ImmutableList.of(
				new RequestAttributeInstantiator(request),
				new VRaptorTypeConverter(converters),
				FallbackConverter.fallbackToNull(new StringConverter()),
				new ArrayAdapter(new ArrayInstantiator(this)),
				new NullDecorator(new ListInstantiator(this)), // NOTE:
																// NullDecorator
																// is here to
																// preserve
																// existing
																// behaviour.
																// Don't know if
																// it is the
																// ideal one,
																// though.
				new NullDecorator(new SetInstantiator(this)),
				new DependencyInstantiator(objectInstantiator),
				objectInstantiator);
		multiInstantiator = new MultiInstantiator(instantiatorList);
	}

	public boolean isAbleToInstantiate(Target<?> target) {
		return true;
	}

	public Object instantiate(Target<?> target, Parameters parameters,
			List<Message> errors) {
		this.errors = errors;
		return instantiate(target, parameters);
	}

	public Object instantiate(Target<?> target, Parameters parameters) {
		try {
			return multiInstantiator.instantiate(target, parameters);
		} catch (Exception e) {
			handleException(target, e);
			return null;
		}
	}

	private void handleException(Target<?> target, Throwable e) {
		if (e instanceof ValidationException) {
			errors.add(new SimpleMessage("validation", e.getLocalizedMessage(), target
					.getName()));
		} else if (e.getCause() == null) {
			throw new InvalidParameterException(
					"Exception when trying to instantiate " + target, e);
		} else {
			handleException(target, e.getCause());
		}
	}

	private final class DependencyInstantiator implements Instantiator<Object> {
		private final Instantiator<Object> delegate;

		public DependencyInstantiator(Instantiator<Object> delegate) {
			this.delegate = delegate;
		}

		public Object instantiate(Target<?> target, Parameters params) {
			return provider.provide(target);
		}

		public boolean isAbleToInstantiate(Target<?> target) {
			return target.getClassType().isInterface()
					&& provider.canProvide(target);
		}

	}

	private final class VRaptorTypeConverter implements Instantiator<Object> {
		private final Converters converters;

		public VRaptorTypeConverter(Converters converters) {
			this.converters = converters;
		}

		public boolean isAbleToInstantiate(Target<?> target) {
			return converters.existsFor(target.getClassType());
		}

		public Object instantiate(Target<?> target, Parameters parameters) {
			try {
				Parameter parameter = parameters.namedAfter(target);
				return converterForTarget(target).convert(parameter.getValue(),
						target.getClassType());
			} catch (ConversionException ex) {
				errors.add(new SimpleMessage("conversion", ex.getMessage(), target
						.getName()));
			} catch (IllegalStateException e) {
				return setPropertiesAfterConversions(target, parameters);
			}
			return null;
		}

		private Object setPropertiesAfterConversions(Target<?> target,
				Parameters parameters) {
			List<Parameter> params = parameters.forTarget(target);
			Parameter parameter = findParamFor(params, target);

			Object converted = converterForTarget(target).convert(
					parameter.getValue(), target.getClassType());

			return new NewObject(this, parameters.focusedOn(target), converted)
					.valueWithPropertiesSet();
		}

		private Parameter findParamFor(List<Parameter> params, Target<?> target) {
			for (Parameter parameter : params) {
				if (parameter.getName().equals(target.getName())) {
					return parameter;
				}
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		private Converter<Object> converterForTarget(Target<?> target) {
			return (Converter<Object>) converters.to(target.getClassType());
		}
	}
}
