package br.gov.jfrj.siga.tp.vraptor.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.MessageInterpolator;
import javax.validation.Validator;

import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.validator.DefaultBeanValidator;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Message;

/**
 * Customizacao do validador padrao do Vraptor. Converte as mensagens consultando no arquivo messages.properties e remove as mensagens duplicadas da lista.
 * 
 * @author db1
 *
 */
@RequestScoped
@Component
public class ExtendedBeanValidation extends DefaultBeanValidator {

	private Localization localization;

	public ExtendedBeanValidation(Localization localization, Validator validator, MessageInterpolator interpolator) {
		super(localization, validator, interpolator);
		this.localization = localization;
	}

	@Override
	public List<Message> validate(Object bean, Class<?>... groups) {
		return removeDuplicated(translate(super.validate(bean, groups)));
	}

	@Override
	public List<Message> validateProperties(Object bean, String... properties) {
		return removeDuplicated(translate(super.validateProperties(bean, properties)));
	}

	@Override
	public List<Message> validateProperty(Object bean, String property, Class<?>... groups) {
		return removeDuplicated(translate(super.validateProperty(bean, property, groups)));
	}

	private List<Message> removeDuplicated(List<Message> messages) {
		Map<String, Message> messagesByDescription = new HashMap<>();
		for (Message message : messages) {
			messagesByDescription.put(message.getMessage(), message);
		}
		return new ArrayList<Message>(messagesByDescription.values());
	}

	private List<Message> translate(List<Message> messages) {
		List<Message> translatedMessages = new ArrayList<Message>();

		for (Message message : messages) {
			translatedMessages.add(translate(message));
		}
		return translatedMessages;
	}

	private Message translate(Message message) {
		String key = message.getMessage();

		if (isBundleMessageKey(key)) {
			I18nMessage i18nMessage = new I18nMessage(message.getCategory(), processKey(key));
			i18nMessage.setBundle(localization.getBundle());
			return i18nMessage;
		}
		return message;
	}

	private String processKey(String key) {
		return new StringBuilder(key).substring(1, key.length() - 1).toString();
	}

	private boolean isBundleMessageKey(String key) {
		return key.startsWith("{") && key.endsWith("}");
	}
}
