package br.gov.jfrj.siga.tp.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import br.gov.jfrj.siga.tp.validation.CnhConstraintValidator;

/**
 * Anotacao criada para excutar o {@link CnhConstraintValidator}.
 * 
 * @author db1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = CnhConstraintValidator.class)
public @interface Cnh {
	String message() default "{condutor.CnhCheck.cnhinvalida}";

	Class<Object>[] groups() default {};

	Class<Object>[] payload() default {};
}
