package br.gov.jfrj.siga.vraptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que o método do Controller não será interceptado para checagem de XSS
 * Ex.: Criação de Modelos, onde tem TAGs como <script> no parâmetro
 *
 * @author Dinarde Bezerra @dinarde
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestParamsNotCheck {

}
