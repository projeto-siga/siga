package br.gov.jfrj.siga.tp.validation.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.gov.jfrj.siga.tp.validation.DataValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy=DataValidator.class)
public @interface Data {
	
	String message() default "{datavalidator.validation}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	int intervalo() default 1;
	
	String descricaoCampo();
	
	boolean nullable() default true;
}