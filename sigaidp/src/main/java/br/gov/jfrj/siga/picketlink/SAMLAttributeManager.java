package br.gov.jfrj.siga.picketlink;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.jacc.PolicyContext;
import javax.servlet.http.HttpServletRequest;

import org.picketlink.identity.federation.core.interfaces.AttributeManager;

/**
 * @author Rodrigo Ramalho
 * 	       hodrigohamalho@gmail.com
 * 
 * Esse handler é chamado assim que um usuário se autentica. É uma maneira de passar
 * parâmetros para os SPs. Nesse caso será passado o sessionID do IDP para manipulação
 * de requests entre os SPs.
 */
public class SAMLAttributeManager implements AttributeManager {
	
	private final String ATTRIBUTE = "IDPsessionID";
	private final String REQUEST = "javax.servlet.http.HttpServletRequest";

	/**
	 * Tudo que for colocado neste mapa que é retornado será colocado na sessão e pode ser
	 * recuperado através do método session.getAttribute("SESSION_ATTRIBUTE_MAP")
	 */
	@Override
	public Map<String, Object> getAttributes(Principal userPrincipal, List<String> attributeKeys) {
    	Map<String, Object> sessionAttrs = new HashMap<String, Object>();
    	
	    try{
	    	HttpServletRequest request = (HttpServletRequest) PolicyContext.getContext(REQUEST);
	    	sessionAttrs.put(ATTRIBUTE, request.getSession().getId());
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		
		return sessionAttrs;
	}

}
