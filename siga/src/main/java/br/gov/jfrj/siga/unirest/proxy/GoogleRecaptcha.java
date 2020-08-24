package br.gov.jfrj.siga.unirest.proxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import br.gov.jfrj.siga.base.Prop;

public class GoogleRecaptcha {
	
	private static String proxyHost = Prop.get("/http.proxyHost");
	private static int proxyPort = Prop.getInt("/http.proxyPort");

	public static boolean isProxySetted() {
		return proxyHost != null && proxyPort > 0;
	}
	
	static void setProxyHost(String proxyHost) {
		GoogleRecaptcha.proxyHost = proxyHost;
	}
	
	static void setProxyPort(int proxyPort) {
		GoogleRecaptcha.proxyPort = proxyPort;
	}
	
	/**
	 * Realiza uma requisicao http a api de recaptcha da Google utilizando a api nativa do Java (java.net)
	 * 
	 * @param secretKey
	 * @param responseUIRecaptcha
	 * @param remoteip
	 * @return
	 * @throws UnirestException
	 */
	public static JsonNode validarRecaptcha(String secretKey, String responseUIRecaptcha, String remoteip) throws UnirestException {

		HttpURLConnection connection = null;
		try {

			String queryParam = "secret="+URLEncoder.encode(secretKey, "UTF-8") +
					"&response="+URLEncoder.encode(responseUIRecaptcha, "UTF-8") +
					"&remoteip="+URLEncoder.encode(remoteip, "UTF-8");

			URL url = new URL("https://www.google.com/recaptcha/api/siteverify?"+queryParam);
			
			InetSocketAddress socketAddress = new InetSocketAddress(proxyHost, proxyPort);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);

			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.setRequestProperty("Content-Length", "0");
			connection.addRequestProperty("accept", "application/json");
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("GET");

			connection.setDoInput(true);
			connection.setDoOutput(true);

			StringBuilder builder = new StringBuilder();
			InputStream inputStream = connection.getInputStream();
			try (BufferedReader newReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
				
				String line = null;
				
				while ((line = newReader.readLine()) != null) {
					builder.append(line);
				}

				return new JsonNode(builder.toString());
			}

		} catch (Exception e) {
			throw new UnirestException(e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

}
