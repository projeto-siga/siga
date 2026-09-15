package br.gov.jfrj.siga.vraptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação criada para ser colocada nos métodos que podem ser acessados pelo usuário externo
 * 
 * @author Renato Crivano
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsuarioExterno {

}