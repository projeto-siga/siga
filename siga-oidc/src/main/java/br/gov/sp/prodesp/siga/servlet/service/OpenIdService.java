package br.gov.sp.prodesp.siga.servlet.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * 
 * @author 03648469681
 *
 */
@WebService(targetNamespace = "http://service.servlet.siga.prodesp.sp.gov.br/")
public interface OpenIdService {
	
    /**
     * WebMethod que gera Token
     * @param matricula
     * @param senha
     * @return String token
     */
    @WebMethod
	public String loginSP() throws Exception;
        
}