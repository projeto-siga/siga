package br.gov.sp.prodesp.siga.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 
 * @author 03648469681
 * 
 *         Carregador de parametros, Utilizar o Objeto parametersOIDC
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

	public static void main(String arg[]) {
		new LoadProperties();
		log.info(parametersOIDC.toString());
	}

	protected LoadProperties() {
		if (null == adaptorProperties) {
			InputStream propStream = null;
			try {

				propStream = new FileInputStream(new File(DEFAULT_CONFIG_FILE));
				this.adaptorProperties = new Properties();
				this.adaptorProperties.load(propStream);
			} catch (FileNotFoundException e) {
				log.warning("Arquivo de propriedades: " + DEFAULT_CONFIG_FILE + " não encontrado!");
				log.warning("Propriedades do adaptor do Siga não serão carregadas.");
			} catch (IOException e) {
				log.warning("Não foi possível carregar as propriedades do arquivo " + DEFAULT_CONFIG_FILE);
				log.warning("Propriedades do adaptor do Siga não serão carregadas.");
			} finally {
				if (propStream != null) {
					try {
						propStream.close();
					} catch (IOException e) {
						log.severe("Erro ao finalizar Stream!");
					}
				}
			}
		}

		log.info("Initial client configuration properties:");
		parametersOIDC = new OIDCParameters();

		mockParametes();
		//loadArquivo();

	}

	public void mockParametes() {
		parametersOIDC.setIss("https://homolog.login.sp.gov.br/sts");
		parametersOIDC.setJwksUri("https://homolog.login.sp.gov.br/sts/.well-known/openid-configuration/jwks");
		parametersOIDC.setAuthzUri("https://homolog.login.sp.gov.br/sts/connect/authorize");
		parametersOIDC.setTokenUri("https://homolog.login.sp.gov.br/sts/connect/token");
		parametersOIDC.setUserInfoUri("https://homolog.login.sp.gov.br/sts/connect/userinfo");

		parametersOIDC.setClientId("sempapel.documentos");
		parametersOIDC.setClientSecret("352f2ef3-db68-43ae-beb0-ee675980e367");
		parametersOIDC.setRedirectUri("http://10.2.100.202:8080/oidc-client/callBack");
		parametersOIDC.setTokenEndpointAuthMethod("client_secret_basic");
		parametersOIDC.setTokenEndpointAuthSigningAlg("");

		parametersOIDC.setResponseType("code");
		parametersOIDC.setScopeOpenId(true);
		parametersOIDC.setScopeEmail(true);
		parametersOIDC.setScopeProfile(false);
		parametersOIDC.setScopeProfile(false);
		parametersOIDC.setScopeAddress(false);
		parametersOIDC.setScopeOfflineAccess(false);
		parametersOIDC.setPrompt("default");
		parametersOIDC.setCodeChallengeMethod("null");

		parametersOIDC.setMaxAge("-1");
		parametersOIDC.setMaxAgeTimeUnit("s");
		parametersOIDC.setIdTokenHint("null");
		parametersOIDC.setLoginHint("null");
		parametersOIDC.setResponseMode("null");
	}

	public void loadArquivo() {
		parametersOIDC.setIss(adaptorProperties.getProperty(iss));
		parametersOIDC.setJwksUri(adaptorProperties.getProperty(jwksUri));
		parametersOIDC.setAuthzUri(adaptorProperties.getProperty(authzUri));
		parametersOIDC.setTokenUri(adaptorProperties.getProperty(tokenUri));
		parametersOIDC.setUserInfoUri(adaptorProperties.getProperty(userInfoUri));

		parametersOIDC.setClientId(adaptorProperties.getProperty(clientId));
		parametersOIDC.setClientSecret(adaptorProperties.getProperty(clientSecret));
		parametersOIDC.setRedirectUri(adaptorProperties.getProperty(redirectUri));
		parametersOIDC.setTokenEndpointAuthMethod(adaptorProperties.getProperty(tokenEndpointAuthMethod));
		parametersOIDC.setTokenEndpointAuthSigningAlg(adaptorProperties.getProperty(tokenEndpointAuthSigningAlg));

		parametersOIDC.setResponseType(adaptorProperties.getProperty(responseType));
		parametersOIDC.setScopeOpenId(new Boolean(adaptorProperties.getProperty(scopeOpenId)));
		parametersOIDC.setScopeEmail(new Boolean(adaptorProperties.getProperty(scopeEmail)));
		parametersOIDC.setScopeProfile(new Boolean(adaptorProperties.getProperty(scopeProfile)));
		parametersOIDC.setScopeProfile(new Boolean(adaptorProperties.getProperty(scopePhone)));
		parametersOIDC.setScopeAddress(new Boolean(adaptorProperties.getProperty(scopeAddress)));
		parametersOIDC.setScopeOfflineAccess(new Boolean(adaptorProperties.getProperty(scopeOfflineAccess)));
		parametersOIDC.setPrompt(adaptorProperties.getProperty(prompt));
		parametersOIDC.setCodeChallengeMethod(adaptorProperties.getProperty(codeChallengeMethod));

		parametersOIDC.setMaxAge(adaptorProperties.getProperty(maxAge));
		parametersOIDC.setMaxAgeTimeUnit(adaptorProperties.getProperty(maxAgeTimeUnit));
		parametersOIDC.setIdTokenHint(adaptorProperties.getProperty(idTokenHint));
		parametersOIDC.setLoginHint(adaptorProperties.getProperty(loginHint));
		parametersOIDC.setResponseMode(adaptorProperties.getProperty(responseMode));
	}

	public OIDCParameters getParametersOIDC() {
		return parametersOIDC;
	}

}
