package br.gov.sp.prodesp.siga.client;


import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.apache.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nimbusds.jose.Header;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.AESDecrypter;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.assertions.jwt.JWTAssertionDetails;
import com.nimbusds.oauth2.sdk.assertions.jwt.JWTAssertionFactory;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.ClientSecretJWT;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.Audience;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.id.Subject;
import com.nimbusds.oauth2.sdk.jose.SecretKeyDerivation;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.UserInfoSuccessResponse;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import com.nimbusds.openid.connect.sdk.validators.IDTokenValidator;
import com.thetransactioncompany.json.pretty.PrettyJson;

import net.minidev.json.JSONObject;


/**
 * OpenID authentication response callback (redirect_uri target).
 */
public class Callback  {
	
	
		@WebServlet(urlPatterns = "/callBack", name = "CallbackServlet", asyncSupported = true)
		public static class CallbackServlet extends HTTPRequestParametersInterceptorServlet {
		
			public CallbackServlet() {
				System.out.println("Construtor !!!!!");
			}
						
	}
	
	
	 /**
	 * The logger.
	 *
	 */
	private static Logger log = LogManager.getLogger("CALLBACK");
	
	
	
	private void createAuthzSuccessPanel(final AuthenticationSuccessResponse successResponse) {
		
		if (successResponse.getAuthorizationCode() != null) {
		}
		
		if (successResponse.getIDToken() != null) {
			//wrapLongString(successResponse.getIDToken().getParsedString()
		}
		
			//wrapLongString(successResponse.getAccessToken().getValue()
		
		if (successResponse.getState() != null) {
			successResponse.getState().getValue();
		}
		
		if (successResponse.getSessionState() != null) {
			successResponse.getSessionState().getValue();
		}
	}
	
	
	 /*
	 * Creates a panel to display an ID token with its header, claims set
	 * and signature.
	 *
	 * @param idToken   The ID token. Must be JWS or plain. Not
	 *                  {@code null}.
	 * @param jweHeader The JWE header is additional encryption has been
	 *                  applied.
	 *
	 *
	 * @return The panel.
	 *
	 *
	 */
	private void createIDTokenPanel(final JWT idToken, final JWEHeader jweHeader) {

		
		PrettyJson jsonFormatter = new PrettyJson(PrettyJson.Style.COMPACT);
		
		String formattedJWEHeader = null;
		String formattedJWSHeader = null;
		String formattedClaims = null;
		String formattedSignature = null;
		
		try {
			formattedJWEHeader = jweHeader != null ? jsonFormatter.parseAndFormat(jweHeader.toJSONObject().toJSONString()) : null;
			formattedJWSHeader = jsonFormatter.parseAndFormat(idToken.getHeader().toJSONObject().toJSONString());
			formattedClaims = jsonFormatter.parseAndFormat(idToken.getJWTClaimsSet().toJSONObject().toJSONString());			
			// formattedSignature = wrapLongString(((SignedJWT) idToken).getSignature().toString());
			
		} catch (Exception e) {
			// unlikely
			throw new RuntimeException("Failed to format ID token: " + e.getMessage());
		}
	}
	
	
	 /**
	 * Creates a panel to display the OpenID provider's public signing
	 * key in JSON Web Key (JWK) format.
	 *
	 * @param jwk The public JWK. Must not be {@code null}.
	 *
	 * @return The panel.
	 **/
	private void createSigningJWKPanel(final JWK jwk) {
		
		//Label typeLabel = new Label(jwk.getKeyType().getValue());
		if (jwk instanceof RSAKey) {
			
		} else if (jwk instanceof ECKey) {
			
			//wrapLongString(((ECKey) jwk).getCurve().toString())
			
		}
		
		if (jwk.getKeyID() != null) {
		}
		
		if (jwk.getKeyUse() != null) {
		}
		
	}
	
	
	/*
	 * Creates a panel to display a successful token response.
	 *
	 * @param tokenResponse The token response. Must not be {@code null}.
	 *
	 * @return The panel.
	 */
	private void createTokenResponsePanel(final OIDCTokenResponse tokenResponse) {
		
		JWT idToken = tokenResponse.getOIDCTokens().getIDToken();
		
		if (idToken != null && idToken.getParsedString() != null) {
			
			//idTokenValue.setContentMode(ContentMode.PREFORMATTED);
			//wrapLongString(idToken.getParsedString()
		}
		
		AccessToken accessToken = tokenResponse.getOIDCTokens().getAccessToken();
		
		if (accessToken != null) {
			accessToken.getType().getValue();
			
			BearerAccessToken bat = (BearerAccessToken)accessToken;
			///wrapLongString(bat.getValue()
			
			if (bat.getScope() != null) {
				// wrapLongString(bat.getScope().toString();
				
			}
		}
		
		RefreshToken refreshToken = tokenResponse.getOIDCTokens().getRefreshToken();
		
		if (refreshToken != null) {
		
		}
		
	}
	
	
	
	/**
	 * Creates a panel to display the obtained UserInfo.
	 *
	 * @param jweHeader Optional JWE header for a UserInfo JWT,
	 *                  {@code null} it not applicable.
	 * @param jwsHeader Optional JWS / plain header for a UserInfo JWT,
	 *                  {@code null} it not applicable.
	 * @param userInfo  The user information as a JSON object. Must not be
	 *                  {@code null}.
	 *
	 * @return The panel.
	 */
	private void createUserInfoPanel(final JWEHeader jweHeader, final Header jwsHeader, final JSONObject userInfo) throws java.text.ParseException {
		
		PrettyJson jsonFormatter = new PrettyJson(PrettyJson.Style.COMPACT);
		
		if (jweHeader != null) {
			jsonFormatter.parseAndFormat(jweHeader.toJSONObject().toJSONString());
		}
		
		if (jwsHeader != null) {
			jwsHeader.toJSONObject().toJSONString();
		}
		
		jsonFormatter.parseAndFormat(userInfo.toJSONString());
	}
	
	
	 /**
	 * Decrypts the specified JWT with a shared key derived from a client
	 * secret.
	 *
	 * @param encryptedJWT The JWT to decrypt. Must not be {@code null}.
	 * @param clientSecret The client secret.
	 *
	 * @throws JOSEException If decryption failed.
	 *
	 */
	private static void decrypt(final EncryptedJWT encryptedJWT, final Secret clientSecret)
		throws JOSEException {
		
		if (clientSecret == null) {
			throw new JOSEException("Missing client_secret");
		}
		
		if (AESDecrypter.SUPPORTED_ALGORITHMS.contains(encryptedJWT.getHeader().getAlgorithm())) {
			
			encryptedJWT.decrypt(new AESDecrypter(SecretKeyDerivation.deriveSecretKey(
				clientSecret,
				encryptedJWT.getHeader().getAlgorithm(),
				encryptedJWT.getHeader().getEncryptionMethod())));
			
		} else if (DirectDecrypter.SUPPORTED_ALGORITHMS.contains(encryptedJWT.getHeader().getAlgorithm())) {
			
			encryptedJWT.decrypt(new DirectDecrypter(SecretKeyDerivation.deriveSecretKey(
				clientSecret,
				encryptedJWT.getHeader().getAlgorithm(),
				encryptedJWT.getHeader().getEncryptionMethod())));
		} else {
			throw new JOSEException("Unsupported JWE encryption algorithm: " + encryptedJWT.getHeader().getAlgorithm());
		}
	}
	
	public void init(HttpRequest req) {
		
		//HashMap<k, V> params = (Map<String, List<String>>)req.getAttribute("http-request-parameters");
		HashMap<String, List<String>> params = new HashMap<>();
		
		AuthenticationResponse oidcResponse;
		
		try {
			oidcResponse = AuthenticationResponseParser.parse(new URI("https:///"), params);
		} catch (Exception e) {
			// (createErrorMessagePanel("Invalid OpenID authentication response: " + e.getMessage()));
			return;
		}
		
		if (oidcResponse.indicatesSuccess()) {
			// Display success parameters
			//createAuthzSuccessPanel((AuthenticationSuccessResponse)oidcResponse));
		} else {
			// Display error parameters
			//createAuthzErrorPanel((AuthenticationErrorResponse) oidcResponse));
		}
		
		
		// Get the client map of pending OpenID requests
		//Map<State,PendingAuthenticationRequest> pendingRequests = (Map<State,PendingAuthenticationRequest>)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("pending-requests");
		
		Map<State, PendingAuthenticationRequest> pendingRequests = new Hashtable<>();
		
		if (pendingRequests == null) {
			// Hostname of callback doesn't match the URL from which the client was launched
			//createErrorMessagePanel("Unexpected callback: The OpenID client was started from a hostname / IP address that differs from the hostname / IP address of the redirect_uri");
			return;
		}
		
		if (oidcResponse.getState() == null) {
			// State is recommended in OAuth 2.0, but mandatory in OpenID Connect
			//createErrorMessagePanel("Invalid OpenID authentication response: Missing state parameter"));
			return;
		}
		

		PendingAuthenticationRequest pendingRequest = pendingRequests.get(oidcResponse.getState());
		if (pendingRequest == null) {
			//createErrorMessagePanel("Unexpected OpenID authentication response: " + "No pending OpenID authentication request with state " + oidcResponse.getState()));
			return;
		}
		
		// If error stop here
		if (! oidcResponse.indicatesSuccess()) {
			return;
		}
		
		AuthenticationSuccessResponse successResponse = (AuthenticationSuccessResponse)oidcResponse;
		
		try {
			log.info("Received OpenID authentication success response: " + successResponse.toParameters());
		} catch (Exception e) {
			// Just in case toParameters fails
			//createErrorMessagePanel(e.getMessage());
			return;
		}
		
		// Process authorization code if expected
		if (pendingRequest.getAuthenticationRequest().getResponseType().contains("code") && successResponse.getAuthorizationCode() == null) {
			//createErrorMessagePanel("Bad OpenID authentication response: Missing expected authorization code, aborting further processing");
			return;
		}
		
		AuthorizationCode authzCode = successResponse.getAuthorizationCode();
		AccessToken accessToken = successResponse.getAccessToken();
		JWT idToken = successResponse.getIDToken();
		
		if (authzCode != null) {
			
			// Make token endpoint request
			URI tokenEndpointURI = pendingRequest.getProviderMetadata().getTokenEndpointURI();
			OIDCClientInformation clientInfo = pendingRequest.getClientInfo();
			
			AuthorizationGrant authzGrant = new AuthorizationCodeGrant(
				authzCode,
				pendingRequest.getAuthenticationRequest().getRedirectionURI(),
				pendingRequest.getCodeVerifier());
			
			TokenRequest tokenRequest;
			
			if (clientInfo.getOIDCMetadata().getTokenEndpointAuthMethod().equals(ClientAuthenticationMethod.NONE)) {
				// Public client
				tokenRequest = new TokenRequest(tokenEndpointURI, clientInfo.getID(), authzGrant);
			} else {
				// Confidential client
				ClientAuthenticationMethod expectedAuthMethod = clientInfo.getOIDCMetadata().getTokenEndpointAuthMethod();
				
				final ClientAuthentication clientAuth;
				
				if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(expectedAuthMethod)) {
					clientAuth = new ClientSecretBasic(clientInfo.getID(), clientInfo.getSecret());
				} else if (ClientAuthenticationMethod.CLIENT_SECRET_JWT.equals(expectedAuthMethod)) {
					try {
						clientAuth = new ClientSecretJWT(JWTAssertionFactory.create(
							new JWTAssertionDetails(
								new Issuer(clientInfo.getID().getValue()),
								new Subject(clientInfo.getID().getValue()),
								new Audience(tokenEndpointURI.toString())),
							clientInfo.getOIDCMetadata().getTokenEndpointAuthJWSAlg(),
							clientInfo.getSecret()));
					} catch (JOSEException e) {
						//content.addComponent(createErrorMessagePanel(e.getMessage()));
						return;
					}
				} else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.equals(expectedAuthMethod)) {
					clientAuth = new ClientSecretPost(clientInfo.getID(), clientInfo.getSecret());
				} else {
					//createErrorMessagePanel("Unsupported client authentication method: " + expectedAuthMethod);
					return;
				}
				tokenRequest = new TokenRequest(tokenEndpointURI, clientAuth, authzGrant);
			}
			
			try {
				HTTPRequest httpRequest = tokenRequest.toHTTPRequest();
				
				HTTPResponse httpResponse = httpRequest.send();
				
				TokenResponse tokenResponse = OIDCTokenResponseParser.parse(httpResponse);
				
				if (tokenResponse instanceof TokenErrorResponse) {
					
					int httpStatusCode = httpResponse.getStatusCode();
					//createTokenErrorPanel(httpStatusCode, (TokenErrorResponse)tokenResponse));
					return;
				}
				
				OIDCTokenResponse oidcTokenResponse = (OIDCTokenResponse)tokenResponse;
				
				accessToken = oidcTokenResponse.getOIDCTokens().getAccessToken();
				log.info("Access token: " + accessToken.getValue());
				
				if (oidcTokenResponse.getOIDCTokens().getIDToken() != null) {
					idToken = oidcTokenResponse.getOIDCTokens().getIDToken();
				}
				createTokenResponsePanel(oidcTokenResponse);
				
			} catch (Exception e) {
				//createErrorMessagePanel("Couldn't make token request: " + e.getMessage());
				return;
			}
		}
		
		
		// Validate ID token
		if (idToken == null) {
			//createErrorMessagePanel("ID token missing from response, aborting further processing"));
			return;
		}
		
		JWEHeader jweHeader = null;
		
		if (idToken instanceof EncryptedJWT) {
			
			EncryptedJWT encryptedIDToken = (EncryptedJWT)idToken;
			jweHeader = encryptedIDToken.getHeader();
			try {
				decrypt(encryptedIDToken, pendingRequest.getClientInfo().getSecret());
				idToken = JWTParser.parse(encryptedIDToken.getPayload().toString());
			} catch (JOSEException | java.text.ParseException e) {
				//createErrorMessagePanel("Couldn't decrypt ID token: " + e.getMessage());
				return;
			}
		}
		
		IDTokenValidator validator;
		
		if (idToken instanceof SignedJWT) {
			
			if (JWSAlgorithm.Family.SIGNATURE.contains(idToken.getHeader().getAlgorithm())) {
				
				try {
					validator = new IDTokenValidator(
						pendingRequest.getProviderMetadata().getIssuer(),
						pendingRequest.getClientInfo().getID(),
						JWSAlgorithm.parse(idToken.getHeader().getAlgorithm().getName()),
						pendingRequest.getProviderMetadata().getJWKSetURI().toURL());
				} catch (MalformedURLException e) {
						//createErrorMessagePanel("Invalid JWK set URL: " + e.getMessage());
					return;
				}
				
				// Display the remote signing JWK for the ID token
				try {
					List<JWK> candidates = new RemoteJWKSet(pendingRequest.getProviderMetadata().getJWKSetURI().toURL())
						.get(new JWKSelector(new JWKMatcher.Builder()
							.keyType(KeyType.forAlgorithm(idToken.getHeader().getAlgorithm()))
							.keyID(((SignedJWT)idToken).getHeader().getKeyID())
							.build()),
							null);
					
					if (candidates.isEmpty()) {
							//createErrorMessagePanel("No matching signing JWK found");
						return;
					}
					
					//content.addComponent(createSigningJWKPanel(candidates.get(0)));
					
				} catch (Exception e) {
							//createErrorMessagePanel("Couldn't retrieve OpenID provider JWK set: " + e.getMessage());
					return;
				}
				
			} else if (JWSAlgorithm.Family.HMAC_SHA.contains(idToken.getHeader().getAlgorithm())) {
				
				if (pendingRequest.getClientInfo().getSecret() == null) {
							//createErrorMessagePanel("Cannot validate HMAC ID token: Missing client secret");
					return;
				}
				
				validator = new IDTokenValidator(
					pendingRequest.getProviderMetadata().getIssuer(),
					pendingRequest.getClientInfo().getID(),
					JWSAlgorithm.parse(idToken.getHeader().getAlgorithm().getName()),
					pendingRequest.getClientInfo().getSecret());
				
			} else {
				//createErrorMessagePanel("Unsupported JWS algorithm: " + idToken.getHeader().getAlgorithm());
				return;
			}
			
		} else if (idToken instanceof PlainJWT) {
			
			validator = new IDTokenValidator(pendingRequest.getProviderMetadata().getIssuer(), pendingRequest.getClientInfo().getID());
			
		} else {
			//createErrorMessagePanel("Unsupported nested JWT: " + idToken.getParsedString());
			return;
		}
		
		try {
			validator.validate(idToken, pendingRequest.getAuthenticationRequest().getNonce());
		} catch (Exception e) {
			///createErrorMessagePanel("Invalid ID token: " + e.getMessage()));
		}
		
			//content.addComponent(createIDTokenPanel(idToken, jweHeader));
		
		if (accessToken != null) {
			
			// Make UserInfo request
			URI userInfoEndpoint = pendingRequest.getProviderMetadata().getUserInfoEndpointURI();
			
			log.info("Making UserInfo request to " + userInfoEndpoint);
			
			UserInfoRequest userInfoRequest = new UserInfoRequest(userInfoEndpoint, (BearerAccessToken)accessToken);
			
			try {
				HTTPRequest httpRequest = userInfoRequest.toHTTPRequest();
				
				HTTPResponse httpResponse = httpRequest.send();
				
				UserInfoResponse userInfoResponse = UserInfoResponse.parse(httpResponse);
				
				if (userInfoResponse instanceof UserInfoErrorResponse) {
					
					UserInfoErrorResponse userInfoErrorResponse = (UserInfoErrorResponse)userInfoResponse;
					
					String msg = "[ " + userInfoErrorResponse.getErrorObject().getCode() + " ] ";
					msg += userInfoErrorResponse.getErrorObject().getDescription();
					//createErrorMessagePanel("UserInfo error response: " + msg));
				}
				
				UserInfoSuccessResponse userInfoSuccessResponse = (UserInfoSuccessResponse)userInfoResponse;
				
				JWEHeader userInfoJWEHeader = null;
				Header userInfoJWSHeader = null;
				JSONObject jsonObject;
				
				if (userInfoSuccessResponse.getUserInfoJWT() != null) {
					
					JWT jwt = userInfoSuccessResponse.getUserInfoJWT();
					
					if (jwt instanceof EncryptedJWT) {
						EncryptedJWT encryptedJWT = (EncryptedJWT)jwt;
						userInfoJWEHeader = encryptedJWT.getHeader();
						decrypt(encryptedJWT, pendingRequest.getClientInfo().getSecret());
						jwt = JWTParser.parse(encryptedJWT.getPayload().toString());
					}
					
					userInfoJWSHeader = jwt.getHeader();
					jsonObject = jwt.getJWTClaimsSet().toJSONObject();
					
				} else {
					jsonObject = userInfoSuccessResponse.getUserInfo().toJSONObject();
				}
				
				//createUserInfoPanel(userInfoJWEHeader, userInfoJWSHeader, jsonObject));
				
			} catch (Exception e) {
				//createErrorMessagePanel("Couldn't make UserInfo request: " + e.getMessage()));
			}
		}
	}
}
