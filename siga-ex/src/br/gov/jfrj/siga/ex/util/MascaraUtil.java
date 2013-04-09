package br.gov.jfrj.siga.ex.util;

import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MascaraUtil {
	
	public static final String CLASSIFICACAO_COD_ASSUNTO = "COD_ASSUNTO";
	public static final String CLASSIFICACAO_COD_CLASSE = "COD_CLASSE";
	public static final String CLASSIFICACAO_COD_SUBCLASSE = "COD_SUBCLASSE";
	
	private static String MASK_IN = "([0-9]{0,2})\\.?([0-9]{2})?\\.?([0-9]{2})?\\.?([0-9]{2})?([A-Z])?";
	private static String MASK_OUT = "%1$02d.%2$02d.%3$02d.%4$02d";

	
	private static MascaraUtil instancia;
	private MascaraUtil(){

	}
	
	public static synchronized MascaraUtil getInstance(){
		if (instancia == null){
			instancia = new MascaraUtil();
		}
		return instancia;
	}
	
	private String getMascaraEntrada(){
		return MASK_IN;
	}
	
	private String getMascaraSaida() {
		return MASK_OUT;
	} 
	
	public void setMascaraEntrada(String regex){
		MASK_IN = regex;
	}

	public void setMascaraSaida(String formatter){
		MASK_OUT = formatter;
	}

	
	/**
	 * Formata um texto que esteja de acordo com a mascara de entrada 
	 * @param mascaraEntrada - Expressão regular (regexp) com formato em que a classificacao documental deve estar de acordo
	 * @param mascaraSaida - formato a ser utilizado na saída (vide Formatter.java)
	 * @param texto - texto a ser formatado
	 * @return - texto formatado de acordo com mascaraSaida. <br/> Retorna null em caso de problemas com entrada ou saída.
	 */
	public String formatar(String texto){
		if (getMascaraEntrada()==null || getMascaraSaida()== null || texto == null){
			return null;
		}
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(texto);
		if(me.matches()){
			Object[] grupos = new Object[me.groupCount()] ;
			for (int i = 0; i < me.groupCount(); i++) {
				if(me.group(i+1)==null || me.group(i+1).length()==0){
					grupos[i] = 0;
				}else{
					try{
						grupos[i] = Integer.valueOf(me.group(i+1));
					}catch(NumberFormatException e){
						grupos[i]= me.group(i+1);
					}
				}
			}
			
			
			Formatter f = new java.util.Formatter();	
			return f.format(getMascaraSaida(),grupos).toString();
		}
		
		return null;
	}
	
	/**
	 * Produz a máscara para consultar m nível de classificacao documental
	 * @param mascaraEntrada - Expressão regular (regexp) com formato em que a classificação documental deve estar de acordo
	 * @param mascaraSaida - Formato da máscara a ser produzida na saída (vide Formatter.java)
	 * @param nivel - Nível na hierarquia desejado. Baseado em zero (0)
	 * @return - Máscara para consultar o nível (Ex: nível 1: "__.__.00.00")
	 */
	public String getMscTodosDoNivel(int nivel) {
		if (getMascaraEntrada()==null || getMascaraSaida() == null){
			return null;
		}
		String txt = formatar("");
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(txt);
		
		if(me.matches()){
			StringBuffer sb = new StringBuffer(txt);
			nivel++;
			
			for (int i = nivel; i > 0; i--) {
				int inicio = me.start(i);
				int fim = me.end(i);
				
				for (int j = inicio; j < fim; j++) {
					sb.setCharAt(j, '_');
				}
				
			}
			
			return sb.toString();
			
		}
		return null;

	}

	/**
	 * Produz a máscara correspondente para obter os filhos da classificacao.
	 * @param mascaraEntrada - Expressão regular (regexp) com formato em que a classificação documental deve estar de acordo 
	 * @param mascaraSaida - Formato da máscara a ser produzida na saída (vide Formatter.java)
	 * @param texto - Valor da Classificacao Documental
	 * @param nivel - Nível na hierarquia desejado. Baseado em zero (0)
	 * @return - Máscara para consultar os filhos (Ex: nível 1: "11.__.00.00")
	 */
	public String getMscFilho(String texto, int nivelInicial, boolean niveisAbaixo) {
		if (getMascaraEntrada()==null || getMascaraSaida()== null || texto == null){
			return null;
		}
	
		String txt = formatar(texto);
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(txt);
		
		if(me.matches()){
			StringBuffer sb = new StringBuffer(txt);
			nivelInicial++;
			int nivelFinal = niveisAbaixo?me.groupCount():nivelInicial;
			
			for (int i = nivelInicial; i <= nivelFinal; i++) {
				int inicio = me.start(i);
				int fim = me.end(i);
				
				for (int j = inicio; j < fim; j++) {
					sb.setCharAt(j, '_');
				}
			}
			
	
			return sb.toString();
			
		}
		return null;
	}

	public String getMscFilho(String texto, boolean niveisAbaixo) {
		int nivelInicial = deduzirNivelInicial(formatar(texto));
		return getMscFilho(texto, nivelInicial, niveisAbaixo);
	}
	
	/**
	 * Retorna o campo correspondente ao nível indicado
	 * @param nivel - Nivel desejado. Baseado em 0.
	 * @param texto - codificacao da classificacao documental
	 * @return
	 */
	public String getCampoDaMascara(int nivel, String texto) {
		String txt = formatar(texto);
	
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(texto);
		
		if(me.matches()){
			StringBuffer sb = new StringBuffer(txt);
			nivel++;
			int inicio = me.start(nivel);
			int fim = me.end(nivel);
			
			return sb.substring(inicio, fim);
			
		}
	
		return null;
	}

	private int deduzirNivelInicial(String texto) {
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(texto);
		if (me.matches()){
			for (int i = 1; i <= me.groupCount(); i++) {
				try{
					Integer grupo = Integer.valueOf(me.group(i));
					if(grupo == 0){
						return i-1;
					}
				}catch (NumberFormatException e) {

				}
			}
		}
		return me.groupCount();
		
	}

		
}
	
	


