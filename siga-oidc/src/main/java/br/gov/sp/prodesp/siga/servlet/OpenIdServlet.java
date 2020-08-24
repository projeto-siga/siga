package br.gov.sp.prodesp.siga.servlet;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.oauth2.sdk.ResponseMode;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;
import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.Display;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.Prompt;
import com.nimbusds.openid.connect.sdk.SubjectType;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientMetadata;

import br.gov.sp.prodesp.siga.client.LoadProperties;
import br.gov.sp.prodesp.siga.client.PendingAuthenticationRequest;

/**
 * OpenID Connect client
 * Provider LoginSP
 * Fluxo de autenticação (hybrid flow).
 * 
 * 02/2020
 */
@WebServlet("/openIdServlet")
public class OpenIdServlet extends javax.servlet.http.HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger("[OIDC]");
	private LoadProperties oidcParameter;
	private final Map<State,PendingAuthenticationRequest> pendingRequests = new Hashtable<>();
	
	@Override
	public void init(){
		oidcParameter = new LoadProperties(getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getSession().setAttribute("pending-requests", pendingRequests);
        try{
   			AuthenticationRequest authRequest1 = buildAuthenticationRequest();
    			
			if (authRequest1 != null) {
				String uriString = authRequest1.toURI().toString();
				log.debug("LOGIN SP: "  + uriString);
		        redirectToLogin(req, resp, uriString);
			}    			
		}catch(Exception e){
			log.error("Log OpenIdServlet: " + e.getMessage());
		}
	  
	}

	private void redirectToLogin(HttpServletRequest request, HttpServletResponse response, String redirectTo) throws IOException {
		log.debug("Redirect: "  + redirectTo);
        response.sendRedirect(redirectTo);
    }
	
	/**
	 * Builds an OpenID authentication request from the parameters entered in
	 * the provider, client and request forms.
	 */
	private AuthenticationRequest buildAuthenticationRequest() {

		try {
			// Compose the OP metadata object
			Issuer issuer = new Issuer(oidcParameter.getParametersOIDC().getIss());
			List<SubjectType> subjectTypes = Collections.singletonList(SubjectType.PUBLIC);
			URI jwkSetURI = new URI(oidcParameter.getParametersOIDC().getJwksUri());

			OIDCProviderMetadata opMetadata = new OIDCProviderMetadata(issuer, subjectTypes, jwkSetURI);
			opMetadata.setAuthorizationEndpointURI(new URI(oidcParameter.getParametersOIDC().getAuthzUri()));
			opMetadata.setTokenEndpointURI(new URI(oidcParameter.getParametersOIDC().getTokenUri()));
			opMetadata.setUserInfoEndpointURI(new URI(oidcParameter.getParametersOIDC().getUserInfoUri()));
			opMetadata.applyDefaults();

			// Set the client credentials and callback
			String clientIDString = oidcParameter.getParametersOIDC().getClientId();
			if (clientIDString.isEmpty()) {
				throw new Exception("Missing client_id");
			}
			ClientID clientID = new ClientID(clientIDString);

			String redirectURIString = oidcParameter.getParametersOIDC().getRedirectUri();
			if (redirectURIString.isEmpty()) {
				throw new Exception("Missing client redirect_uri");
			}
			URI redirectURI = new URI(redirectURIString);
			ClientAuthenticationMethod clientAuthMethod = ClientAuthenticationMethod
					.parse(oidcParameter.getParametersOIDC().getTokenEndpointAuthMethod());

			Secret clientSecret = null;
			if (!clientAuthMethod.equals(ClientAuthenticationMethod.NONE)) {
				String clientSecretString = oidcParameter.getParametersOIDC().getClientSecret();
				if (clientSecretString.isEmpty()) {
					throw new Exception("Missing client_secret");
				}
				clientSecret = new Secret(clientSecretString);
			}

			JWSAlgorithm clientAuthJWS = null;
			if (clientAuthMethod.equals(ClientAuthenticationMethod.CLIENT_SECRET_JWT)) {
				clientAuthJWS = JWSAlgorithm.parse(oidcParameter.getParametersOIDC().getTokenEndpointAuthSigningAlg());
			}

			CodeChallengeMethod codeChallengeMethod = null;
			/*String pkceMethodString = oidcParameter.getParametersOIDC().getCodeChallengeMethod();
			if (!"[ disable ]".equals(pkceMethodString)) {
				codeChallengeMethod = CodeChallengeMethod.parse(pkceMethodString);
				log.info("PKCE method: " + codeChallengeMethod);
			}*/

			OIDCClientMetadata clientMetadata = new OIDCClientMetadata();
			clientMetadata.setTokenEndpointAuthMethod(clientAuthMethod);
			clientMetadata.setTokenEndpointAuthJWSAlg(clientAuthJWS);
			clientMetadata.setRedirectionURI(redirectURI);
			OIDCClientInformation clientInfo = new OIDCClientInformation(clientID, null, clientMetadata, clientSecret);

			// Compose the OIDC auth request
			ResponseType responseType = ResponseType.parse(oidcParameter.getParametersOIDC().getResponseType());

			Scope scope = new Scope();

			if (oidcParameter.getParametersOIDC().getScopeOpenId())
				scope.add("openid");

			if (oidcParameter.getParametersOIDC().getScopeEmail())
				scope.add("email");

			if (oidcParameter.getParametersOIDC().getScopeProfile())
				scope.add("profile");

			if (oidcParameter.getParametersOIDC().getScopePhone())
				scope.add("phone");

			if (oidcParameter.getParametersOIDC().getScopeAddress())
				scope.add("address");

			if (oidcParameter.getParametersOIDC().getScopeOfflineAccess())
				scope.add("offline_access");

			String authzEndpointURIString = oidcParameter.getParametersOIDC().getAuthzUri();

			if (authzEndpointURIString.isEmpty()) {
				throw new Exception("Missing authorization endpoint URI");
			}

			URI authzEndpointURI = new URI(authzEndpointURIString);

			String promptString = oidcParameter.getParametersOIDC().getPrompt();

			Prompt prompt = promptString.equals("default") ? null : Prompt.parse(promptString);

			int maxAge = Integer.parseInt(oidcParameter.getParametersOIDC().getMaxAge());

			TimeUnit maxAgeTimeUnit = parseTimeUtil(oidcParameter.getParametersOIDC().getMaxAgeTimeUnit());

			maxAge = new Long(maxAgeTimeUnit.toSeconds(maxAge)).intValue();

			String idTokenHintString = oidcParameter.getParametersOIDC().getIdTokenHint();

			JWT idTokenHint = null;

			if (!idTokenHintString.isEmpty()) {
				try {
					idTokenHint = JWTParser.parse(idTokenHintString);
				} catch (java.text.ParseException e) {
					// ignore
				}
			}

			//String loginHint = oidcParameter.getParametersOIDC().getLoginHint();
			String loginHint = null;
			
			//String responseModeString = oidcParameter.getParametersOIDC().getResponseMode();
			
			//String responseModeString = null;

			ResponseMode responseMode = null;
			
			// ResponseMode responseMode = responseModeString.equals("default") ? null : new ResponseMode(responseModeString);

			CodeVerifier codeVerifier = null;

			if (codeChallengeMethod != null) {
				codeVerifier = new CodeVerifier();
			}

			AuthenticationRequest authRequest = 
					new AuthenticationRequest.Builder(responseType, scope, clientID,redirectURI).
					responseMode(responseMode)
					.display(Display.PAGE)
					.state(new State())
					.nonce(new Nonce())
					.prompt(prompt)
					.maxAge(maxAge)
					.idTokenHint(idTokenHint)
					.loginHint(loginHint)
					.codeChallenge(codeVerifier, codeChallengeMethod)
					.endpointURI(authzEndpointURI)
					.build();

			// Save request to session so the popup window can fetch it
			pendingRequests.put(authRequest.getState(), new PendingAuthenticationRequest(opMetadata, clientInfo, authRequest, codeVerifier));

			return authRequest;

		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	private TimeUnit parseTimeUtil(String c) {
		switch (c) {
		case "s":
			return TimeUnit.SECONDS;
		case "m":
			return TimeUnit.MINUTES;
		case "h":
			return TimeUnit.HOURS;
		case "d":
			return TimeUnit.DAYS;
		default:
			throw new IllegalArgumentException(String.format("%s is not a valid code [smhd]", c));
		}

	}

}
