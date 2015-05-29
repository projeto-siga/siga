package br.gov.jfrj.siga.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TelefoneConstraintValidator implements ConstraintValidator<Telefone, String> {

	@Override
	public void initialize(Telefone annotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		return validarFixo(value);
	}

	private boolean validarFixo(String string) {
		return true;
	}
}
