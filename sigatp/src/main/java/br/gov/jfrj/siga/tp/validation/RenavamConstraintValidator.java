package br.gov.jfrj.siga.tp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import br.gov.jfrj.siga.tp.validation.annotation.Renavam;

public class RenavamConstraintValidator implements ConstraintValidator<Renavam, String> {
	int base = 11;

	@Override
	public void initialize(Renavam annotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value == null) {
			return true;
		}
		if (!value.matches("^[0-9]{1," + base + "}+$")) {
			return false;
		}
		return validarRenavam(value);
	}

	public Boolean validarRenavam(String renavam) {
		int[] multiplicadores = { 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
		int soma = 0;
		String digitoRetornado;
		int resto;
		String renavamSemDigito;
		String digitoInformado;

		renavam = renavam.trim().replace("-", "").replace(".", "");

		if (renavam.length() < base) {
			renavam = StringUtils.repeat("0", base - renavam.length()) + renavam;
		}

		renavamSemDigito = renavam.substring(0, renavam.length() - 1);
		digitoInformado = renavam.substring(renavam.length() - 1);

		for (int i = 0; i <= renavamSemDigito.length() - 1; i++) {
			soma += Integer.parseInt(renavamSemDigito.substring(i, i + 1)) * multiplicadores[i];
		}

		resto = soma % base;
		digitoRetornado = (base - resto >= 10) ? "0" : String.valueOf(base - resto);
		return digitoInformado.equals(digitoRetornado);
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}
}