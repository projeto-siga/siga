package br.gov.jfrj.siga.xjus;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.XjusRecordServiceEnum;

public class Utils {

	public static String formatId(Long id) {
		return String.format("%012d", id);
	}

	public static String buildUrl(XjusRecordServiceEnum service) {
		return SwaggerServlet.getHttpServletRequest().getRequestURL().toString().replace("siga/apis/x-jus/v1/",
				service.path);
	}
}
