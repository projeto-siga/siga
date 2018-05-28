package br.gov.jfrj.siga.tp.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.gov.jfrj.siga.tp.validation.ChassiConstraintValidator;

/**
 * Anotacao criada para excutar o {@link ChassiConstraintValidator}.
 * 
 * @author db1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ChassiConstraintValidator.class)
public @interface Chassi {

	String message() default "{chassi.validation}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
