package br.gov.jfrj.siga.tp.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.gov.jfrj.siga.tp.validation.RenavamConstraintValidator;

/**
 * Anotacao criada para excutar o {@link RenavamConstraintValidator}.
 * 
 * @author db1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = RenavamConstraintValidator.class)
public @interface Renavam {

	String message() default "{renavamCheck.validation}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
