package br.gov.jfrj.siga.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Anotacao para validacao de email utilizando {@link EmailConstraintValidator}.
 * 
 * @author db1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = EmailConstraintValidator.class)
public @interface Email {

	String message() default "Formato incorreto de e-mail.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean nullable() default true;
}
