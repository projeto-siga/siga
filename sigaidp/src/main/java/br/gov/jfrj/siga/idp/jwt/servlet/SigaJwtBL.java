package br.gov.jfrj.siga.idp.jwt.servlet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.idp.jwt.SigaJwtOptions;
import br.gov.jfrj.siga.idp.jwt.SigaJwtOptionsBuilder;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProvider;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProviderException;

import com.auth0.jwt.interfaces.DecodedJWT;


public class SigaJwtBL {

	private SigaJwtProvider provider;

	public SigaJwtBL() throws SigaJwtProviderException {
		SigaJwtOptions options = new SigaJwtOptionsBuilder()
		.setPassword("12345")
		.setTTL(60000)
		.build();
		provider = SigaJwtProvider.getInstance(options);

	}
	
	public static SigaJwtBL getInstance() throws SigaJwtProviderException {
		return new SigaJwtBL();
	}

	public String login(String matricula, String senha) {
		return login(matricula, null, senha, null, null);
	}
	
	/**
	 * 
	 * @param matricula - Matrícula do usuário que está se logando
	 * @param lotacao - Lotação do usuário. Utilizada para obter permissões de usuário em outra lotação.
	 * @param senha - Senha do usuário que está se logando
	 * @param configuracao - Configurações informadas pelo requisitante do token  
	 * @param permissoes - Expressão regular de permissões do siga-gi que serão utilizadas pelo usuário (REGEXP). Exemplo: SIGA-DOC-ASS|SIGA-WF-.*
	 * @return
	 */
	public String login(String matricula, String lotacao, String senha, String configuracao, String permissoes) {
		String token = null;
		
		GiService giService = Service.getGiService();
		String usuarioLogado = giService.login(matricula, senha);
		if(usuarioLogado != null && usuarioLogado.length() > 0){
			Map<String,String[]> mapClaims = new HashMap<String, String[]>();
			
			String acessos = giService.acessos(matricula, lotacao);
			JSONObject jsonPermissoes = new JSONObject(acessos);
			Iterator keysIterator = jsonPermissoes.keys();
			
			while(keysIterator.hasNext()){
				String item = (String)keysIterator.next();
				if(permissaoNaoCorresponde(permissoes, item) || naoTemPermissao(jsonPermissoes, item)){
					keysIterator.remove();
				}
				
			}
			
			String[] claims = new String[jsonPermissoes.keySet().size()];
			jsonPermissoes.keySet().toArray(claims);
			mapClaims.put("perm", claims);
			
			String subject = matricula + (lotacao!=null?("@" + lotacao):"");
			token = provider.criarToken(subject,configuracao,mapClaims);
		}
		
		return token;
	}

	private boolean naoTemPermissao(JSONObject jsonPermissoes, String item) {
		return !jsonPermissoes.get(item).toString().equalsIgnoreCase("pode");
	}

	private boolean permissaoNaoCorresponde(String permissoes, String item) {
		return permissoes != null && !item.toUpperCase().matches(permissoes.toUpperCase());
	}

	public String validar(String token) {
		DecodedJWT tokenDecodificado = provider.validarToken(token);
		return "{situacao:\"valido\"}"; 
	}
	
	public String assertAcesso(String token, String matricula, String lotacao, String servico) {
		DecodedJWT tokenDecodificado = provider.validarToken(token);
		GiService giService = Service.getGiService();
		return giService.acesso(matricula, lotacao, servico); 
	}
	
	public String getAcessos(String token, String matricula, String lotacao) {
		DecodedJWT tokenDecodificado = provider.validarToken(token);
		GiService giService = Service.getGiService();
		return giService.acessos(matricula, lotacao); 
	}



}
