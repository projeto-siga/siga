package br.gov.jfrj.siga.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import br.gov.jfrj.siga.base.Texto;

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
	
}