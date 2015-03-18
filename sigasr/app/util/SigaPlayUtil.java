package util;

import models.SrItemConfiguracao;

public class SigaPlayUtil {

	/**
	 * Retorna uma String formatada para renderizar as informações do componente 
	 * #{descricaoItem}.
	 */
	public static String descricaoItem(SrItemConfiguracao itemConfiguracao) {
		if (itemConfiguracao != null)
			return itemConfiguracao.tituloItemConfiguracao;
		else
			return new String("Item não informado");
	}
	
	/**
	 * Retorna uma String formatada para renderizar as informações do componente 
	 * #{selecionado}
	 */
	public static String selecionado(String sigla, String descricao) {
		return new String("<span title=\"" + descricao + "\">" + sigla + "</span>");
	}
	
	public static String botaoExpandir() {
		StringBuffer sb = new StringBuffer();
		sb.append("<button class=\"bt-expandir\">");
		sb.append("<span id=\"iconeBotaoExpandirTodos\">+</span>");
		sb.append("</button>");
		
		return sb.toString();
	}
}
