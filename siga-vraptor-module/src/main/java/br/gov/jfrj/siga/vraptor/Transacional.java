package br.gov.jfrj.siga.vraptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação criada para ser colocada antes de funcionalidades que fazem
 * operações de DML(INSERT, UPDATE OU DELETE) tendo como objetivo abrir
 * transações
 * 
 * @author João Luis
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Transacional {

}