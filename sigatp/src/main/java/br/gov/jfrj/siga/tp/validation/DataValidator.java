package br.gov.jfrj.siga.tp.validation;

import java.text.MessageFormat;
import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.gov.jfrj.siga.tp.validation.annotation.Data;

public class DataValidator implements ConstraintValidator<Data, Calendar> {

	private Data annotation;

	@Override
	public void initialize(Data annotation) {
		this.annotation = annotation;
	}

	@Override
	public boolean isValid(Calendar value, ConstraintValidatorContext context) {

		//		data.message("Ano de " + data.descricaoCampo() + " nao deve ser maior que o ano de " + ano + ".");
	    if(annotation.nullable() && (value == null || value.equals(""))) 
	        return true;
		
		boolean isValida = validarAnoData(value, annotation.intervalo());
		if(!isValida) {
			int ano = Calendar.getInstance().get(Calendar.YEAR) + annotation.intervalo();
			context.disableDefaultConstraintViolation();
			context
				.buildConstraintViolationWithTemplate(MessageFormat.format("Ano de {0} n&atilde;o deve ser maior que o ano de {1}.", annotation.descricaoCampo(), String.valueOf(ano)))
				.addConstraintViolation();
		}
		
		return isValida;
	}
	
	public static Boolean validarAnoData(Calendar value, int intervalo)  {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(value.getTime());
			return (calendar.get(Calendar.YEAR) - Calendar.getInstance().get(Calendar.YEAR) <= intervalo);
		} catch (Exception e) {
			return false;
		}
	}
}