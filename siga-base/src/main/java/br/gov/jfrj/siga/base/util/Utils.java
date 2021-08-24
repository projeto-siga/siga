package br.gov.jfrj.siga.base.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import br.gov.jfrj.siga.base.GZip;
import br.gov.jfrj.siga.model.Historico;

public class Utils {
	/**
	 * @param map
	 * @param form
	 */
	public static void mapFromUrlEncodedForm(Map map, final byte[] form) {
		if (form != null) {
			final String as[] = new String(form).split("&");
			for (final String s : as) {
				final String param[] = s.split("=");
				try {
					if (param.length == 2) {
						map.put(param[0], URLDecoder.decode(param[1], "iso-8859-1"));
					}
				} catch (final UnsupportedEncodingException e) {
				}
			}
		}
	}

	public static String completarComZeros(int valor, int casas) {
		String s = String.valueOf(valor);
		while (s.length() < casas)
			s = "0" + s;
		return s;
	}

	public static boolean empty(String s) {
		return s == null || s.trim().length() == 0;
	}
	
	public static boolean empty(Object s) {
		if (s instanceof String) return Utils.empty((String) s);
		return s == null;
	}
	
	
	
	public static boolean isEmailValido(String email) {
		Pattern pattern = Pattern.compile(Texto.DpPessoa.EMAIL_REGEX_PATTERN);   
	    Matcher matcher = pattern.matcher(email);   
	    return matcher.find();   
	}
	
	public static String getBaseUrl(HttpServletRequest request) {
	    String scheme = request.getScheme() + "://";
	    String serverName = request.getServerName();
	    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
	    String contextPath = request.getContextPath();
	    return scheme + serverName + serverPort + contextPath;
	  }
	
	/*
	* Devolve o valor do cookie com o nome correspondente
	*/
	public static String getCookieValue(HttpServletRequest request, String name) {
		final Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;
		return Arrays.stream(cookies)
		             .filter(e -> name.equals(e.getName()))
		             .findAny()
		             .map(Cookie::getValue)
		             .orElse(null);		
	} 

	public static String encodeAndZip(String json) {
		byte[] compressed;
		try {
			compressed = GZip.compress(json.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new RuntimeException("Erro codificando JSON", e);
		}
		String base64 = java.util.Base64.getEncoder().encodeToString(compressed);
		return base64;
	}

	public static String unzipAndDecode(String base64) {
		byte[] compressed = java.util.Base64.getDecoder().decode(base64);
		byte[] decompressed;
		try {
			decompressed = GZip.decompress(compressed);
		} catch (IOException e) {
			throw new RuntimeException("Erro decodificando JSON", e);
		}
		String json = new String(decompressed, StandardCharsets.UTF_8);
		return json;
	}

	public static boolean equivale(Historico o1, Historico o2) {
		if (o1 != null && o2 != null && o1.equivale(o2))
			return true;
		return false;
	}
}