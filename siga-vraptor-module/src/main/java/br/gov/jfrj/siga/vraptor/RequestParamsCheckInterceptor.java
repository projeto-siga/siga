package br.gov.jfrj.siga.vraptor;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.dp.DpPessoa;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
    
    private final Level BLAME = Level.forName("BLAME", 450);
    private final Logger logger = LogManager.getLogger(RequestParamsCheckInterceptor.class);
    
    @SuppressWarnings("unchecked")
	@AroundCall
    public void intercept(SimpleInterceptorStack stack) {
    	
    	parameters = request.getParameterMap();
		parameters.forEach((key, value) -> {
			if (!RequestParamsCheck.checkParameter(value[0],method.containsAnnotation(RequestParamsPermissiveCheck.class))) {
				log(value[0]);
				throw new AplicacaoException("Conteúdo inválido. Por favor, revise os valores fornecidos.");
			}
		});
		
		stack.next();
    }
    
    @Accepts
    public boolean accepts(ControllerMethod method) {
        return !method.containsAnnotation(RequestParamsNotCheck.class);
    }
    
    private void log(String param) {
    	String siglaCadastrante;
    	DpPessoa cadastrante;
    	
    	try {
    		SigaObjects so = new SigaObjects(this.request);
    		cadastrante = so.getCadastrante();
    		request.setAttribute("cadastrante", cadastrante);
    		siglaCadastrante = cadastrante.getLotacao().getSigla() + "/" + cadastrante.getSigla();
		} catch (Exception e) {
			siglaCadastrante = "Não identificado";
		}
    	
    	logger.log(BLAME, "[Detectado XSS] - Request: {}; Param XSS: {}; IP de Origem: {}; Usuário: {};", 
    			request, 
    			param, 
    			HttpRequestUtils.getIpAudit(request), 
    			siglaCadastrante);
    }
    
}