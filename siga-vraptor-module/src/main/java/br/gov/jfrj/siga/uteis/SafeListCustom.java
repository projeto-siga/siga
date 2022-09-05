package br.gov.jfrj.siga.uteis;

import org.jsoup.safety.Safelist;

public class SafeListCustom extends Safelist {
	
    public static Safelist relaxedCustom() {
    	
    	return Safelist.relaxed()
    			.preserveRelativeLinks(true)
    			
    			//Add Tags
    			.addTags("s")
    			.addTags("section")
    			.addTags("header")
    			.addTags("hr")

    			//Add Atributos Gerais - modo permissivo
    			.addAttributes(":all", "style","class","id","name","class","align","valign","width","heigth","bgcolor","valign")

    			//Add Atributos Específicos
    			.addAttributes("a", "rel")
    			.addAttributes("div", "page-break-after","word-wrap")
    			.addAttributes("sup", "data-footnote-id")
    			.addAttributes("li", "data-footnote-id")
    			.addAttributes("table", "bordercolor","border","cellpadding","cellspacing","summary")
    			.addAttributes("th", "scope")
    			.addAttributes("td", "headers","colspan","rowspan");

	}
}
