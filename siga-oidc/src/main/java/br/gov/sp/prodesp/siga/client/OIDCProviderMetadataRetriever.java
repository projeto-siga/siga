package br.gov.sp.prodesp.siga.client;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;

import net.minidev.json.JSONObject;


/**
 * OpenID provider metadata retriever.
 */
public class OIDCProviderMetadataRetriever {
	
	private static final String WELL_KNOWN_OPENID_CONFIGURATION = ".well-known/openid-configuration";


	/**
	 * Retorna o OpenID provider metadata URL from especificado por issuer
	 * URL.
	 */
	static URL composeMetadataURL(final URL issuerURL) {
		
		String urlString = issuerURL.toString();
		
		if (! urlString.endsWith("/"))
			urlString += "/";
		
		urlString += WELL_KNOWN_OPENID_CONFIGURATION;
		
		try {
			return new URL(urlString);
			
		} catch (MalformedURLException e) {
			throw new RuntimeException("Invalid issuer URI: " + urlString);
		}
	}
	
	static OIDCProviderMetadata retrieve(final URL opMetadataURL)
		throws IOException, ParseException {
		
		HTTPRequest httpRequest = new HTTPRequest(HTTPRequest.Method.GET, opMetadataURL);
		httpRequest.setConnectTimeout(1000); // ms
		httpRequest.setReadTimeout(1000); // ms
		httpRequest.setFollowRedirects(true);
		
		HTTPResponse httpResponse = httpRequest.send();
		
		int statusCode = httpResponse.getStatusCode();
		
		if (statusCode < 200 || statusCode > 299) {
			String msg = "The server returned HTTP " + statusCode;
			if (httpResponse.getStatusMessage() != null) {
				msg += " " + httpResponse.getStatusMessage();
			}
			throw new IOException(msg);
		}
		
		JSONObject jsonObject = httpResponse.getContentAsJSONObject();
		
		return OIDCProviderMetadata.parse(jsonObject);
	}
}
