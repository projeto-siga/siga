package br.gov.sp.prodesp.siga.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.nimbusds.jose.Header;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.AESDecrypter;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.KeyType;
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

import br.gov.sp.prodesp.siga.client.HTTPRequestParametersInterceptorServlet;
import br.gov.sp.prodesp.siga.client.PendingAuthenticationRequest;
import net.minidev.json.JSONObject;

@WebServlet(urlPatterns = "/callBack", name = "CallbackServlet", asyncSupported = true)
public class CallBackServlet extends HTTPRequestParametersInterceptorServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger("[OIDC - CallBackServlet]");

	private static final String PENDING_REQUESTS = "pending-requests";
	private static final String HTTP_REQUEST_PARAMETERS2 = "http-request-parameters";
	private static final String PUBLIC_APP_LOGIN_SP = "public/app/loginSSO";
	public  static final String PUBLIC_CPF_USER_SSO = "cpfUserSSO";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("## doGET ##");

		AuthenticationResponse oidcResponse;
		final Map<String, List<String>> params;

		params = (Map<String, List<String>>) req.getSession().getAttribute(HTTP_REQUEST_PARAMETERS2);

		try {
			oidcResponse = AuthenticationResponseParser.parse(new URI("https:///"), params);
		} catch (Exception e) {
			createError("Invalid OpenID authentication response: " + e.getMessage());
			return;
		}

		if (oidcResponse.indicatesSuccess()) {
			// Display success parameters
			createAuthzSuccess((AuthenticationSuccessResponse)oidcResponse);
		} else {
			// Display error parameters
			createError("OIDC falhou.");
		}

		Map<State,PendingAuthenticationRequest> pendingRequests = (Map<State,PendingAuthenticationRequest>) req.getSession().getAttribute(PENDING_REQUESTS);
		

		if (pendingRequests == null) {
			// Hostname of callback doesn't match the URL from which the client was launched
			createError("Unexpected callback: The OpenID client was started from a hostname / IP address that differs from the hostname / IP address of the redirect_uri");
			return;
		}
		
		if (oidcResponse.getState() == null) {
			// State is recommended in OAuth 2.0, but mandatory in OpenID Connect
			createError("Invalid OpenID authentication response: Missing state parameter");
			return;
		}
		
		PendingAuthenticationRequest pendingRequest = pendingRequests.get(oidcResponse.getState());
		if (pendingRequest == null) {
			createError("Unexpected OpenID authentication response: " + "No pending OpenID authentication request with state " + oidcResponse.getState());
			return;
		}
		
		// If error stop here
		if (! oidcResponse.indicatesSuccess()) {
			return;
		}
		
		AuthenticationSuccessResponse successResponse = (AuthenticationSuccessResponse)oidcResponse;
		
		try {
			log.debug("Received OpenID authentication success response: " + successResponse.toParameters());
		} catch (Exception e) {
			// Just in case toParameters fails
			createError(e.getMessage());
			return;
		}

		// Process authorization code if expected
		if (pendingRequest.getAuthenticationRequest().getResponseType().contains("code") && successResponse.getAuthorizationCode() == null) {
			createError("Bad OpenID authentication response: Missing expected authorization code, aborting further processing");
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
						createError(e.getMessage());
						return;
					}
				} else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.equals(expectedAuthMethod)) {
					clientAuth = new ClientSecretPost(clientInfo.getID(), clientInfo.getSecret());
				} else {
					createError("Unsupported client authentication method: " + expectedAuthMethod);
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
					createError("Status code: " + httpStatusCode + tokenResponse.toErrorResponse());
					return;
				}
				
				OIDCTokenResponse oidcTokenResponse = (OIDCTokenResponse)tokenResponse;
				
				accessToken = oidcTokenResponse.getOIDCTokens().getAccessToken();
				log.debug("Access token: " + accessToken.getValue());
				
				if (oidcTokenResponse.getOIDCTokens().getIDToken() != null) {
					idToken = oidcTokenResponse.getOIDCTokens().getIDToken();
				}
				
			} catch (Exception e) {
				createError("Couldn't make token request: " + e.getMessage());
				return;
			}
		}
		
		// Validate ID token
		if (idToken == null) {
			createError("ID token missing from response, aborting further processing");
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
				createError("Couldn't decrypt ID token: " + e.getMessage());
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
						createError("Invalid JWK set URL: " + e.getMessage());
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
							createError("No matching signing JWK found");
						return;
					}
					
				} catch (Exception e) {
						createError("Couldn't retrieve OpenID provider JWK set: " + e.getMessage());
					return;
				}
				
			} else if (JWSAlgorithm.Family.HMAC_SHA.contains(idToken.getHeader().getAlgorithm())) {
				
				if (pendingRequest.getClientInfo().getSecret() == null) {
						createError("Cannot validate HMAC ID token: Missing client secret");
					return;
				}
				
				validator = new IDTokenValidator(
					pendingRequest.getProviderMetadata().getIssuer(),
					pendingRequest.getClientInfo().getID(),
					JWSAlgorithm.parse(idToken.getHeader().getAlgorithm().getName()),
					pendingRequest.getClientInfo().getSecret());
				
			} else {
				createError("Unsupported JWS algorithm: " + idToken.getHeader().getAlgorithm());
				return;
			}
			
		} else if (idToken instanceof PlainJWT) {
			
			validator = new IDTokenValidator(pendingRequest.getProviderMetadata().getIssuer(), pendingRequest.getClientInfo().getID());
			
		} else {
			createError("Unsupported nested JWT: " + idToken.getParsedString());
			return;
		}
		
		try {
			validator.validate(idToken, pendingRequest.getAuthenticationRequest().getNonce());
		} catch (Exception e) {
			createError("Invalid ID token: " + e.getMessage());
		}
		
		createIDToken(idToken, jweHeader);
		
		if (accessToken != null) {
			
			// Make UserInfo request
			URI userInfoEndpoint = pendingRequest.getProviderMetadata().getUserInfoEndpointURI();
			
			log.debug("Making UserInfo request to " + userInfoEndpoint);
			
			UserInfoRequest userInfoRequest = new UserInfoRequest(userInfoEndpoint, (BearerAccessToken)accessToken);
			
			try {
				HTTPRequest httpRequest = userInfoRequest.toHTTPRequest();
				HTTPResponse httpResponse = httpRequest.send();
				UserInfoResponse userInfoResponse = UserInfoResponse.parse(httpResponse);
				
				if (userInfoResponse instanceof UserInfoErrorResponse) {
				
					UserInfoErrorResponse userInfoErrorResponse = (UserInfoErrorResponse)userInfoResponse;
					String msg = "[ " + userInfoErrorResponse.getErrorObject().getCode() + " ] ";
					msg += userInfoErrorResponse.getErrorObject().getDescription();
					createError("UserInfo error response: " + msg);
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
				
				createUserInfo(userInfoJWEHeader, userInfoJWSHeader, jsonObject);
				
				reqSession(jsonObject, req);
				
				handleCallback(resp);
				 
			} catch (Exception e) {
				createError("Couldn't make UserInfo request: " + e.getMessage());
			}
		}
	}
	
	/*
	 * SESSION
	 */
	private void reqSession(JSONObject jsonObject, HttpServletRequest req) {
		String cpf = (String) jsonObject.get("sub");
		if(!cpf.isEmpty()){
			req.getSession().setAttribute(PUBLIC_CPF_USER_SSO, cpf);
		}
		log.debug("CPF SSO: " + cpf);
	}

	private void handleCallback(HttpServletResponse resp) {
		try {
			resp.sendRedirect(PUBLIC_APP_LOGIN_SP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void imprimirParametroTela(HttpServletRequest req, HttpServletResponse resp){
		
		// 	------------------------------- IMPRIMIR PARAMETER  ----------------------------
		String code = (String) req.getParameter("code");
		String scope = (String) req.getParameter("scope");
		String state = (String) req.getParameter("state");
		String session_state = (String) req.getParameter("session_state");

		try (PrintWriter out = resp.getWriter()) {
			out.println("<html><head>");
			out.println("<title>SIGA</title>");
			out.println("</head><body>");
			out.println("<h1>Callback</h1>");
			out.println("Code: " + code);
			out.println("<br> Scope: " + scope);
			out.println("<br> State: " + state);
			out.println("<br> Session_state: " + session_state);
			out.println("<br> -------------------------");
			out.println("</body></html>");
		} catch (Exception ex) {

		}

	}

	private void createAuthzSuccess(final AuthenticationSuccessResponse successResponse) {
		String authorizationCode = "";
		String idToken = "";
		String accessToken = "";
		String state = "";
		String sessionState = "";

		if (successResponse.getAuthorizationCode() != null) {
			authorizationCode = successResponse.getAuthorizationCode().getValue();
			log.debug("Authorization Code: " + authorizationCode);
		}

		if (successResponse.getIDToken() != null) {
			idToken = successResponse.getIDToken().getParsedString();
			log.debug("ID token: " + idToken);
		}

		if (successResponse.getAccessToken() != null) {
			accessToken = successResponse.getAccessToken().getValue();
			log.debug("Access token: " + accessToken);
		}

		if (successResponse.getState() != null) {
			state = successResponse.getState().getValue();
			log.debug("State: " + state);
		}

		if (successResponse.getSessionState() != null) {
			sessionState = successResponse.getSessionState().getValue();
			log.debug("Session State: " + sessionState);
		}
	}

	private void createTokenResponse(final OIDCTokenResponse tokenResponse) {
		
		JWT idToken = tokenResponse.getOIDCTokens().getIDToken();
		
		if (idToken != null && idToken.getParsedString() != null) {
			log.debug("IdToke: " + idToken.getParsedString());
		}
		
		AccessToken accessToken = tokenResponse.getOIDCTokens().getAccessToken();
		
		if (accessToken != null) {
			log.debug("Id Acces Token: " + accessToken.getType().getValue());
			
			BearerAccessToken bat = (BearerAccessToken)accessToken;
			bat.getValue();
			
			if (bat.getScope() != null) {
				log.debug("Scope: " + bat.getScope().toString());
			}
		}
		RefreshToken refreshToken = tokenResponse.getOIDCTokens().getRefreshToken();
		log.debug("Refresh Token: " + refreshToken.toString());
	}
	
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
	
	private void createUserInfo(final JWEHeader jweHeader, final Header jwsHeader, final JSONObject userInfo) throws java.text.ParseException {

			log.debug("UserInfo");

			PrettyJson jsonFormatter = new PrettyJson(PrettyJson.Style.COMPACT);
			if (jweHeader != null) {
				log.debug(jsonFormatter.parseAndFormat(jweHeader.toJSONObject().toJSONString()));
			}

			if (jwsHeader != null) {
				log.debug(jsonFormatter.parseAndFormat(jwsHeader.toJSONObject().toJSONString()));
			}
			log.debug(jsonFormatter.parseAndFormat(userInfo.toJSONString()));
			
			
	}
	
	private void createError(String errorResponse) {
		if (errorResponse != null) {
			try {
				throw new Exception(errorResponse);
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
	
	private void createIDToken(final JWT idToken, final JWEHeader jweHeader) {
		
		PrettyJson jsonFormatter = new PrettyJson(PrettyJson.Style.COMPACT);
		
		String formattedJWEHeader = null;
		String formattedJWSHeader = null;
		String formattedClaims = null;
		String formattedSignature = null;
		
		try {
			formattedJWEHeader = jweHeader != null ? jsonFormatter.parseAndFormat(jweHeader.toJSONObject().toJSONString()) : null;
			formattedJWSHeader = jsonFormatter.parseAndFormat(idToken.getHeader().toJSONObject().toJSONString());
			formattedClaims = jsonFormatter.parseAndFormat(idToken.getJWTClaimsSet().toJSONObject().toJSONString());
			formattedSignature = ((SignedJWT) idToken).getSignature().toString();
			
		} catch (Exception e) {
			// unlikely
			throw new RuntimeException("Failed to format ID token: " + e.getMessage());
		}
		
		log.debug(formattedJWEHeader);
		log.debug(formattedJWSHeader);
		log.debug(formattedClaims);
		log.debug(formattedSignature);
	}
}
