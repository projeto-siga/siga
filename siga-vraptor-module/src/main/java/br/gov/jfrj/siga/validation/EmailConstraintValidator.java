package br.gov.jfrj.siga.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailConstraintValidator implements ConstraintValidator<Email, String> {

	private Email annotation;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public void initialize(Email annotation) {
		this.annotation = annotation;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		return validarEmail(value, annotation.nullable());
	}

	public boolean validarEmail(String valor, Boolean aceitaNulos) {
		if (aceitaNulos && (valor == null || valor.equals(""))) {
			return true;
		}
		Pattern p = Pattern.compile(EMAIL_PATTERN);
		Matcher m = p.matcher(valor);
		boolean matchFound = m.matches();


		if (matchFound) {
			return true;
		}
		return false;
	}
}