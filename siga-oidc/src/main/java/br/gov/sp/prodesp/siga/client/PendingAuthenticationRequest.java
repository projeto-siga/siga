package br.gov.sp.prodesp.siga.client;


import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;


/**
 * Pending OpenID Connect authentication request and context details.
 */
public final class PendingAuthenticationRequest {
	
	
	private final OIDCProviderMetadata opMetadata;
	private final OIDCClientInformation clientInfo;
	private final AuthenticationRequest authRequest;
	private final CodeVerifier codeVerifier;
	
	public PendingAuthenticationRequest(final OIDCProviderMetadata opMetadata,
					    final OIDCClientInformation clientInfo,
					    final AuthenticationRequest authRequest,
					    final CodeVerifier codeVerifier) {
		
		assert opMetadata != null;
		this.opMetadata = opMetadata;
		
		assert clientInfo != null;
		this.clientInfo = clientInfo;
		
		assert authRequest != null;
		this.authRequest = authRequest;
		
		this.codeVerifier = codeVerifier;
	}
	
	public OIDCProviderMetadata getProviderMetadata() {
		return opMetadata;
	}
	
	public OIDCClientInformation getClientInfo() {
		return clientInfo;
	}
	
	public AuthenticationRequest getAuthenticationRequest() {
		return authRequest;
	}
	
	public CodeVerifier getCodeVerifier() {
		return codeVerifier;
	}
}