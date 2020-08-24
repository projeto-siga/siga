package br.gov.jfrj.siga.idp.jwt;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.base.Prop;

/**
 * Provedor de tokens JWT Propriedades idp.jwt.token.ttl - Tempo de vida padrão
 * do token idp.jwt.modulo.pwd.[nome-do-modulo] - senha do módulo para o qual
 * será gerado o token. Todos as aplicações que utilizarem o token precisarão
 * saber a senha para validar o JWT
 * 
 * @author kpf
 *
 */
public class SigaJwtProvider {

	static final String PROVIDER_ISSUER = "sigaidp";
	private long defaultTTLToken = 3600; // default 1 hora
	private SigaJwtOptions options;

	private SigaJwtProvider(SigaJwtOptions options)
			throws SigaJwtProviderException {
		try {
			this.options = options;
			defaultTTLToken = options.getTtlToken();
		} catch (Exception e) {
			throw new SigaJwtProviderException(
					"Problema ao definir o algoritimo", e);
		}
	}

	public static SigaJwtProvider getInstance(SigaJwtOptions options)
			throws SigaJwtProviderException {
		return new SigaJwtProvider(options);
	}

	/**
	 * Cria um token JWT
	 * 
	 * @param subject
	 *            - para quem o token se destina
	 * @param config
	 *            - Configurações informadas pelo requisitante do token. Serve
	 *            de referência caso seja necessário reproduzir um token
	 *            semelhante.
	 * @param ttl
	 *            - Tempo de vida do token
	 * @return token assinado
	 */
	public String criarToken(String subject, String config,
			Map<String, Object> claimsMap, Integer ttl) {
		final JWTSigner signer = new JWTSigner(options.getPassword());
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		setTimes(claims, ttl);

		claims.put("sub", subject);
		claims.put("iss", PROVIDER_ISSUER);
		claims.put("mod", options.getModulo());
		
		String cookieDomain = Prop.get("/siga.jwt.cookie.domain");
		if (Prop.isGovSP() && cookieDomain != null){
			claims.put("aud", cookieDomain);
		}
		

		if (claimsMap != null) {
			for (String claimName : claimsMap.keySet()) {
				claims.put(claimName, claimsMap.get(claimName));
			}
		}

		if (config != null) {
			claims.put("cfg", config);
		}

		return signer.sign(claims);
	}

	public String renovarToken(String token, Integer ttl)
			throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException,
			JWTVerifyException {
		final JWTSigner signer = new JWTSigner(options.getPassword());
		Map<String, Object> claims = validarToken(token);
		setTimes(claims, ttl);
		return signer.sign(claims);

	}

	public void setTimes(final Map<String, Object> claims, final Integer ttl) {
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + (ttl == null ? defaultTTLToken : ttl);
		claims.put("iat", iat);
		claims.put("nbf", iat);
		claims.put("exp", exp);
	}

	private long getTTL(Integer ttl) {
		if (ttl != null && ttl < defaultTTLToken) {
			return ttl;
		} else {
			return defaultTTLToken;
		}
	}

	public Map<String, Object> validarToken(String token)
			throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException,
			JWTVerifyException {
		final JWTVerifier verifier;
		String cookieDomain = Prop.get("/siga.jwt.cookie.domain");
		if (Prop.isGovSP() && cookieDomain != null){
			verifier = new JWTVerifier(options.getPassword(), cookieDomain);
		} else {
			verifier = new JWTVerifier(options.getPassword());
		}
		
		return verifier.verify(token);

	}

}
