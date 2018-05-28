package br.gov.jfrj.siga.tp.util;

import br.gov.jfrj.siga.tp.model.Parametro;
import java.util.logging.Logger;

public abstract class VerySimpleLogger {
	private static final String VSLOGGER = "VSLogger";
	private static final String SIGATP_VSLOGGER = "sigatp.VSLogger";
	private static boolean gerarLogParametro = false;
 	
	private static void logar(String saida) {
		Logger.getLogger(SIGATP_VSLOGGER).info(VSLOGGER + saida);
	}
 	
	private static void escreverLinhaDeLog(String controle, int fator) {
		escreverLinhaDeLog(controle, fator, null);
	}
 	
	private static void escreverLinhaDeLog(String controle, int fator, String dado) {
		if(!VerySimpleLogger.gerarLogParametro) {
			return;
		}
		
		StringBuilder log = new StringBuilder();
		log.append(controle != null ? " " + controle + " - " : " - ");
		log.append(fator);
		log.append(dado != null ? " - \"" + dado + "\"" : "");
		logar(log.toString());
	}
 	
	/**
	 * Escreve uma linha de log muito simples
	 * @param nomeControle String de controle a imprimir na linha de log
	 * @param numeroControle Inteiro a imprimir na linha de log
	 */
	public static void escrever(String nomeControle, int numeroControle) {
		escreverLinhaDeLog(nomeControle, numeroControle);
	}
 	
	/**
	 * Escreve uma linha de log muito simples incluindo um dado fornecido
	 * @param nomeControle String de controle a imprimir na linha de log
	 * @param numeroControle Inteiro a imprimir na linha de log
	 * @param dadoExtra String extra a imprimir ao final da linha de log
	 */
	public static void escrever(String nomeControle, int numeroControle, String dadoExtra) {
		escreverLinhaDeLog(nomeControle, numeroControle, dadoExtra);
	}
 	
	/**
	 * Resetar o estado se deve ou nao escrever o log. Extraido de um parametro atraves do metodo Parametro.buscarConfigSistemaEmVigor
	 * @param nomeParametroBooleanSeDeveGerarLog nome do parametro a ser buscado
	 */
	public static void lerParametroSeDeveGerarLog(String nomeParametroBooleanSeDeveGerarLog) {
		VerySimpleLogger.gerarLogParametro = Boolean.parseBoolean(Parametro.buscarConfigSistemaEmVigor(nomeParametroBooleanSeDeveGerarLog));
	}
}