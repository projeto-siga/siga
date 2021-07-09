package br.gov.jfrj.siga.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class SigaSwaggerRedirectUtils {

	public static final String SWAGGER_UI_SUFFIX = "/swagger-ui";

	private SigaSwaggerRedirectUtils() {}

	public static String getSwaggerYamlSchemaUrl(HttpServletRequest req) {
		final String schema = HttpRequestUtils.getHttpSchema(req);
		final String requestUrl = req.getRequestURL().replace(0, req.getRequestURL().indexOf("://") + 3, schema).toString();
		final String baseApiRequestUrl = requestUrl.replace(SWAGGER_UI_SUFFIX, StringUtils.EMPTY);

		return new StringBuilder(requestUrl)
				.append("/index.html?url=")
				.append(baseApiRequestUrl)
				.append("/swagger.yaml")
				.toString();
	}

}
