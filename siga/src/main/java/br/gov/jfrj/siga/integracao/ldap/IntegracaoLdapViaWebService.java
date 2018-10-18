package br.gov.jfrj.siga.integracao.ldap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;

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
	
	public synchronized boolean trocarSenha(String login, String novaSenha) {
		final Client client = new Client();
		String enderecoWSLDAP = props.getEnderecoWebServiceLdap();
        final WebResource webResource = client.resource(enderecoWSLDAP);
        MultivaluedMap<String, String> dadosForm = new MultivaluedMapImpl();
        dadosForm.add("login", login);
        dadosForm.add("newPassword", novaSenha);
        final ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).accept("application/json").post(ClientResponse.class, dadosForm);
        if (response.getStatus() != 200)
        {
            throw new RuntimeException("Failed Http Error code " + response.getStatus());
        }
        final String output = response.getEntity(String.class);
        
        // temporario
        if(output.contains(login))
        	return true;
		
		return false;
	}
}
