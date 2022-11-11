package br.gov.jfrj.siga.vraptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que o método do Controller passará por uma checagem de XSS de modo Permissivo
 * Ex.: Criação de Documentos e outros locais onde são suportados parâmetros HTMLs
 *
 * @author Dinarde Bezerra @dinarde
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface RequestParamsPermissiveCheck {

}
