package br.gov.jfrj.siga.ex.util.BIE;

import java.io.UnsupportedEncodingException;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ManipuladorHTML {
	
	enum ParteDaMateria {
		NUMERO, ABERTURA, CORPO, FECHO, ASSINATURA 
	}
	
	private ExDocumento doc;
	
	private int indiceNoDocumento;
	
	public ManipuladorHTML(ExDocumento doc){
		this(doc, 1);
	}
	
	public ManipuladorHTML(ExDocumento doc, int indiceNoDocumento){
		this.doc = doc;
		this.indiceNoDocumento = indiceNoDocumento;
	}

	private String enxugaHtml(String html){
		int aPartirDe = 0;
		int posIni = html.indexOf("<p");
		int posFim = html.indexOf(">", posIni);
		
		while(posIni!=-1){
			String sub = html.substring(posIni, posFim);
			if (sub.indexOf("style=\"")!=-1)
				sub = sub.replace("style=\"", "style=\"margin-bottom:7px; margin-top:7px;");
			else 
				sub = sub + " style=\"margin-bottom:7px; margin-top:7px;\" ";
			
			String[] atributos = sub.split(";");
			
			for (String atributo : atributos) {
				if(atributo.contains("text-indent:")) {
					sub = sub.replace(atributo + ";", "");
				}
				if(atributo.contains("text-align:left")) {
					sub = sub.replace(atributo, "text-align:justify");
				}
			}
			
			html = html.substring(0, posIni) + sub + html.substring(posFim, html.length());
			aPartirDe = posFim + 1;
			posIni = html.indexOf("<p", aPartirDe);
			posFim = html.indexOf(">", posIni);
		}
		html = html.replace("&nbsp;", " ");
		return html;
		
	}
	
	public String getTrecho(ParteDaMateria parte){
		try {
			String s = doc.getConteudoBlobHtmlString();
			if (s.contains("<!-- INICIO " + parte + " -->")) {
				return Texto.extrai(s, "<!-- INICIO " + parte + " -->",
						"<!-- FIM " + parte + " -->");
			} else {
				String marcacaoDoInicio = "<!-- INICIO " + parte;

				String marcacaoDoFim = "FIM " + parte + " -->";

				return Texto.extrai(s, marcacaoDoInicio, marcacaoDoFim, indiceNoDocumento);
			}
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public String getCorpo(){
		return getTrecho(ParteDaMateria.CORPO);
	}
	
	public String getCorpoEnxugado(){
		return enxugaHtml(getCorpo());
	}

	public String getAssinatura(){
		return getTrecho(ParteDaMateria.ASSINATURA);
	}

	public String getNumero(){
		return getTrecho(ParteDaMateria.NUMERO);
	}

	public String getAbertura(){
		return getTrecho(ParteDaMateria.ABERTURA);
	}

	public String getFecho(){
		return getTrecho(ParteDaMateria.FECHO);
	}
	
	public String getFechoEnxugadoComAssinatura(){
		return enxugaHtml(getFecho()) + enxugaHtml(getAssinatura());
	}
}
