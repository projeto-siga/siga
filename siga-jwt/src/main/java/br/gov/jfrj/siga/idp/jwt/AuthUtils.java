package br.gov.jfrj.siga.idp.jwt;

import java.util.List;

public class AuthUtils {

	private List<String> listaPermissoes;
	private static AuthUtils instance;
	
	private AuthUtils(){
		
	}
	
	public static AuthUtils getInstance(){
		if(instance == null){
			instance = new AuthUtils();
		}
		return instance;
	}
	
	public void setPermissoes(List<String> permissoes) {
		listaPermissoes = permissoes;
	}
	
	public void assertAcesso(String permissao) {
		if(listaPermissoes == null || !listaPermissoes.contains(permissao)){
			throw new RuntimeException("Acesso negado.");
		}
	}

	public String getModuloPassword(String nomeModulo) {
		return System.getProperty("idp.jwt.client." + nomeModulo + ".pwd");
	}

	public String getProviderIssuer() {
		return "sigaidp";
	}

	
}
