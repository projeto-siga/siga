package br.gov.jfrj.siga.vraptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

import br.gov.jfrj.siga.uteis.SafeListCustom;

public class RequestParamsCheck {
	
    
    private static final String HTML_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
    private static Pattern pattern = Pattern.compile(HTML_PATTERN);
	
    public static boolean checkParameter(final Object requestParameter, final boolean permissiveCheck) {
    	boolean isRequestValid = true;
    	
    	if (requestParameter != null) {
    		String requestParameterStr = requestParameter instanceof String ? (String) requestParameter : requestParameter.toString();
	    	
			if (!"".equals(requestParameter) && hasHTMLTags(requestParameterStr)) {	
				if (permissiveCheck) 
					isRequestValid = isValid(requestParameterStr,SafeListCustom.relaxedCustom());
			    else 
			    	isRequestValid = isValid(requestParameterStr,SafeListCustom.simpleText());				
			}
		}
    	
		return isRequestValid;
    }
    
    public static boolean hasHTMLTags(String text){
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
    
    public static boolean isValid(String paramDirty, Safelist safelist) {
    	Document dirty = Jsoup.parseBodyFragment(paramDirty, ""); //pré-formata parâmetro HTML para desconsiderar HTMLs com problemas semânticos
    	String bodyHtml = dirty.body().html() //extrai body para checagem 
    						.replaceAll("<!--", ""); //Retira tag comentário

    	return Jsoup.isValid(bodyHtml, safelist);
    }
    

}
