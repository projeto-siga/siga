package br.gov.jfrj.siga.vraptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação criada para métodos que necessitem do registro de logs.
 * Ex.: auditar requisições de usuários ao visualizar documentos
 *
 * @author Heverson Vasconcelos
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackRequest {

}