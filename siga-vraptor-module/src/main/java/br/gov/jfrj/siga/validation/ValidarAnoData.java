package br.gov.jfrj.siga.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Anotacao para validacao de ano com {@link ValidarAnoDataConstraintValidator}.
 * 
 * @author db1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidarAnoDataConstraintValidator.class)
public @interface ValidarAnoData {

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int intervalo() default 1;

	String descricaoCampo();

	String message() default "Data inv&aacutelida";
	
	boolean nullable() default true;
}