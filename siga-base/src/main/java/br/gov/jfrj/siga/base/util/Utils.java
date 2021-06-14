package br.gov.jfrj.siga.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.Texto;

public class Utils {

	private static final Logger log = Logger.getLogger(Utils.class);

	private Utils() {}

	/**
	 * @param map
	 * @param form
	 */
	public static void mapFromUrlEncodedForm(final Map<String, Object> map, final byte[] form) {
		if (form != null) {
			final String as[] = new String(form).split("&");
			for (final String s : as) {
				final String param[] = s.split("=");
				try {
					if (param.length == 2) {
						map.put(param[0], URLDecoder.decode(param[1], StandardCharsets.ISO_8859_1.name()));
					}
				} catch (final UnsupportedEncodingException e) {
					log.warn("Erro mapeando parâmetros do formulário da requisição Web", e);
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
	
	

}