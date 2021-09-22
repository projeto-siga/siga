package br.gov.jfrj.siga.idp.jwt;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.gi.service.GiService;

/**
 * Contém a lógica de utilização do Provider
 * 
 * @author kpf
 *
 */
public class SigaJwtBL {

	private static final String MODULO_SIGAIDP = "sigaidp";
	private SigaJwtProvider provider;

	public SigaJwtBL(String modulo) throws SigaJwtProviderException {
		provider = getProvider(modulo);
	}

	public SigaJwtProvider getProvider(String modulo) throws SigaJwtProviderException {
		int ttl = Prop.getInt("/siga.jwt.token.ttl");
		String moduloPwd = null;

		if (modulo == null) {
			modulo = MODULO_SIGAIDP;
		}

		try {
			moduloPwd = Prop.get("/siga.jwt.secret");
			if (moduloPwd == null) {
				throw new SigaJwtProviderException(
						"Senha do modulo indefinida. Defina a propriedade idp.jwt.modulo.pwd." + modulo);
			}
		} catch (Exception e) {
			throw new SigaJwtProviderException("Não foi possível obter a senha do módulo " + modulo, e);
		}

		SigaJwtOptions options = new SigaJwtOptionsBuilder().setPassword(moduloPwd).setModulo(modulo).setTTL(ttl)
				.build();
		return SigaJwtProvider.getInstance(options);
	}

	/**
	 * Retorna uma instancia do provider
	 * 
	 * @param modulo - Nome do módulo a ser utilizado. A aplicação correspondente
	 *               deve possuir a senha para conseguir validar os tokens emitidos
	 *               para o módulo
	 * @return
	 * @throws SigaJwtProviderException
	 */
	public static SigaJwtBL getInstance(String modulo) throws SigaJwtProviderException {
		return new SigaJwtBL(modulo);
	}

	public String login(String matricula, String senha) throws JSONException {
		return login(matricula, null, senha, null, null, null);
	}

	/**
	 * 
	 * @param matricula    - Matrícula do usuário que está se logando
	 * @param lotacao      - Lotação do usuário. Utilizada para obter permissões de
	 *                     usuário em outra lotação.
	 * @param senha        - Senha do usuário que está se logando
	 * @param configuracao - Configurações informadas pelo requisitante do token
	 * @param permissoes   - Expressão regular de permissões do siga-gi que serão
	 *                     utilizadas pelo usuário (REGEXP). Exemplo:
	 *                     SIGA-DOC-ASS|SIGA-WF-.*
	 * @param ttl          - Tempo de vida do token
	 * @return
	 * @throws JSONException 
	 */
	public String login(String matricula, String lotacao, String senha, String configuracao, String permissoes,
			Integer ttl) throws JSONException {
		String token = null;

		GiService giService = Service.getGiService();
		String usuarioLogado = giService.login(matricula, senha);
		if (usuarioLogado == null || usuarioLogado.trim().length() == 0) {
			throw new RuntimeException("Falha de autenticação");
		}
		if (usuarioLogado != null && usuarioLogado.length() > 0) {
			Map<String, Object> mapClaims = new HashMap<String, Object>();

			String acessos = giService.acessos(matricula, lotacao);
			JSONObject jsonPermissoes = new JSONObject(acessos);
			Iterator keysIterator = jsonPermissoes.keys();

			while (keysIterator.hasNext()) {
				String item = (String) keysIterator.next();
				if (permissaoNaoCorresponde(permissoes, item) || naoTemPermissao(jsonPermissoes, item)) {
					keysIterator.remove();
				}

			}

			String[] claims = JSONObject.getNames(jsonPermissoes);
			mapClaims.put("perm", claims);

			String subject = matricula + (lotacao != null ? ("@" + lotacao) : "");
			token = provider.criarToken(subject, configuracao, mapClaims, ttl);
		}

		return token;
	}

	public String criarToken(String subject, String config, Map<String, Object> claimsMap, Integer ttl) {
		return provider.criarToken(subject, config, claimsMap, ttl);
	}

	private boolean naoTemPermissao(JSONObject jsonPermissoes, String item) throws JSONException {
		return !jsonPermissoes.get(item).toString().equalsIgnoreCase("pode");
	}

	private boolean permissaoNaoCorresponde(String permissoes, String item) {
		return permissoes != null && !item.toUpperCase().matches(permissoes.toUpperCase());
	}

	public String validar(String token) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
			SignatureException, IOException, JWTVerifyException {
		provider.validarToken(token);
		return "{situacao:\"valido\"}";
	}

	public Map<String, Object> validarToken(String token) throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		return provider.validarToken(token);
	}

	public String assertAcesso(String token, String matricula, String lotacao, String servico)
			throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, SignatureException,
			IOException, JWTVerifyException {
		provider.validarToken(token);
		GiService giService = Service.getGiService();
		return giService.acesso(matricula, lotacao, servico);
	}

	public String getAcessos(String token, String matricula, String lotacao) throws InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException, SignatureException, IOException, JWTVerifyException {
		provider.validarToken(token);
		GiService giService = Service.getGiService();
		return giService.acessos(matricula, lotacao);
	}

	public static SigaJwtBL inicializarJwtBL(String modulo) throws IOException, ServletException {
		SigaJwtBL jwtBL = null;

		try {
			jwtBL = SigaJwtBL.getInstance(modulo);
		} catch (SigaJwtProviderException e) {
			throw new ServletException("Erro ao iniciar o provider", e);
		}

		return jwtBL;
	}

	public static String extrairModulo(HttpServletRequest request) throws IOException, ServletException, JSONException {
		String opcoes = request.getHeader("Jwt-Options");
		if (opcoes != null) {
			String modulo = new JSONObject(opcoes).optString("mod");
			if (modulo == null || modulo.length() == 0) {
				throw new ServletException(
						"O parâmetro mod deve ser informado no HEADER Jwt-Options do request. Ex: {\"mod\":\"siga-wf\"}");
			}
			return modulo;
		}
		return null;
	}

}
