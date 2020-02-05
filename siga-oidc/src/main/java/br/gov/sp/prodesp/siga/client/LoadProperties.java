package br.gov.sp.prodesp.siga.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

/**
 * 
 * @author 03648469681
 * 
 * Carregador de parametros, Utilizar o Objeto parametersOIDC
 * 
 */
public class LoadProperties {

	protected static final Logger log = Logger.getLogger(LoadProperties.class.getName());
	protected Properties adaptorProperties;

	private static OIDCParameters parametersOIDC;

	static final String DEFAULT_CONFIG_FILE = "client.properties";

	static final String iss = "iss";
	static final String jwksUri = "jwks_uri";
	static final String authzUri = "authz_uri";
	static final String tokenUri = "token_uri";
	static final String userInfoUri = "userinfo_uri";
	static final String clientId = "client_id";
	static final String clientSecret = "client_secret";
	static final String redirectUri = "redirect_uri";
	static final String tokenEndpointAuthMethod = "token_endpoint_auth_method";
	static final String tokenEndpointAuthSigningAlg = "token_endpoint_auth_signing_alg";

	static final String responseType = "response_type";
	static final String scopeOpenId = "scope_openid";
	static final String scopeEmail = "scope_email";
	static final String scopeProfile = "scope_profile";
	static final String scopePhone = "scope_phone";
	static final String scopeAddress = "scope_address";
	static final String scopeOfflineAccess = "scope_offline_access";
	static final String prompt = "prompt";
	static final String codeChallengeMethod = "code_challenge_method";

	static final String maxAge = "max_age";
	static final String maxAgeTimeUnit = "max_age_time_unit";
	static final String idTokenHint = "id_token_hint";
	static final String loginHint = "login_hint";
	static final String responseMode = "response_mode";

	public LoadProperties(ServletContext servletContext) {
		if (null == adaptorProperties) {
			try {
				this.adaptorProperties = new Properties();
				adaptorProperties.load(servletContext.getResourceAsStream("/WEB-INF/properties/" + DEFAULT_CONFIG_FILE));
			} catch (FileNotFoundException e) {
				log.warning("Arquivo de propriedades: " + DEFAULT_CONFIG_FILE + " não encontrado!");
				log.warning("Propriedades do adaptor do Siga não serão carregadas.");
			} catch (IOException e) {
				log.warning("Não foi possível carregar as propriedades do arquivo " + DEFAULT_CONFIG_FILE);
				log.warning("Propriedades do adaptor do Siga não serão carregadas.");
			} 
		}

		log.info("# Propriedades de configuração inicial do cliente.");
		
		parametersOIDC = new OIDCParameters();

		loadArquivo();
	}

	public void loadArquivo() {
		parametersOIDC.setIss(adaptorProperties.getProperty(iss).trim());
		parametersOIDC.setJwksUri(adaptorProperties.getProperty(jwksUri).trim());
		parametersOIDC.setAuthzUri(adaptorProperties.getProperty(authzUri).trim());
		parametersOIDC.setTokenUri(adaptorProperties.getProperty(tokenUri).trim());
		parametersOIDC.setUserInfoUri(adaptorProperties.getProperty(userInfoUri).trim());

		parametersOIDC.setClientId(adaptorProperties.getProperty(clientId).trim());
		parametersOIDC.setClientSecret(adaptorProperties.getProperty(clientSecret).trim());
		parametersOIDC.setRedirectUri(adaptorProperties.getProperty(redirectUri).trim());
		parametersOIDC.setTokenEndpointAuthMethod(adaptorProperties.getProperty(tokenEndpointAuthMethod).trim());
		parametersOIDC.setTokenEndpointAuthSigningAlg(adaptorProperties.getProperty(tokenEndpointAuthSigningAlg).trim());

		parametersOIDC.setResponseType(adaptorProperties.getProperty(responseType).trim());
		parametersOIDC.setScopeOpenId(new Boolean(adaptorProperties.getProperty(scopeOpenId).trim()));
		parametersOIDC.setScopeEmail(new Boolean(adaptorProperties.getProperty(scopeEmail).trim()));
		parametersOIDC.setScopeProfile(new Boolean(adaptorProperties.getProperty(scopeProfile).trim()));
		parametersOIDC.setScopeProfile(new Boolean(adaptorProperties.getProperty(scopePhone).trim()));
		parametersOIDC.setScopeAddress(new Boolean(adaptorProperties.getProperty(scopeAddress).trim()));
		parametersOIDC.setScopeOfflineAccess(new Boolean(adaptorProperties.getProperty(scopeOfflineAccess).trim()));
		parametersOIDC.setPrompt(adaptorProperties.getProperty(prompt).trim());
		parametersOIDC.setCodeChallengeMethod(adaptorProperties.getProperty(codeChallengeMethod).trim());

		parametersOIDC.setMaxAge(adaptorProperties.getProperty(maxAge).trim());
		parametersOIDC.setMaxAgeTimeUnit(adaptorProperties.getProperty(maxAgeTimeUnit).trim());
		parametersOIDC.setIdTokenHint(adaptorProperties.getProperty(idTokenHint).trim());
		parametersOIDC.setLoginHint(adaptorProperties.getProperty(loginHint).trim());
		parametersOIDC.setResponseMode(adaptorProperties.getProperty(responseMode).trim());
	}

	public OIDCParameters getParametersOIDC() {
		return parametersOIDC;
	}
}