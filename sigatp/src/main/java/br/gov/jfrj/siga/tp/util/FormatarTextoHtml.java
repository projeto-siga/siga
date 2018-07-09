package br.gov.jfrj.siga.tp.util;

import java.text.Normalizer;

public class FormatarTextoHtml {
	public static String retirarTagsHtml(String conteudo, String espacosHtml) {
		String retorno = conteudo.replace("<br>", "\n");
		retorno = retorno.replace("&aacute", "á");
		retorno = retorno.replace("&eacute", "é");
		retorno = retorno.replace("&oacute", "ó");
		retorno = retorno.replace("&iacute", "í");
		retorno = retorno.replace("&uacute", "ú");
		retorno = retorno.replace("&atilde", "ã");
		retorno = retorno.replace("&otilde", "õ");
		retorno = retorno.replace("&ccedil", "ç");
		retorno = retorno.replace("<html>", "");
		retorno = retorno.replace("</html>", "");
		retorno = retorno.replace("<p>", "");
		retorno = retorno.replace("</p>", "\n");
		retorno = retorno.replace(espacosHtml, "");
		retorno = retorno.replace("</a href=", "");
		retorno = retorno.replace(">", "");
		retorno = retorno.replace("<b>", "");
		retorno = retorno.replace("</b>", "");
		retorno = retorno.replace("'", "");
		retorno = retorno.replace("</a>", "");
		return retorno;
	}
	
	public static String removerAcentuacao(String texto) {
		return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
}
