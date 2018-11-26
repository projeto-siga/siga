package br.gov.jfrj.siga.idp.jwt.servlet;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.gi.service.GiService;
import br.gov.jfrj.siga.idp.jwt.SigaJwtOptions;
import br.gov.jfrj.siga.idp.jwt.SigaJwtOptionsBuilder;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProvider;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProviderException;

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

	public SigaJwtProvider getProvider(String modulo)
			throws SigaJwtProviderException {
		int ttl = Cp.getInstance().getProp().getJWTTokenTTL(modulo);
		String moduloPwd = null;

		if (modulo == null) {
			modulo = MODULO_SIGAIDP;
		}

		try {
			moduloPwd = Cp.getInstance().getProp().getJWTModuloPwd(modulo);
			if (moduloPwd == null) {
				throw new SigaJwtProviderException(
						"Senha do modulo indefinida. Defina a propriedade idp.jwt.modulo.pwd."
								+ modulo);
			}
		} catch (Exception e) {
			throw new SigaJwtProviderException(
					"Não foi possível obter a senha do módulo " + modulo, e);
		}

		SigaJwtOptions options = new SigaJwtOptionsBuilder()
				.setPassword(moduloPwd).setModulo(modulo).setTTL(ttl).build();
		return SigaJwtProvider.getInstance(options);
	}

	/**
	 * Retorna uma instancia do provider
	 * 
	 * @param modulo
	 *            - Nome do módulo a ser utilizado. A aplicação correspondente
	 *            deve possuir a senha para conseguir validar os tokens emitidos
	 *            para o módulo
	 * @return
	 * @throws SigaJwtProviderException
	 */
	public static SigaJwtBL getInstance(String modulo)
			throws SigaJwtProviderException {
		return new SigaJwtBL(modulo);
	}

	public String login(String matricula, String senha) {
		return login(matricula, null, senha, null, null, null);
	}

	/**
	 * 
	 * @param matricula
	 *            - Matrícula do usuário que está se logando
	 * @param lotacao
	 *            - Lotação do usuário. Utilizada para obter permissões de
	 *            usuário em outra lotação.
	 * @param senha
	 *            - Senha do usuário que está se logando
	 * @param configuracao
	 *            - Configurações informadas pelo requisitante do token
	 * @param permissoes
	 *            - Expressão regular de permissões do siga-gi que serão
	 *            utilizadas pelo usuário (REGEXP). Exemplo:
	 *            SIGA-DOC-ASS|SIGA-WF-.*
	 * @param ttl
	 *            - Tempo de vida do token
	 * @return
	 */
	public String login(String matricula, String lotacao, String senha,
			String configuracao, String permissoes, Integer ttl) {
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
				if (permissaoNaoCorresponde(permissoes, item)
						|| naoTemPermissao(jsonPermissoes, item)) {
					keysIterator.remove();
				}

			}

			String[] claims = new String[jsonPermissoes.keySet().size()];
			jsonPermissoes.keySet().toArray(claims);
			mapClaims.put("perm", claims);

			String subject = matricula
					+ (lotacao != null ? ("@" + lotacao) : "");
			token = provider.criarToken(subject, configuracao, mapClaims, ttl);
		}

		return token;
	}

	public String criarToken(String subject, String config,
			Map<String, Object> claimsMap, Integer ttl) {
		return provider.criarToken(subject, config, claimsMap, ttl);
	}

	private boolean naoTemPermissao(JSONObject jsonPermissoes, String item) {
		return !jsonPermissoes.get(item).toString().equalsIgnoreCase("pode");
	}

	private boolean permissaoNaoCorresponde(String permissoes, String item) {
		return permissoes != null
				&& !item.toUpperCase().matches(permissoes.toUpperCase());
	}

	public String validar(String token) throws InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException,
			SignatureException, IOException, JWTVerifyException {
		provider.validarToken(token);
		return "{situacao:\"valido\"}";
	}

	public Map<String, Object> validarToken(String token) throws InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException,
			SignatureException, IOException, JWTVerifyException {
		return provider.validarToken(token);
	}

	public String assertAcesso(String token, String matricula, String lotacao,
			String servico) throws InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException,
			SignatureException, IOException, JWTVerifyException {
		provider.validarToken(token);
		GiService giService = Service.getGiService();
		return giService.acesso(matricula, lotacao, servico);
	}

	public String getAcessos(String token, String matricula, String lotacao)
			throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException,
			JWTVerifyException {
		provider.validarToken(token);
		GiService giService = Service.getGiService();
		return giService.acessos(matricula, lotacao);
	}

}
