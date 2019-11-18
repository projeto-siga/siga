package br.gov.jfrj.siga.gi.integracao;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import br.gov.jfrj.siga.gi.integracao.IntegracaoLdapProperties;

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
	
	public synchronized boolean trocarSenha(String login, String novaSenha) throws Exception {
		final Client client = new Client();
		String enderecoWSLDAP = props.getEnderecoTrocaSenhaWebServiceLdap();
        final WebResource webResource = client.resource(enderecoWSLDAP);
        MultivaluedMap<String, String> dadosForm = new MultivaluedMapImpl();
        dadosForm.add("login", login);
        dadosForm.add("newPassword", novaSenha);
        final ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).accept("application/json").post(ClientResponse.class, dadosForm);
        if (response.getStatus() != 200)
        {
            throw new Exception("Erro de acesso ao web service. HTTP error code: " + response.getStatus());
        }
        final String output = response.getEntity(String.class);
        
        // temporario - estudar a possibilidade do retorno de um status com mais significado 
        // ao invés de retornar o nome de usuário fornecido - no atual cenario, nenhuma 
        // mensagem de erro retorna o login em seu texto, e o proprio WS evita que isso aconteca, 
        // porem um status de erro mais significativo seria melhor
        if(output.contains(login))
        	return true;
		
        throw new Exception("Mensagem: " + output, new Exception("Erro ao trocar senha."));
	}
	
	public synchronized boolean autenticarUsuario(String login, String senha) throws Exception {
		final Client client = new Client();
		String enderecoWSLDAP = props.getEnderecoAutenticacaoWebServiceLdap();
		final WebResource webResource = client.resource(enderecoWSLDAP);
        MultivaluedMap<String, String> dadosForm = new MultivaluedMapImpl();
        dadosForm.add("login", login);
        dadosForm.add("password", senha);
        final ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).accept("application/json").post(ClientResponse.class, dadosForm);
        if (response.getStatus() != 200)
        {
            throw new Exception("Erro de acesso ao web service. HTTP error code: " + response.getStatus());
        }
        final String output = response.getEntity(String.class);
        
        // temporario - estudar a possibilidade do retorno de um status com mais significado 
        // ao invés de retornar o nome de usuário fornecido - no atual cenario, nenhuma 
        // mensagem de erro retorna o login em seu texto, e o proprio WS evita que isso aconteca, 
        // porem um status de erro mais significativo seria melhor
        if(output.contains(login))
        	return true;
        
        throw new Exception("Mensagem: " + output, new Exception("Erro ao autenticar usuario."));
	}
}
