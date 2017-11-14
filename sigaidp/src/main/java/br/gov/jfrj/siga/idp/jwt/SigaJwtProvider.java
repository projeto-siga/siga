package br.gov.jfrj.siga.idp.jwt;

import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;

public class SigaJwtProvider {

	static final String PROVIDER_ISSUER = "sigaidp";
	static long TTL_TOKEN = 3600 * 1000; // 1 hora
	private SigaJwtOptions options;
	
	private Algorithm algorithm;
	
	private SigaJwtProvider(SigaJwtOptions options) throws SigaJwtProviderException{
		try {
			algorithm = Algorithm.HMAC256(options.getPassword());
			TTL_TOKEN = options.getTtlToken();
		} catch (Exception e) {
			throw new SigaJwtProviderException("Problema ao definir o algoritimo",e);
		}
	}
	
	public static SigaJwtProvider getInstance(SigaJwtOptions options) throws SigaJwtProviderException{
		return new SigaJwtProvider(options);
	}
	
	/**
	 * Cria um token JWT
	 * @param subject - para quem o token se destina
	 * @param config - Configurações informadas pelo requisitante do token. Serve de referência caso seja necessário reproduzir um token semelhante.
	 * @return token assinado
	 */
	public String criarToken(String subject,String config, Map<String,String[]> claimsMap){
		Date agora = new Date();
		Date expiraEm = new Date(agora.getTime() + TTL_TOKEN);
		Builder jwtBuilder = JWT.create();
		
		jwtBuilder.withIssuer(PROVIDER_ISSUER)
				.withSubject(subject)
				.withIssuedAt(agora)
				.withNotBefore(agora)
				.withExpiresAt(expiraEm);
		
		for (String claimName: claimsMap.keySet()) {
			String[] claims = claimsMap.get(claimName);
			if(claims!= null){
				jwtBuilder.withArrayClaim(claimName, claims);
			}
		}
		
		if(config != null){
			jwtBuilder.withClaim("cfg", config);
		}
		
		return jwtBuilder.sign(algorithm);
				
	}
	
	public DecodedJWT validarToken(String token){
		JWTVerifier verificador = JWT.require(this.algorithm)
					.withIssuer(PROVIDER_ISSUER)
				.build();
		DecodedJWT jwt = verificador.verify(token);
		return jwt;
		
	}

}
