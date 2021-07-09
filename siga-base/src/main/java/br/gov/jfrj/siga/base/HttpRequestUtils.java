package br.gov.jfrj.siga.base;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtils {

	private static final String BASE_URL_PROPERTY = "/siga.base.url";

	public static String getIpAudit(HttpServletRequest req) {
		if (req == null)
			return null;
		String ip = req.getHeader("X-Forwarded-For");
		if (ip == null)
			ip = req.getRemoteHost();
		return ip;
	}

	public static String getHttpSchema(HttpServletRequest req) {
		final String baseUrl = Prop.get(BASE_URL_PROPERTY);
		return baseUrl.substring(0, baseUrl.indexOf("://") + 3);
	}

}
