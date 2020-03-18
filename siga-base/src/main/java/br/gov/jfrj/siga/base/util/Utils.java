package br.gov.jfrj.siga.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

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

}
