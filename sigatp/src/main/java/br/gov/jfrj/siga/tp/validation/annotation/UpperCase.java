package br.gov.jfrj.siga.tp.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Tem por finalidade converter para maiusculas os atributos que estiverem 
 * anotado por @UpperCase
 * 
 * Utilizada em classes Model
 * @author jlo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UpperCase
{

}