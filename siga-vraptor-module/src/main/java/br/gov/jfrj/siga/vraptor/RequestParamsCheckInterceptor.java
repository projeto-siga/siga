package br.gov.jfrj.siga.vraptor;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.HttpRequestUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jsoup.Jsoup;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import br.gov.jfrj.siga.uteis.SafeListCustom;


/**
 * Interceptador para Verificação dos Parâmetros submetidos ao controler, 
 * Com o objetivo de barrar eventuais ataques XSS e semelhantes
 * 
 * Após verificar que Request contém o caracter de abertura de TAG "<", será checado
 * conforme os níveis definidos no Controller.
 * 
 * Os níveis de checagem são:
 * 
 * <ul>
 * 	 <li><b>Sem Checagem</b>: Adicionar ao método do Controller a anotação RequestParamsNotCheck.
 * 			Para Requests que podem conter scripts como a criação de modelos</li>
 *	 <li><b>Checagem Restritiva</b>: <i>(Default)</i></li>
 *   <li><b>Checagem Permissiva</b>: Adicionar ao método do Controller a anotação RequestParamsPermissiveCheck. 
 * 			Para Request que devem conter body HTML</li>
 * </ul>
 * 
 *
 * @author Dinarde Bezerra @dinarde
 */

@Intercepts
@RequestScoped
public class RequestParamsCheckInterceptor {
    @Inject
    private HttpServletRequest request;
    @Inject
	private ControllerMethod method;
    
    private Map<String, String[]> parameters;
    private final Logger logger = LogManager.getLogger(RequestParamsCheckInterceptor.class);
    private boolean isRequestValid = true;
    

    @SuppressWarnings("unchecked")
	@AroundCall
    public void intercept(SimpleInterceptorStack stack) {
    	
    	parameters = request.getParameterMap();

		parameters.forEach((key, value) -> {
			if (value[0] != null && !"".equals(value[0]) && value[0].contains("<") ) {	
				if (method.containsAnnotation(RequestParamsPermissiveCheck.class)) 
					isRequestValid = Jsoup.isValid(value[0],SafeListCustom.relaxedCustom());
			    else 
			    	isRequestValid = Jsoup.isValid(value[0],SafeListCustom.simpleText());				
			}
			
			if (!isRequestValid) {
				logger.warn("Tentativa de Ataque XSS {}. IP de Origem: {}", value[0], HttpRequestUtils.getIpAudit(request));
				throw new AplicacaoException("Conteúdo inválido. Por favor, revise os valores fornecidos.");
			}
		});
		
		stack.next();
    }
    
    
    @Accepts
    public boolean accepts(ControllerMethod method) {
        return !method.containsAnnotation(RequestParamsNotCheck.class);
    }

}