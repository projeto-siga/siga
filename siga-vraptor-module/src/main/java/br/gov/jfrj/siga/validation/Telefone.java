package br.gov.jfrj.siga.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy=TelefoneCheck.class)
public @interface Telefone {

	String message() default "Numero de telefone fixo invalido.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
