package br.gov.jfrj.siga.validation;

import java.text.MessageFormat;
import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidarAnoDataConstraintValidator implements ConstraintValidator<ValidarAnoData, Calendar> {

	private ValidarAnoData annotation;

	@Override
	public void initialize(ValidarAnoData annotation) {
		this.annotation = annotation;
	}

	@Override
	public boolean isValid(Calendar value, ConstraintValidatorContext constraintValidatorContext) {
		if (!validarAnoData(value)) {
			int ano = Calendar.getInstance().get(Calendar.YEAR) + annotation.intervalo();
			constraintValidatorContext
				.buildConstraintViolationWithTemplate(MessageFormat.format("Ano de {0} nao deve ser maior que o ano de {1}.", annotation.descricaoCampo(), ano))
				.addConstraintViolation();
			return false;
		}
		return true;
	}

	public Boolean validarAnoData(Calendar value) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(value.getTime());
			return (calendar.get(Calendar.YEAR) - Calendar.getInstance().get(Calendar.YEAR) <= annotation.intervalo());
		} catch (Exception e) {
			return false;
		}
	}
}