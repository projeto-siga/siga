package com.google.enterprise.adaptor;

/**
 * 
 * Classe utilitária que possui acesso aos métodos com acesso de pacote 
 * no pacote com.google.enterprise.adaptor
 * 
 */
public class ConfigUtils {
	
	/**
	 * Altera o valor da propriedade no objeto Config
	 * @param key
	 * @param value
	 * @param config
	 */
	public static void setValue(String key, String value, Config config){
		config.setValue(key, value);
	}
	
}
