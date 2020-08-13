package br.gov.jfrj.siga.ex.util;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.mvel2.MVEL;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;

public class MascaraUtil {

	private static String MASK_IN;
	private static String MASK_OUT;
	private static String MASK_SHOW;

//	MASCARA ATUAL
//	private static String MASK_IN = "([0-9]{0,2})\\.?([0-9]{2})?\\.?([0-9]{2})?\\.?([0-9]{2})?([A-Z])?";
//	private static String MASK_OUT = "%1$02d.%2$02d.%3$02d.%4$02d";

//	MASCARA ANTIGA
//	private static String MASK_IN = "([0-9]{0,2})\\.?([0-9]{3})?\\.?([0-9]{2})?";
//	private static String MASK_OUT = "%1$02d.%2$03d.%3$02d";


	
	private static MascaraUtil instancia;
	private MascaraUtil(){

	}
	
	public static synchronized MascaraUtil getInstance(){
		if (instancia == null){
			MASK_IN = Prop.get("classificacao.mascara.entrada");
			MASK_OUT = Prop.get("classificacao.mascara.saida");
			MASK_SHOW = Prop.get("classificacao.mascara.exibicao");
			instancia = new MascaraUtil();
		}
		return instancia;
	}
	
	/**
	 * Retorna Expressão regular com formato em que a classificação documental deve estar de acordo 
	 * @return - regex da classificacao documental
	 */
	public String getMascaraEntrada(){
		return MASK_IN;
	}
	
	/**
	 * Retorna o formato da máscara a ser produzida na saída (vide Formatter.java)
	 * @return - máscara de saída
	 */
	public String getMascaraSaida() {
		return MASK_OUT;
	} 
	
	/**
	 * Retorna o formato da máscara a ser produzida para exibição (vide Formatter.java)
	 * @return - máscara de exibição
	 */
	public String getMascaraExibicao() {
		return MASK_SHOW;
	} 
	
	public void setMascaraEntrada(String regex){
		MASK_IN = regex;
	}

	public void setMascaraSaida(String formatter){
		MASK_OUT = formatter;
	}

	public void setMascaraExibicao(String formatter){
		MASK_SHOW = formatter;
	}

	
	/**
	 * Formata um texto que esteja de acordo com a mascara de entrada 
	 * @param texto - texto a ser formatado como codificacao de classificação documental
	 * @return - codificacao formatado de acordo com mascaraSaida. <br/> Retorna null em caso de problemas com entrada ou saída.
	 */
	public String formatar(String texto){
		final String mascara = getMascaraSaida();
		return formatar(texto, mascara);
	}

	/**
	 * Formata um texto que esteja de acordo com a mascara de exibição 
	 * @param texto - texto a ser formatado como codificacao de classificação documental
	 * @return - codificacao formatado de acordo com mascaraExibicao. <br/> Retorna null em caso de problemas com entrada ou saída.
	 */
	public String formatarParaExibicao(String texto){
		String mascara = getMascaraExibicao();
		if (mascara == null || mascara.length() == 0)
			mascara = getMascaraSaida();
		return formatar(texto, mascara);
	}
	
	/**
	 * Formata um texto que esteja de acordo com a mascara informada 
	 * @param texto - texto a ser formatado como codificacao de classificação documental
	 * @param mascara - string representando a máscara a ser utilizada. Se a máscara começar com "(", então será considerada uma expressão MVEL, caso o contrário, será utilizada em uma chamada ao Formatter.
	 * @return - codificacao formatado de acordo com a máscara. <br/> Retorna null em caso de problemas com entrada ou saída.
	 */
	private String formatar(String texto, final String mascara) {
		if (getMascaraEntrada()==null || mascara== null || texto == null){
			return null;
		}
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(texto);
		if(me.find()){
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
			
			if (mascara != null && mascara.startsWith("(")) {
				try {
					 Map<String,Object> vars = new HashMap<String,Object>();
	                 vars.put("grupos", grupos);

					String eval = (String)MVEL.eval(mascara, vars);
					if (eval == null) {
						throw new AplicacaoException("Problema na expressão: "
								+ mascara);
					}
					return eval;
				} catch (Exception e) {
					throw new AplicacaoException("Problema na expressão: "
							+ mascara);
				}
			}			
			
			Formatter f = new java.util.Formatter();	
			return f.format(mascara,grupos).toString();
		}
		
		return null;
	}
	
	/**
	 * Produz a máscara para consultar m nível de classificacao documental
	 * @param nivel - Nível na hierarquia desejado. Baseado em um (1)
	 * @return - Máscara para consultar o nível (Ex: nível 2: "__.__.00.00")
	 */
	public String getMscTodosDoNivel(int nivel) {
		String txt = formatar("");
		if (getMascaraEntrada()==null || getMascaraSaida() == null || txt == null){
			return null;
		}

		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(txt);
		
		if(me.matches()){
			if (nivel<= 0 || nivel>me.groupCount()){
				nivel = getUltimoNivel(txt);
			}
			StringBuffer sb = new StringBuffer(txt);
//			nivel++;
			
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
	
	private int getUltimoNivel(String codificacao) {
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(codificacao);
		
		if(me.matches()){
			for (int i = me.groupCount(); i > 0 ; i--) {
				if (me.group(i)!=null){
					return i;
				}
			}
		}
		return 0;
	}

	/**
	 * Retorna a máscara no maior nível possível.
	 * @return - Máscara do maior nível possível Ex:__.__.__.__
	 */
	public String getMscTodosDoMaiorNivel(){
		return getMscTodosDoNivel(-1);
	}

	/**
	 * Produz a máscara correspondente para obter os filhos da classificacao.
	 * @param texto - Valor da Classificacao Documental
	 * @param nivelInicial - Nível na hierarquia desejado. Baseado em um (1)
	 * @param niveisAbaixo - boolean que indica se deve ser calculados os níveis inferiores ao nível inicial
	 * @return - Máscara para consultar os filhos (Ex1: <br/>nível 2: "11.__.00.00" <br/>Ex2: nível 2 com niveis abaixo: "11.__.__.__" )
	 */
	public String getMscFilho(String texto, int nivelInicial, boolean niveisAbaixo) {
		String txt = formatar(texto);
		if (getMascaraEntrada()==null || getMascaraSaida()== null || txt == null){
			return null;
		}
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(txt);
		
		if(me.matches()){
			StringBuffer sb = new StringBuffer(txt);
//			nivelInicial++;
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

	public String getMscFilho(String codificacao, boolean niveisAbaixo) {
		int nivelInicial = calcularNivel(formatar(codificacao));
		nivelInicial++;
		return getMscFilho(codificacao, nivelInicial, niveisAbaixo);
	}
	
	/**
	 * Retorna o campo correspondente ao nível indicado
	 * @param nivel - Nivel desejado. Baseado em 1.
	 * @param texto - texto com a classificacao documental
	 * @return
	 */
	public String getCampoDaMascara(int nivel, String texto) {
		String txt = formatar(texto);
		if (txt == null || nivel <= 0){
			return null;
		}
	
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(txt);
		
		if(me.matches()){
			if (me.groupCount() < nivel)
				return null;
			StringBuffer sb = new StringBuffer(txt);
//			nivel++;
			int inicio = me.start(nivel);
			int fim = me.end(nivel);
			
			if (inicio<0 || fim<0){
				return null;
			}
			
			return sb.substring(inicio, fim);
			
		}
	
		return null;
	}

	/**
	 * Calcula qual é o nível inicial em que se deve procurar os filhos. A lógica é pegar o primeiro grupo com zero (0)
	 * @param codificacao - codificacao da classificacao documental
	 * @return
	 */
	public int calcularNivel(String codificacao) {
		if (codificacao==null || codificacao.equals("")){
			return -1;
		}
		if (codificacao.equals(formatar("0"))){
			return 1;
		}
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(codificacao);
		int nivel = 0;
		if (me.matches()){
			for (int i = me.groupCount(); i > 0 ; i--) {
				if (me.group(i)!=null){
					Integer grupo = Integer.valueOf(me.group(i));
					if (grupo != 0){
						nivel = i;
						break;
					}
				}
			}
		}
		return nivel;
		
	}
	
	public String[] getPais(String texto){
		String codificacao = formatar(texto);
		if (codificacao == null){
			return null;
		}

		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(codificacao);
		String result[] = null;
		if (me.matches()){
			int nivelInicial = calcularNivel(codificacao);
			if (nivelInicial==1){
				return result;
			}
			result = new String[nivelInicial-1];
			StringBuffer sb = new StringBuffer(codificacao);
			for (int i = nivelInicial; i > 1; i--) {
				int inicio = me.start(i);
				int fim = me.end(i);
				
				for (int j = inicio; j < fim; j++) {
					sb.setCharAt(j, '0');
				}
				result[i-2] = sb.toString();
			}
		}
		return result;
	}

	/**
	 * Substitui um valor pela máscara correspondente
	 * @param s - String a ter o valor substituído. A string deve estar no formato da máscara definida em getMascaraentrada().<br/>
	 * 			  Ex:01.02.03.04
	 * @param mask - Máscara que será aplicada ao valor. A string ser compatível com o formato da máscara definida em getMascaraentrada().<br/> 
	 * 				ex: 05.06.__.__
	 * @return O valor da entrada alterado de acordo com a máscara. Retorna null, em caso de erros de formatação ou máscaras incompatíveis.  
	 */
	public String substituir(String valor, String masklike) {
		if (valor==null || masklike==null ||valor.length()!=masklike.length()){
			return null;
		}
		Pattern p = Pattern.compile(getMascaraEntrada());
		Matcher mValor = p.matcher(valor);
		
		String mascaraLike =  masklike.replaceAll("_", "0");
		Matcher mMaskLike = p.matcher(mascaraLike);
		
		if(!mValor.matches() || !mMaskLike.matches()){
			return null;
		}
		
		StringBuffer result = new StringBuffer();
		char[] caracteres = masklike.toCharArray();
		for (int i = 0; i < caracteres.length; i++) {
			if(caracteres[i]=='_'){
				result.append(valor.charAt(i));
			}else{
				result.append(caracteres[i]);
			}
		}
		
		return result.toString();
	}

	public boolean isCodificacao(String texto) {
		return Pattern.matches(getMascaraEntrada(), texto);
	}
	
	/**
	 * Retorna a quantidade de níveis da mascara definida. Por exemplo:
	 * "00.00.00.00" retorna 4. "11-2222", retorna 2 níveis; Foram inseridos
	 * vários caractéres "1" para tentar cobrir o caso das máscareas de tamanho
	 * variável.
	 * 
	 * @return - número de níveis da máscara;
	 */
	public int getTotalDeNiveisDaMascara(){
		if (true) return 4;
		Pattern pe = Pattern.compile(getMascaraEntrada());
		Matcher me = pe.matcher(formatar(StringUtils.repeat("1", getMascaraExibicao().length())));
		int result = 0;
		if(me.find()){
			for (int i = 1; i<=me.groupCount(); i++) {
				if (me.group(i)!=null){
					result++;
				}
			}
		}
		return result;

	}

	public boolean isUltimoNivel(String codificacao) {
		String c = formatar(codificacao);
		if (calcularNivel(c)==getTotalDeNiveisDaMascara()){
			return true;
		} 
		return false;
	}

		
}
	
	


