package br.com.caelum.vraptor.validator;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.validator.beanvalidation.BeanValidatorContext;
import br.com.caelum.vraptor.view.ValidationViewsFactory;

/**
 * Customizacao do validador padrao do Vraptor. Converte as mensagens consultando no arquivo messages.properties e remove as mensagens duplicadas da lista.
 * 
 * @author db1
 *
 */

@Specializes
@RequestScoped
public class ExtendedValidator extends DefaultValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(ExtendedValidator.class);

	private final Result result;
	private final ValidationViewsFactory viewsFactory;
	private final Outjector outjector;
	private final Proxifier proxifier;
	private final ResourceBundle bundle;

	private final javax.validation.Validator bvalidator;
	private final MessageInterpolator interpolator;
	private final Locale locale;
	private final Messages messages;

	
	protected ExtendedValidator() {
		this(null, null, null, null, null, null, null, null, null);
	}
	
	@Inject
	public ExtendedValidator(Result result, ValidationViewsFactory factory, Outjector outjector, Proxifier proxifier, ResourceBundle bundle, javax.validation.Validator bvalidator, javax.validation.MessageInterpolator interpolator, Locale locale,Messages messages) {
		this.result = result;
		this.viewsFactory = factory;
		this.outjector = outjector;
		this.proxifier = proxifier;
		this.bundle = bundle;
		this.bvalidator = bvalidator;
		this.interpolator = interpolator;
		this.locale = locale;
		this.messages = messages;
	}

	@Override
	public Validator check(boolean condition, Message message) {
		return ensure(condition, message);
	}
	
	@Override
	public Validator ensure(boolean expression, Message message) {
		return addIf(!expression, message);
	}
	
	@Override
	public Validator addIf(boolean expression, Message message) {
		message.setBundle(bundle);
		if (expression) {
			messages.add(message);
		}
		return this;
	}

	@Override
	public Validator validate(Object object, Class<?>... groups) {
		return validate((String) null, object, groups);
	}

	@Override
	public Validator validate(String alias, Object object, Class<?>... groups) {
		if (object != null) {
			addAll(alias, bvalidator.validate(object, groups));
		}
		return this;
	}

	@Override
	public Validator add(Message message) {
		message.setBundle(bundle);
		messages.add(message);
		return this;
	}

	@Override
	public Validator addAll(Collection<? extends Message> messages) {
		for (Message message : messages) {
			add(message);
		}
		return this;
	}

	@Override
	public <T> Validator addAll(Set<ConstraintViolation<T>>  errors) {
		return addAll((String) null, errors);
	}

	@Override
	public <T> Validator addAll(String alias, Set<ConstraintViolation<T>> errors) {
		for (ConstraintViolation<T> v : errors) {
			String msg = interpolator.interpolate(v.getMessageTemplate(), new BeanValidatorContext(v), locale);
			String category = v.getPropertyPath().toString();
			if (!isNullOrEmpty(alias)) {
				category = alias + "." + category;
			}

			add(translate(new SimpleMessage(category, msg)));
			logger.debug("added message {}={} for contraint violation", category, msg);
		}
		return this;
	}

	@Override
	public <T extends View> T onErrorUse(Class<T> view) {
		if (!hasErrors()) {
			return new MockResult(proxifier).use(view); //ignore anything, no errors occurred
		}

		result.include("errors", getErrors());
		outjector.outjectRequestMap();
		
		logger.debug("there are errors on result: {}", getErrors());
		return viewsFactory.instanceFor(view, messages.handleErrors());
	}

	@Override
	public boolean hasErrors() {
		return !getErrors().isEmpty();
	}

	@Override
	public List<Message> getErrors() {
		Map<String, Message> messagesByDescription = new HashMap<>();
		for (Message message : messages.getErrors()) {
			messagesByDescription.put(message.getMessage(), message);
		}
		
		return new ArrayList<Message>(messagesByDescription.values());
			
		//return messages.getErrors();
	}
	
	private Message translate(Message message) {
		String key = message.getMessage();

		if (isBundleMessageKey(key)) {
			I18nMessage i18nMessage = new I18nMessage(message.getCategory(), processKey(key));
			i18nMessage.setBundle(bundle);
			return i18nMessage;
		}
		return message;
	}

	private String processKey(String key) {
		return new StringBuilder(key).substring(1, key.length() - 1).toString();
	}

	private boolean isBundleMessageKey(String key) {
		return key.startsWith("{") && key.startsWith("}");
	}
	
}
