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
	
	public static String botaoRemoverSolicitacao(Long idSolicitacao, Long idLista) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"javascript: return block();\" href=\"/sigasr/solicitacao/retirarDeLista?idSolicitacao=");
		sb.append(idSolicitacao + "&idLista=" + idLista + "\" title=\"Remover da Lista\" name=\"idSolicitacao\" ");
		sb.append("value=\"" + idSolicitacao + "\">");
		sb.append("<img id=\"imgCancelar\" src=\"/siga/css/famfamfam/icons/delete.png\" style=\"margin-right: 3px;\"></a></td>");
		
		return sb.toString();
	}
	
	public static String botaoPriorizarSolicitacao() {
		StringBuffer sb = new StringBuffer();
		sb.append("<a class=\"once gt-btn-ativar\" onclick=\"listaService.alterarPosicao(event)\" title=\"Alterar posição\">");
		sb.append("<img src=\"/siga/css/famfamfam/icons/arrow_refresh_small_up_down.png\" style=\"margin-right: 3px;\"></img></a>");
		sb.append("<a class=\"once gt-btn-ativar\" onclick=\"listaService.alterarPrioridade(event)\" title=\"Alterar prioridade\">");
		sb.append("<img src=\"/siga/css/famfamfam/icons/arrow_switch.png\"></img></a>");
		
		return sb.toString();
	}
	
	public static String tagA(String descricao) {
		return new String("<a>" + descricao + "</a>");
	}
}
