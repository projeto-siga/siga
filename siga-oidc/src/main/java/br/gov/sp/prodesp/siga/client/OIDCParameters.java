package br.gov.sp.prodesp.siga.client;


import java.util.logging.Logger;

/**
 * OpenID Connect OP, RP and request parameters.
 * 
 */
public class OIDCParameters {
	
	protected static final Logger log = Logger.getLogger(OIDCParameters.class.getName());
	
	private String iss = "";
	private String jwksUri = "";
	private String authzUri = "";
	private String tokenUri = "";
	private String userInfoUri = "";
	private String clientId = "";
	private String clientSecret = "";
	private String redirectUri = "";
	private String tokenEndpointAuthMethod = "";
	private String tokenEndpointAuthSigningAlg = "";
	
	private String responseType = "";
	private Boolean scopeOpenId = true;
	private Boolean scopeEmail = true;
	private Boolean scopeProfile = false;
	private Boolean scopePhone = false;
	private Boolean scopeAddress = false;
	private Boolean scopeOfflineAccess = false;
	private String prompt = "";
	private String codeChallengeMethod = "";
	
	private String maxAge = "";
	private String maxAgeTimeUnit = "";
	private String idTokenHint = "";
	private String loginHint = "";
	private String responseMode = "";
	
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getJwksUri() {
		return jwksUri;
	}
	public void setJwksUri(String jwksUri) {
		this.jwksUri = jwksUri;
	}
	public String getAuthzUri() {
		return authzUri;
	}
	public void setAuthzUri(String authzUri) {
		this.authzUri = authzUri;
	}
	public String getTokenUri() {
		return tokenUri;
	}
	public void setTokenUri(String tokenUri) {
		this.tokenUri = tokenUri;
	}
	public String getUserInfoUri() {
		return userInfoUri;
	}
	public void setUserInfoUri(String userInfoUri) {
		this.userInfoUri = userInfoUri;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getTokenEndpointAuthMethod() {
		return tokenEndpointAuthMethod;
	}
	public void setTokenEndpointAuthMethod(String tokenEndpointAuthMethod) {
		this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
	}
	public String getTokenEndpointAuthSigningAlg() {
		return tokenEndpointAuthSigningAlg;
	}
	public void setTokenEndpointAuthSigningAlg(String tokenEndpointAuthSigningAlg) {
		this.tokenEndpointAuthSigningAlg = tokenEndpointAuthSigningAlg;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public Boolean getScopeOpenId() {
		return scopeOpenId;
	}
	public void setScopeOpenId(Boolean scopeOpenId) {
		this.scopeOpenId = scopeOpenId;
	}
	public Boolean getScopeEmail() {
		return scopeEmail;
	}
	public void setScopeEmail(Boolean scopeEmail) {
		this.scopeEmail = scopeEmail;
	}
	public Boolean getScopeProfile() {
		return scopeProfile;
	}
	public void setScopeProfile(Boolean scopeProfile) {
		this.scopeProfile = scopeProfile;
	}
	public Boolean getScopePhone() {
		return scopePhone;
	}
	public void setScopePhone(Boolean scopePhone) {
		this.scopePhone = scopePhone;
	}
	public Boolean getScopeAddress() {
		return scopeAddress;
	}
	public void setScopeAddress(Boolean scopeAddress) {
		this.scopeAddress = scopeAddress;
	}
	public Boolean getScopeOfflineAccess() {
		return scopeOfflineAccess;
	}
	public void setScopeOfflineAccess(Boolean scopeOfflineAccess) {
		this.scopeOfflineAccess = scopeOfflineAccess;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public String getCodeChallengeMethod() {
		return codeChallengeMethod;
	}
	public void setCodeChallengeMethod(String codeChallengeMethod) {
		this.codeChallengeMethod = codeChallengeMethod;
	}
	public String getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}
	public String getMaxAgeTimeUnit() {
		return maxAgeTimeUnit;
	}
	public void setMaxAgeTimeUnit(String maxAgeTimeUnit) {
		this.maxAgeTimeUnit = maxAgeTimeUnit;
	}
	public String getIdTokenHint() {
		return idTokenHint;
	}
	public void setIdTokenHint(String idTokenHint) {
		this.idTokenHint = idTokenHint;
	}
	public String getLoginHint() {
		return loginHint;
	}
	public void setLoginHint(String loginHint) {
		this.loginHint = loginHint;
	}
	public String getResponseMode() {
		return responseMode;
	}
	public void setResponseMode(String responseMode) {
		this.responseMode = responseMode;
	}
	@Override
	public String toString() {
		return "OIDCParameters [iss=" + iss + ", jwksUri=" + jwksUri + ", authzUri=" + authzUri + ", tokenUri="
				+ tokenUri + ", userInfoUri=" + userInfoUri + ", clientId=" + clientId + ", clientSecret="
				+ clientSecret + ", redirectUri=" + redirectUri + ", tokenEndpointAuthMethod=" + tokenEndpointAuthMethod
				+ ", tokenEndpointAuthSigningAlg=" + tokenEndpointAuthSigningAlg + ", responseType=" + responseType
				+ ", scopeOpenId=" + scopeOpenId + ", scopeEmail=" + scopeEmail + ", scopeProfile=" + scopeProfile
				+ ", scopePhone=" + scopePhone + ", scopeAddress=" + scopeAddress + ", scopeOfflineAccess="
				+ scopeOfflineAccess + ", prompt=" + prompt + ", codeChallengeMethod=" + codeChallengeMethod
				+ ", maxAge=" + maxAge + ", maxAgeTimeUnit=" + maxAgeTimeUnit + ", idTokenHint=" + idTokenHint
				+ ", loginHint=" + loginHint + ", responseMode=" + responseMode + "]";
	}

	
}
