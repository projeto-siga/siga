package br.gov.jfrj.siga.uteis;

import org.jsoup.safety.Safelist;

public class SafeListCustom extends Safelist {
	
    public static Safelist relaxedCustom() {
    	
    	return Safelist.relaxed()
    			.preserveRelativeLinks(true)
    			.addProtocols("a", "href", "#")
    			
    			//Add Tags
    			.addTags("s")
    			.addTags("section")
    			.addTags("header")
    			.addTags("hr")

    			//Add Atributos Gerais - modo permissivo
    			.addAttributes(":all", "style","class","id","name","class","align","valign","width","heigth","bgcolor","valign", "tabindex", 
    					"aria-label",  "aria-invalid", "aria-labelledby", "aria-describedby", "role", 
    					"data-delay" , "data-html" , "data-original-title" , "data-original-title", "data-toggle", "data-placement", "data-description", "data-dismiss", "data-siga-modal-abrir", "dir")

    			//Add Atributos Específicos
    			.addAttributes("a", "rel", "target")
    			.addAttributes("div", "page-break-after","word-wrap")
    			.addAttributes("sup", "data-footnote-id")
    			.addAttributes("li", "data-footnote-id")
    			.addAttributes("table", "bordercolor","border","cellpadding","cellspacing","summary")
    			.addAttributes("th", "scope")
    			.addAttributes("td", "headers","colspan","rowspan");

	}
}
