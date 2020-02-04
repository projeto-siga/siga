package br.gov.sp.prodesp.siga.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HTTPRequestParametersInterceptorServlet extends HttpServlet {

	private static final String HTTP_REQUEST_PARAMETERS = "http-request-parameters";
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	}

	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, List<String>> paramsCopy = new HashMap<>();
		for (String key : request.getParameterMap().keySet()) {
			List<String> values = Arrays.asList(request.getParameterMap().get(key));
			if (values == null || values.isEmpty())
				continue;

			paramsCopy.put(key, values);
		}
		request.getSession().setAttribute(HTTP_REQUEST_PARAMETERS, paramsCopy);
		doGet(request, response);
	}
}