package br.gov.jfrj.siga.vraptor;

/**
 * Melhoria feita sobre a annotation @Load, para permitir contornar o 
 * Result.nothing() gerado quando uma entidade não é carregada pelo 
 * EntityManager.
 * 
 * @author Carlos Alberto Junior Spohr Poletto (carlosjrcabello@gmail.com)
 */
@java.lang.annotation.Target(value={java.lang.annotation.ElementType.PARAMETER})
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface LoadOptional
{
	/**
	 * Se marcado como true, o objeto é obrigatório, não encontrando ele irá
	 * redirecionar a página para uma determinada lógica. Caso ela não seja
	 * informada, será retornado Result.nothing().
	 * 
	 * @return
	 */
	boolean required() default false;
	
	/**
	 * O caminho da lógica a redirecionar a aplicação. Exemplo:
	 * &#47;cadastros&#47;usuarios&#47;listagem&#47;
	 * @return
	 */
	String redirectToWhenObjectNotFound() default "";
}