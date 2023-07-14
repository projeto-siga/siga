package br.gov.sp.prodesp.siga.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import br.gov.jfrj.siga.base.Prop;

/**
 * 
 * Carregador de parametros, Utilizar o Objeto parametersOIDC
 * 
 */
public class LoadProperties {

	protected static final Logger log = Logger.getLogger(LoadProperties.class.getName());
	protected Properties adaptorProperties;


	private static OIDCParameters parametersOIDC;

	/*   Formato dominio ex. https://homolog.login.sp.gov.br   */
	static final String sso_dominio = "/siga.integracao.sso.dominio";
	
	
	static String sso_context = "/siga.integracao.sso.context"; /* /sts/connect */
	
	static final String iss = "/siga.integracao.sso.iss";	
	static final String jwksUri = "/siga.integracao.sso.jwks.uri"; //"/sts/.well-known/openid-configuration/jwks";
	static final String authzUri = "/authorize";
	static final String tokenUri = "/token";
	static final String userInfoUri = "/userinfo";
	
	static final String clientId = "/siga.integracao.sso.cliente.id";
	static final String clientSecret = "/siga.integracao.sso.client.secret";
	static final String redirectUri = "/siga.integracao.sso.redirect.uri";
	
	static final String tokenEndpointAuthMethod = "token_endpoint_auth_method";
	static final String tokenEndpointAuthSigningAlg = "token_endpoint_auth_signing_alg";

	//siga/WEB-INF/properties/client.properties
	static final String DEFAULT_CONFIG_FILE = "client.properties";
	
	static final String responseType = "response_type";
	static final String scopeOpenId = "scope_openid";
	static final String scopeEmail = "scope_email";
	static final String scopeProfile = "scope_profile";
	static final String scopeGovBrConfiabilidades = "scope_govbr_confiabilidades";
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
		
		
		loadParameters();
		
	}

	/**
	 * Loader Parameters File
	 * client.properties and
	 * StandaAlone.xml
	 */
	private void loadParameters() {
		/*
		 * PARAMETERS 
		 * standalone.xml
		 */
		
		sso_context = Prop.get(sso_context) != null ? Prop.get(sso_context) : "";
		parametersOIDC.setIss(Prop.get(sso_dominio) + Prop.get(iss));
		parametersOIDC.setJwksUri(Prop.get(sso_dominio)  + Prop.get(jwksUri));		
		parametersOIDC.setAuthzUri(Prop.get(sso_dominio)  + sso_context + authzUri); 
		parametersOIDC.setTokenUri(Prop.get(sso_dominio ) + sso_context + tokenUri);
		parametersOIDC.setUserInfoUri(Prop.get(sso_dominio)  + sso_context + userInfoUri);

		parametersOIDC.setClientId(Prop.get(clientId));
		parametersOIDC.setClientSecret(Prop.get(clientSecret));
		parametersOIDC.setRedirectUri(Prop.get(redirectUri));
		
		/*
		 * PARAMETERS 
		 * client.properties
		 */
		parametersOIDC.setTokenEndpointAuthMethod(adaptorProperties.getProperty(tokenEndpointAuthMethod).trim());
		parametersOIDC.setTokenEndpointAuthSigningAlg(adaptorProperties.getProperty(tokenEndpointAuthSigningAlg).trim());

		parametersOIDC.setResponseType(adaptorProperties.getProperty(responseType).trim());
		parametersOIDC.setScopeOpenId(new Boolean(adaptorProperties.getProperty(scopeOpenId).trim()));
		parametersOIDC.setScopeEmail(new Boolean(adaptorProperties.getProperty(scopeEmail).trim()));
		parametersOIDC.setScopeGovBrConfiabilidades(new Boolean(adaptorProperties.getProperty(scopeGovBrConfiabilidades).trim()));
		parametersOIDC.setScopeProfile(new Boolean(adaptorProperties.getProperty(scopeProfile).trim()));
		parametersOIDC.setScopePhone(new Boolean(adaptorProperties.getProperty(scopePhone).trim()));
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