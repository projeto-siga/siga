/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 *
 *     This file is part of SIGA.
 *
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.base;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.picketlink.common.util.StringUtil;

/**
 *
 * @author Rodrigo Ramalho hodrigohamalho@gmail.com
 *
 *         Essa classe devera ser usada para requests entre modulos do SIGA. Se
 *         fizer um GET direto o request sera processado como feito por um
 *         usuario anonimo e retornara um form de autenticao.
 */
public class SigaHTTP {

	private Header[] headers;
	private final String COOKIE = "cookie";
	private final String SAMLRequest = "SAMLRequest";
	private final String SAMLResponse = "SAMLResponse";
	private final String JSESSIONID_PREFIX = "JSESSIONID=";
	private final String SET_COOKIE = "set-cookie";
	private final String HTTP_POST_BINDING = "HTTP Post Binding";
	private final String doubleQuotes = "\"";
	private static final int MAX_RETRY = 3;
	private int retryCount = 0;
	private String idp;

	/**
	 * @param URL
	 *            pode ser a url completa ou relativa.
	 * @param request
	 *            (se for modulo play, setar pra null)
	 * @param cookieValue
	 *            (necessario apenas nos modulos play)
	 */
	public String get(String URL, HttpServletRequest request, CookieStore cookieStore)
			throws Exception {
		this.retryCount = 0;

		String cValue = getIdp(request);

		return handleAuthentication(URL, request, cookieStore, cValue);
	}

	public HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SigaSSLSocketFactory sf = new SigaSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	String handleAuthentication(String URL, HttpServletRequest request,
			CookieStore cookieStore, String cValue) throws Exception {
		String html = "";

		Executor exec = Executor.newInstance(getNewHttpClient());
		if (cookieStore == null)
			cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", cValue);
		cookie.setPath("/sigaidp");
		cookie.setDomain("sigat.jfrj.jus.br");
		cookie.setSecure(false);
		cookie.setVersion(1);
		cookieStore.addCookie(cookie);
		exec.cookieStore(cookieStore);

		try {
			// Efetua o request para o Service Provider (modulo)
			// Atribui o html retornado e pega o header do Response
			// Se a aplicacao ja efetuou a autenticaao entre o modulo
			// da URL o conteudo sera trazido nesse primeiro GET
			// Caso contrario passara pelo processo de autenticao
			// (if abaixo)
			HttpResponse response = exec.execute(Request.Get(URL))
					.returnResponse();
			html = handleResponse(html, response);
			//currentCookie = extractSetCookie(currentCookie, response);

			// Verifica se retornou o form de autenticao do
			// picketlink
			if (html.contains(HTTP_POST_BINDING)) {
				if (StringUtil.isNullOrEmpty(cValue)) {
					return "";
				}

				if (html.contains(SAMLRequest)) {
					// Atribui o valor do SAMLRequest contido no html retornado
					// no GET efetuado.
					String SAMLRequestValue = getAttributeValueFromHtml(html,
							SAMLRequest);
					// Atribui a URL do IDP (sigaidp)
					String idpURL = getAttributeActionFromHtml(html);
					if (!idpURL.contains("http"))
						idpURL = completeURL(URL, idpURL);

					// Faz um novo POST para o IDP com o SAMLRequest como
					// parametro
					// e
					// utilizando o sessionID do IDP
					response = exec
							.execute(
									Request.Post(idpURL)
											.useExpectContinue()
											.addHeader("content-type",
													"application/x-www-form-urlencoded")
											// .addHeader(COOKIE, idpCookie)
											.bodyForm(
													Form.form()
															.add(SAMLRequest,
																	SAMLRequestValue)
															.build()))
							.returnResponse();

					html = handleResponse(html, response);
				}

				if (html.contains(SAMLResponse)) {
					// Extrai o valor do SAMLResponse
					// Caso o SAMLResponse nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¯ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¿ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â£o esteja
					// disponÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â­vel
					// aqui,
					// ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¯ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¿ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â© porque
					// o JSESSIONID utilizado nÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â£o foi o do IDP.
					String SAMLResponseValue = getAttributeValueFromHtml(html,
							SAMLResponse);
					String spURL = getAttributeActionFromHtml(html);
					if (!spURL.contains("http"))
						spURL = completeURL(URL, spURL);

					response = exec
							.execute(
									Request.Post(spURL)
											.useExpectContinue()
											.addHeader("Content-Type",
													"application/x-www-form-urlencoded")
											.addHeader("Accept",
													"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
											.addHeader("Accept-Encoding",
													"gzip, deflate")
											.addHeader("Accept-Language",
													"en-US,en;q=0.8,pt;q=0.6")
											.bodyForm(
													Form.form()
															.add(SAMLResponse,
																	SAMLResponseValue)
															.build()))
							.returnResponse();

					if (response.getStatusLine().getStatusCode() == 302)
						response = exec.execute(
								Request.Get(response.getFirstHeader("Location")
										.getValue())).returnResponse();

					html = handleResponse(html, response);
					if (isAuthPage(html)) {
						html = exec
								.execute(
										Request.Post(spURL)
												.useExpectContinue()
												.addHeader("Content-Type",
														"application/x-www-form-urlencoded")
												// .addHeader(COOKIE,
												// currentCookie)
												.bodyForm(
														Form.form()
																.add(SAMLResponse,
																		SAMLResponseValue)
																.build()))
								.returnContent().asString();
					}
				}
			}
		} catch (HTTPException httpE) {
			throw new HTTPException(httpE.getStatusCode());
		} catch (IOException io) {
		}

		// tryAgain(URL, request, cookieValue, html);

		return html;
	}

	private String completeURL(String URL, String idpURL) {
		return URL.substring(0, StringUtils.ordinalIndexOf(URL, "/", 3))
				+ idpURL;
	}

	private String extractSetCookie(String currentCookie, HttpResponse response) {
		for (Header header : response.getAllHeaders()) {
			if (header.getName().equals("Set-Cookie")) {
				if (header.getValue().contains(";")) {
					String headers[] = header.getValue().split(";");
					for (String h : headers) {
						if (h.contains(JSESSIONID_PREFIX)) {
							currentCookie = h;
							break;
						}
					}
				}
			}
		}
		return currentCookie;
	}

	private String handleResponse(String html, HttpResponse response)
			throws HTTPException, IllegalStateException, IOException {
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new HTTPException(response.getStatusLine().getStatusCode());
		} else {
			html = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
		}
		return html;
	}

	// private void tryAgain(String URL, HttpServletRequest request,
	// String cookieValue, String html) throws Exception {
	// if (isErrorPage(html) || isAuthPage(html) || isIDPPage(html)) {
	// if (retryCount < MAX_RETRY) {
	// retryCount++;
	// handleAuthentication(URL, request, cookieValue);
	// }
	// }
	// }

	public boolean isErrorPage(String html) {
		return html.contains("<title>")
				&& html.contains("NÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â£o Foi PossÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â­vel Completar a OperaÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â§ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â£o");
	}

	public boolean isAuthPage(String html) {
		return html.contains("Senha")
				&& html.contains("MatrÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â­cula");
	}

	public boolean isIDPPage(String html) {
		return html.contains("siga-modules") && html.contains("siga-box");
	}

	private String getCookie(HttpServletRequest request, String cookieValue) {
		if (cookieValue == null || cookieValue.isEmpty()) {
			return request.getSession().getId();
		}
		return cookieValue;
	}

	private String extractCookieFromHeader(String headerValue) {
		return headerValue.substring(headerValue.indexOf("=") + 1,
				headerValue.indexOf(";"));
	}

	private String getHeader(String headerName) throws ElementNotFoundException {

		for (Header header : headers) {
			if (header.getName().equalsIgnoreCase(headerName)) {
				return header.getValue();
			}
		}

		throw new ElementNotFoundException("Header " + headerName
				+ " Not founded on headers variable");
	}

	private String getAttributeValueFromHtml(String htmlContent,
			String attribute) {
		String value = "";

		Document doc = Jsoup.parse(htmlContent);
		// Get SAMLRequest value
		for (Element el : doc.select("input")) {
			if (el.attr("name").equals(attribute)) {
				value = el.attr("value");
			}
		}

		return value;
	}

	private String getAttributeActionFromHtml(String htmlContent) {
		Document doc = Jsoup.parse(htmlContent);
		return doc.select("form").attr("action");
	}

	@SuppressWarnings("unchecked")
	public String getIdp(HttpServletRequest request) {
		try {
			final String idpSession = request.getHeader("IDP-JSESSIONID");
			if (idpSession != null) {
				idp = idpSession;
				return idpSession;
			}

			if (StringUtil.isNullOrEmpty(idp)) {
				String idpSessionID = "";
				try {
					Map<String, Object> map = (Map<String, Object>) request
							.getSession().getAttribute("SESSION_ATTRIBUTE_MAP");
					idpSessionID = (String) ((List<Object>) map
							.get("IDPsessionID")).get(0);
				} catch (NullPointerException npe) {
					// relax.
				}

				if (StringUtil.isNotNull(idpSessionID)) {
					idp = idpSessionID;
				} else {
					idp = tryGetIDPSessionIDFromRequest(request);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			idp = tryGetIDPSessionIDFromRequest(request);
		}
		return idp;
	}

	private String tryGetIDPSessionIDFromRequest(HttpServletRequest request) {
		if (request != null && request.getAttribute("idp") != null) {
			String idpFromRequest = (String) request.getAttribute("idp");
			if (StringUtil.isNotNull(idpFromRequest))
				return idpFromRequest;

		}

		return "";
	}

	public void setIdp(String idp) {
		this.idp = idp;
	}

	public String getNaWeb(String URL, HashMap<String, String> header,
			Integer timeout, String payload) throws AplicacaoException {

		try {

			HttpURLConnection conn = (HttpURLConnection) new URL(URL)
					.openConnection();

			if (timeout != null) {
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
			}

			// conn.setInstanceFollowRedirects(true);

			if (header != null) {
				for (String s : header.keySet()) {
					conn.setRequestProperty(s, header.get(s));
				}
			}

			System.setProperty("http.keepAlive", "false");

			if (payload != null) {
				byte ab[] = payload.getBytes("UTF-8");
				conn.setRequestMethod("POST");
				// Send post request
				conn.setDoOutput(true);
				try (OutputStream os = conn.getOutputStream()) {
					os.write(ab);
					os.flush();
					os.close();
				}
			}

			// StringWriter writer = new StringWriter();
			// IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
			// return writer.toString();
			return IOUtils.toString(conn.getInputStream(), "UTF-8");

		} catch (IOException ioe) {
			throw new AplicacaoException(
					"NÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â£o foi possÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â­vel abrir conexÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â£o",
					1, ioe);
		}

	}
}
