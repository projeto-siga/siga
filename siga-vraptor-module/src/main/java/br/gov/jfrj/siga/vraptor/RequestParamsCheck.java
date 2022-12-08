package br.gov.jfrj.siga.vraptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

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
					isRequestValid = Jsoup.isValid(requestParameterStr,SafeListCustom.relaxedCustom());
			    else 
			    	isRequestValid = Jsoup.isValid(requestParameterStr,SafeListCustom.simpleText());				
			}
		}
    	
		return isRequestValid;
    }
    
    public static boolean hasHTMLTags(String text){
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

}
