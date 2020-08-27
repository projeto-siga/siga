package br.gov.jfrj.siga.cp.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.idp.jwt.SigaJwtOptions;
import br.gov.jfrj.siga.idp.jwt.SigaJwtOptionsBuilder;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProvider;
import br.gov.jfrj.siga.idp.jwt.SigaJwtProviderException;

/**
 * Implementação JWT, Gera e valida Tokens
 * 
 * @author 03648469681
 *
 */
public class SigaJwt {

	private static final String MODULO_SIGAIDP = "sigaidp";
	private SigaJwtProvider provider;

	public SigaJwt(String modulo) throws SigaJwtProviderException {
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

	public static SigaJwt getInstance(String modulo) throws SigaJwtProviderException {
		return new SigaJwt(modulo);
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

	public String criarToken(String subject, String config, Map<String, Object> claimsMap, Integer ttl) {
		return provider.criarToken(subject, config, claimsMap, ttl);
	}

}
