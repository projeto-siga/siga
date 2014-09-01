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
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
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
public class ConexaoHTTP {
	
	private static final Logger log = Logger.getLogger(ConexaoHTTP.class.getName());
	private Header[] headers;
	private final String COOKIE = "cookie";
	private final String SAMLRequest = "SAMLRequest";
	private final String SAMLResponse = "SAMLResponse";
	private final String JSESSIONID_PREFIX = "JSESSIONID=";
	private final String SET_COOKIE = "set-cookie";

	public String get(String URL, String IDPSessionID) throws AplicacaoException {
		String html = "";
		
		try{
			 // Efetua o request para o Service Provider (módulo)
			 Request req = Request.Get(URL);
			 // Atribui o html retornado e pega o header do Response
			 // Se a aplicação já efetuou a autenticação entre o módulo da URL o conteúdo será trago nesse primeiro GET
			 // Caso contrário passará pelo processo de autenticação (if abaixo)
			 html = req.execute().handleResponse(new ResponseHandler<String>() {
				@Override            
				public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
					// O atributo que importa nesse header é o set-cookie que será utilizado posteriormente
					headers = httpResponse.getAllHeaders();
					return IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
				}
			});

			// Verifica se retornou o form de autenticação do picketlink 
			if (html.contains("HTTP Post Binding (Request)")){
				// Atribui o cookie recuperado no response anterior
				String setCookie = extractCookieFromHeader(getHeader(SET_COOKIE));
				// Atribui o valor do SAMLRequest contido no html retornado no GET efetuado.
				String SAMLRequestValue = getAttributeValueFromHtml(html, SAMLRequest);
				// Atribui a URL do IDP (sigaidp)
				String idpURL = getAttributeActionFromHtml(html);

				// Faz um novo POST para o IDP com o SAMLRequest como parametro e utilizando o sessionID do IDP
				html = Request.Post(idpURL).addHeader(COOKIE, JSESSIONID_PREFIX+IDPSessionID).
						bodyForm(Form.form().add(SAMLRequest, SAMLRequestValue).build()).execute().returnContent().toString();

				// Extrai o valor do SAMLResponse
				// Caso o SAMLResponse não esteja disponível aqui, é porque o JSESSIONID utilizado não foi o do IDP.
				String SAMLResponseValue = getAttributeValueFromHtml(html, SAMLResponse);
				
				// Faz um POST para o SP com o atributo SAMLResponse utilizando o sessionid do primeiro GET
				// O retorno é discartado pois o resultado é um 302.
				Request.Post(URL).addHeader(COOKIE, JSESSIONID_PREFIX+setCookie).
						bodyForm(Form.form().add(SAMLResponse, SAMLResponseValue).build()).execute().discardContent();
				
				// Agora que estamos autenticado efetua o GET para página desejada.
				html = Request.Get(URL).addHeader(COOKIE, JSESSIONID_PREFIX+setCookie).execute().returnContent().toString();
				if (html.contains("HTTP Post Binding (Request)")){
					log.info("Alguma coisa falhou na autenticação!: "+idpURL);
				}
			}
		}catch(Exception io){
			io.printStackTrace();
		}

		return html;
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
}
