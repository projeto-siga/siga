package br.gov.jfrj.siga.sr.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.uteis.SigaPlayCalendar;

public class SrViewUtil {
	
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
	
	public static String botaoRemoverSolicitacao(String sigla, Long idLista) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"javascript: return block();\" href=\"/sigasr/app/solicitacao/retirarDeLista?sigla=");
		sb.append(sigla + "&idLista=" + idLista + "\" title=\"Remover da Lista\" name=\"idSolicitacao\" ");
		sb.append("value=\"" + sigla + "\">");
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
	
	public static String toDDMMYYYY(Date dt){
		return dt != null ? new SimpleDateFormat("dd/MM/yyyy").format(dt) : "";
	}
	
	public static String toHHMM(Date dt){
		return dt != null ? new SimpleDateFormat("HH:mm").format(dt) : "";
	}
	
	public static String toYYYY(Date dt){
		return dt != null ? new SimpleDateFormat("yyyy").format(dt) : "";
	}
	
	public static String toDDMMYYYYHHMM(Date dt){
		return (dt != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dt) : "").replace(" 00:00", "");
	}
	
	public static String toDDMMYYYYHHMMSS(Date dt){
		return (dt != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dt) : "").replace(" 00:00:00", "");
	}
	
	public static String toStr(Date dt){
		if (dt == null)
			return "";
		SigaPlayCalendar cal = new SigaPlayCalendar();
        cal.setTime(dt);
        return cal.getTempoTranscorridoString(false);
	}
	
	public static Date fromDDMMYYYYHHMM(String dt){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return(df.parse(dt));
        } catch (Exception e) {
        	return null;
        }
	}
	
	public static Date fromDDMMYYYYHHMMSS(String dt){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return(df.parse(dt));
        } catch (Exception e) {
        	return null;
        }
	}
}
