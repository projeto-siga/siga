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
	
	
	/**
	 * Returns the OpenID provider metadata URL from the specified issuer
	 * URL.
	 *
	 * @param issuerURL The issuer URL. Must not be {@code null}.
	 *
	 * @return The OpenID provider metadata URL.
	 */
	static URL composeMetadataURL(final URL issuerURL) {
		
		String urlString = issuerURL.toString();
		
		// Append the well known metadata
		if (! urlString.endsWith("/"))
			urlString += "/";
		
		urlString += ".well-known/openid-configuration";
		
		try {
			return new URL(urlString);
			
		} catch (MalformedURLException e) {
			throw new RuntimeException("Invalid issuer URI: " + urlString);
		}
	}
	
	
	/**
	 * Retrieves the OpenID provider metadata for the specified URL.
	 *
	 * @param opMetadataURL The OpenID provider metadata URL. Must not be
	 *                      {@code null}.
	 *
	 * @return The OpenID provider metadata.
	 */
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
