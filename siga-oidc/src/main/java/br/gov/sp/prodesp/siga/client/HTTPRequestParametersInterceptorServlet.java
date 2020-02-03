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

/**
 * Extends the default Vaadin servlet to pass HTTP GET and HTTP form POST
 * parameters to the UI page. See https://vaadin.com/forum#!/thread/4210576
 */

public class HTTPRequestParametersInterceptorServlet  extends HttpServlet {

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		
		super.init(servletConfig);
	}
	
	
	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		super.service(request, response);

		// Copy the HTTP request (GET, POST) parameters, else Vaadin will overwrite them
		Map<String, List<String>> paramsCopy = new HashMap<>();

		for (String key: request.getParameterMap().keySet()) {

			List<String> values = Arrays.asList(request.getParameterMap().get(key));

			if (values == null || values.isEmpty())
				continue;

			paramsCopy.put(key, values);
		}

		//log.info("HTTP request parameters: " + paramsCopy);

		request.getSession().setAttribute("http-request-parameters", paramsCopy);
	}
}
