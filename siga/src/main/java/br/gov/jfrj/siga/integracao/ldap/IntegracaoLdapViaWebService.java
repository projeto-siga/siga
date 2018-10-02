package br.gov.jfrj.siga.integracao.ldap;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;

public class IntegracaoLdapViaWebService {
	
	private static IntegracaoLdapViaWebService instancia;
	private IntegracaoLdapProperties props;
	private IntegracaoLdapViaWebService(){
		props = new IntegracaoLdapProperties();
	}
	
	public synchronized static IntegracaoLdapViaWebService getInstancia(){
		if (instancia == null){
			instancia = new IntegracaoLdapViaWebService();
		}
		return instancia;
	}
	
	public synchronized String trocarSenha(String login, String novaSenha) {
		final Client client = new Client();
        final WebResource webResource = client.resource("http://localhost:8080/authldap/api/v1/credentials");
        final ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).accept("application/json").header("login", login).header("newPassword", novaSenha).post(ClientResponse.class);
        if (response.getStatus() != 200)
        {
            throw new RuntimeException("Failed Http Error code " + response.getStatus());
        }
        final String output = response.getEntity(String.class);
		
		return output;
	}
}
