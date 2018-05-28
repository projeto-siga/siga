package br.gov.jfrj.siga.tp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.gov.jfrj.siga.tp.validation.annotation.Cnh;

public class CnhConstraintValidator implements ConstraintValidator<Cnh, String> {

	@Override
	public void initialize(Cnh annotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value == null) {
			return false;
		}
		return validarCnh(value);
	}

	private static String zeros(int quantidade) {
		String retorno = "00000000000";
		retorno = retorno.substring(0, quantidade);
		return retorno;
	}

	private Boolean validarEspelho(String valor) {
		int tamanhoNumeroCnh = 11;
		int soma;
		Integer digito1 = 0;
		Integer digito2 = 0;
		;
		int incrDig2 = 0;
		int multiplicador;
		String numCnh = "";
		String controle = "";
		String digitoInformado = "";
		int cont = 1;

		valor = valor.trim();
		numCnh = zeros(tamanhoNumeroCnh - valor.length()) + valor;
		digitoInformado = numCnh.substring(numCnh.length() - 2);

		if (!valor.equals(zeros(11))) {
			do {
				soma = 0;
				multiplicador = cont == 1 ? 9 : 1;

				for (int j = 0; j < 9; j++) {
					soma += Integer.parseInt(numCnh.substring(j, j + 1)) * multiplicador;
					multiplicador = cont == 1 ? multiplicador - 1 : multiplicador + 1;
				}

				if (cont == 1) {
					digito1 = soma % 11;

					if (digito1 > 9) {
						if (digito1 == 10) {
							incrDig2 = -2;
						}

						digito1 = 0;
					}
				} else if (cont == 2) {
					int valorMod = soma % 11;

					if (valorMod + incrDig2 < 0) {
						digito2 = 11 + valorMod + incrDig2;
					} else {
						digito2 = valorMod + incrDig2;
					}

					if (digito2 > 9) {
						digito2 = 0;
					}
				}

				cont++;
			} while (cont <= 2);

			controle = digito1.toString() + digito2.toString();
			return controle.equals(digitoInformado);
		}

		return false;
	}

	private Boolean validarPgu(String valor) {
		int soma = 0;
		int multiplicador = 2;

		String pgu = valor.substring(0, valor.length() - 1);
		String digitoPgu = valor.substring(valor.length() - 1);

		for (int z = 0; z <= pgu.length() - 1; z++) {
			soma += Integer.parseInt(pgu.substring(z, z + 1)) * multiplicador;
			multiplicador++;
		}

		Integer digito = soma % 11;

		if (digito > 9) {
			digito = 0;
		}

		if (digitoPgu.equals(digito.toString())) {
			return true;
		}

		return false;
	}

	public Boolean validarCnh(String valor) {
		if (!validarEspelho(valor)) {
			return validarPgu(valor);
		} else {
			return true;
		}
	}

};