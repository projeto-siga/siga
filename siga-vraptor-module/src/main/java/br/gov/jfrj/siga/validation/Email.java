package br.gov.jfrj.siga.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy=EmailCheck.class)
public @interface Email {

	String message() default "Formato incorreto de e-mail.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean nullable() default false;
}
