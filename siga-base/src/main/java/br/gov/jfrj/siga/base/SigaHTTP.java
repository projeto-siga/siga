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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Rodrigo Ramalho hodrigohamalho@gmail.com
 *
 *         Essa classe devera ser usada para requests entre modulos do SIGA. Se
 *         fizer um GET direto o request sera processado como feito por um
 *         usuario anonimo e retornara um form de autenticao.
 */
public class SigaHTTP {

	private final String SAMLRequest = "SAMLRequest";
	private final String SAMLResponse = "SAMLResponse";
	private final String HTTP_POST_BINDING = "HTTP Post Binding";

	/**
	 * @param URL
	 *            deve ser a url completa.
	 * @param cookieStore
	 *            Necessaria se desejar incluir cookies alem da session do idp
	 * @param idpDomain
	 *            dominio do idp, ex.: siga.jfrj.jus.br
	 * @param idpSession
	 *            valor do cookie JSESSIONID do idp
	 */
	public String get(String URL, CookieStore cookieStore, String idpDomain,
			String idpSession) throws Exception {
		if (idpDomain != null && idpDomain.contains(":"))
			idpDomain = idpDomain.split(":")[0];
		return handleAuthentication(URL, cookieStore, idpDomain, idpSession);
	}

	public String get(String URL) throws Exception {
		return get(URL, null, null, null);
	}

	/**
	 * Esse é um cliente de HTTP que despreza o certificado e aceita qualquer
	 * servidor. Precisamos implementar isso para conseguir conectar ao GSA, que
	 * náo tinha certificado válido e exigia HTTPS para autenticar com o SAML.
	 */
	public HttpClient getNewHttpClient() throws KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException {
		HttpClientBuilder b = HttpClientBuilder.create();

		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
				new TrustStrategy() {
					public boolean isTrusted(X509Certificate[] arg0, String arg1)
							throws CertificateException {
						return true;
					}
				}).build();
		b.setSslcontext(sslContext);

		X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
				sslContext, hostnameVerifier);
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http",
						PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslSocketFactory).build();

		PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry);
		b.setConnectionManager(connMgr);

		HttpClient client = b.build();
		return client;
	}

	String handleAuthentication(String URL, CookieStore cookieStore,
			String idpDomain, String idpSession) throws Exception {
		String html = "";

		Executor exec = Executor.newInstance(getNewHttpClient());
		if (cookieStore == null)
			cookieStore = new BasicCookieStore();
		if (idpSession != null && idpDomain != null) {
			BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",
					idpSession);
			cookie.setPath("/sigaidp");
			cookie.setDomain(idpDomain);
			cookie.setSecure(false);
			cookie.setVersion(1);
			cookieStore.addCookie(cookie);
		}
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
			// currentCookie = extractSetCookie(currentCookie, response);

			// Verifica se retornou o form de autenticao do
			// picketlink
			if (html.contains(HTTP_POST_BINDING)) {
				if (idpSession == null || idpSession.trim().length() == 0) {
					return "";
				}

				if (html.contains(SAMLRequest)) {
					// Atribui o valor do SAMLRequest contido no html retornado
					// no GET efetuado.
					String SAMLRequestValue = getAttributeValueFromHtml(html,
							SAMLRequest);
					// Atribui a URL do IDP (sigaidp)
					String idpURL = getAttributeActionFromHtml(html);

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
					// Extrai o valor do SAMLResponse. Caso o SAMLResponse nao
					// esteja disponivel aqui, eh porque o JSESSIONID utilizado
					// nao representa uma session valida do do IDP.
					String SAMLResponseValue = getAttributeValueFromHtml(html,
							SAMLResponse);
					String spURL = getAttributeActionFromHtml(html);

					// Precisei inserir totod esses headers porque o GSA
					// reamente dava erro sem eles.
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

					// Esse redirect deveria ser automatico, mas o GSA manda um
					// header de connectio: close, e isso aparentemente
					// interrompe o redirecionamento automatico.
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

		return html;
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
				&& html.contains("vel Completar a Opera");
	}

	public boolean isAuthPage(String html) {
		return html.contains("Senha") && html.contains("Apostila");
	}

	public boolean isIDPPage(String html) {
		return html.contains("siga-modules") && html.contains("siga-box");
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

	public String getNaWeb(String URL, HashMap<String, String> header,
			Integer timeout, String payload) throws AplicacaoException {
		try {
			return IOUtils.toString(fetch(URL, header, timeout, payload), "UTF-8");
		} catch (Exception ioe) {
			throw new RuntimeException("Erro obtendo recurso externo", ioe);
		}
	}
	
	public InputStream fetch(String URL, HashMap<String, String> header,
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
			return conn.getInputStream();
		} catch (IOException ioe) {
			throw new RuntimeException("Não foi possível abrir conexão", ioe);
		}
	}

	public static byte[] convertStreamToByteArray(InputStream stream, long size) throws IOException {
		// check to ensure that file size is not larger than Integer.MAX_VALUE.
		if (size > Integer.MAX_VALUE) {
			return new byte[0];
		}

		byte[] buffer = new byte[(int) size];
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		int line = 0;
		// read bytes from stream, and store them in buffer
		while ((line = stream.read(buffer)) != -1) {
			// Writes bytes from byte array (buffer) into output stream.
			os.write(buffer, 0, line);
		}
		stream.close();
		os.flush();
		os.close();
		return os.toByteArray();
	}
}
