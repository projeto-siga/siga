package br.gov.jfrj.siga.idp.jwt;

import java.util.List;

import br.gov.jfrj.siga.base.Prop;

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
			throw new AuthJwtException("Acesso negado.");
		}
	}

	public String getModuloPassword(String nomeModulo) {
		return Prop.get("/siga.jwt.secret");
	}

	public String getProviderIssuer() {
		return "sigaidp";
	}

	
}
