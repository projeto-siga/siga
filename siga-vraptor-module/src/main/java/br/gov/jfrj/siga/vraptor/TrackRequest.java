package br.gov.jfrj.siga.vraptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anota&ccdil;&atilde;o criada para m&eactute;todos que necessitem do registro de logs.
 * Ex.: auditar alguma requisi&ccedil;&atilde;o de um determinado usu&aacute;rio (Visualizar Documento)
 *
 * @author Heverson Vasconcelos
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackRequest {

}