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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 
 * @author Rodrigo Ramalho
 * 	       hodrigohamalho@gmail.com
 *
 * Essa classe deverá ser usada para requests entre módulos do SIGA. Se fizer um GET direto
 * o request será processado como feito por um usuário anônimo e retornará um form de autenticação.
 */
public class SigaHTTP {

	private static final Logger log = Logger.getLogger(SigaHTTP.class.getName());
	private Header[] headers;
	private final String COOKIE = "cookie";
	private final String SAMLRequest = "SAMLRequest";
	private final String SAMLResponse = "SAMLResponse";
	private final String JSESSIONID_PREFIX = "JSESSIONID=";
	private final String SET_COOKIE = "set-cookie";
	private final String HTTP_POST_BINDING_REQUEST = "HTTP Post Binding (Request)";
	private final String doubleQuotes = "\"";
//	private static final int MAX_RETRY = 2; 
//	private int retryCount = 0;
	private String idp;

	/**
	 * @param URL pode ser a url completa ou relativa.
	 * @param request (se for modulo play, setar pra null)
	 * @param cookieValue (necessario apenas nos modulos play)
	 */
	public String get(String URL, HttpServletRequest request, String cookieValue) {
		if (URL.startsWith("/"))
			URL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + URL;

		return handleAuthentication(URL, request, cookieValue);
	}

	private String handleAuthentication(String URL, HttpServletRequest request, String cookieValue) {
		StringBuilder sb = new StringBuilder();
		sb.append("---- MODULO que deu erro no carregamento ------ \n"); // Prepara informacoes para printar em caso de erro
		
		String html = "";
		Executor exec = Executor.newInstance(HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build());
		String currentCookie = JSESSIONID_PREFIX+doubleQuotes+getCookie(request, cookieValue)+doubleQuotes;
		String idpCookie = JSESSIONID_PREFIX+doubleQuotes+getIdp(request)+doubleQuotes;

		try{
			// Efetua o request para o Service Provider (modulo)
			// Atribui o html retornado e pega o header do Response
			// Se a aplicaçao ja efetuou a autenticação entre o modulo da URL o conteudo sera trago nesse primeiro GET
			// Caso contrario passara pelo processo de autenticação (if abaixo)
			sb.append("-----------------------------------------------------------");
			sb.append("GET url: "+URL + "\n");
			sb.append("COOKIE: "+currentCookie + "\n");
			sb.append("-----------------------------------------------------------");
			html = exec.execute(Request.Get(URL).useExpectContinue().
					addHeader(COOKIE, currentCookie)).
					handleResponse(new ResponseHandler<String>() {
						@Override            
						public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
							// O atributo que importa nesse header e o set-cookie que será utilizado posteriormente
							headers = httpResponse.getAllHeaders();
							return IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
						}
					});

			// Verifica se retornou o form de autenticação do picketlink 
			if (html.contains(HTTP_POST_BINDING_REQUEST)){
				// Atribui o cookie recuperado no response anterior
				String setCookie = null;
				try{
					setCookie = extractCookieFromHeader(getHeader(SET_COOKIE));
				}catch(ElementNotFoundException elnf){
					setCookie = currentCookie;
				}

				// Atribui o valor do SAMLRequest contido no html retornado no GET efetuado.
				String SAMLRequestValue = getAttributeValueFromHtml(html, SAMLRequest);
				// Atribui a URL do IDP (sigaidp)
				String idpURL = getAttributeActionFromHtml(html);
				// Faz um novo POST para o IDP com o SAMLRequest como parametro e utilizando o sessionID do IDP
				sb.append("-----------------------------------------------------------");
				sb.append("POST url: "+idpURL + "\n");
				sb.append("COOKIE: "+idpCookie + "\n");
				sb.append("SAMLRequest: "+SAMLRequestValue + "\n");
				sb.append("-----------------------------------------------------------");
				html = exec.execute(Request.Post(idpURL).useExpectContinue().
						addHeader("content-type", "application/x-www-form-urlencoded").
						addHeader(COOKIE, idpCookie).
						bodyForm(Form.form().add(SAMLRequest, SAMLRequestValue).build())).
						returnContent().toString();

				if (html.contains(SAMLResponse)){
					// Extrai o valor do SAMLResponse
					// Caso o SAMLResponse não esteja disponível aqui, é porque o JSESSIONID utilizado não foi o do IDP.
					String SAMLResponseValue = getAttributeValueFromHtml(html, SAMLResponse);
					String spURL = getAttributeActionFromHtml(html);

					sb.append("-----------------------------------------------------------");
					sb.append("POST url: "+spURL + "\n");
					sb.append("COOKIE: "+setCookie + "\n");
					sb.append("SAMLResponse: "+SAMLResponseValue + "\n");
					sb.append("-----------------------------------------------------------");
					html = exec.execute(Request.Post(spURL).useExpectContinue().
							addHeader("content-type", "application/x-www-form-urlencoded").
							addHeader(COOKIE, setCookie).
							bodyForm(Form.form().add(SAMLResponse, SAMLResponseValue).build())).returnContent().asString();
				}else{
					log.info(sb.toString());
				}
			}
		}catch(Exception io){
			io.printStackTrace();
		}
		
		return html;
	}


	private String getCookie(HttpServletRequest request, String cookieValue) {
		if (cookieValue == null || cookieValue.isEmpty()){
			return request.getSession().getId();
		}
		return cookieValue;
	}


	private String extractCookieFromHeader(String headerValue) {
		return headerValue.substring(headerValue.indexOf("=")+1, headerValue.indexOf(";"));
	}

	private String getHeader(String headerName) throws ElementNotFoundException {

		for (Header header : headers) {
			if (header.getName().equalsIgnoreCase(headerName)){
				return header.getValue();
			}
		}

		throw new ElementNotFoundException("Header "+headerName+ " Not founded on headers variable");
	}


	private String getAttributeValueFromHtml(String htmlContent, String attribute){
		String value = "";

		Document doc = Jsoup.parse(htmlContent);
		// Get SAMLRequest value
		for (Element el : doc.select("input")){
			if (el.attr("name").equals(attribute)){
				value = el.attr("value");
			}
		}

		return value;
	}

	private String getAttributeActionFromHtml(String htmlContent){
		Document doc = Jsoup.parse(htmlContent);
		return doc.select("form").attr("action");
	}

	@SuppressWarnings("unchecked")
	public String getIdp(HttpServletRequest request) {
		try{
			if (idp == null || idp.isEmpty()){
				Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute("SESSION_ATTRIBUTE_MAP");
				String idpSessionID = (String) ((List<Object>) map.get("IDPsessionID")).get(0);
				if (idpSessionID != null){
					idp = idpSessionID;
				}
			}else{
				idp = "";
			}
		}catch(Exception e){
			idp = "";
		}
		return idp;
	}

	public void setIdp(String idp) {
		this.idp = idp;
	}

	public String getNaWeb(String URL, HashMap<String, String> header, Integer timeout, String payload)
			throws AplicacaoException {

		try {

			HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();

			if (timeout != null) {
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
			}

			//conn.setInstanceFollowRedirects(true);

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
				OutputStream os = conn.getOutputStream();
				os.write(ab);
				os.flush();
				os.close();
			}

			//StringWriter writer = new StringWriter();
			//IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
			//return writer.toString();
			return IOUtils.toString(conn.getInputStream(), "UTF-8");

		} catch (IOException ioe) {
			throw new AplicacaoException("Não foi possível abrir conexão", 1, ioe);
		}

	}
}
