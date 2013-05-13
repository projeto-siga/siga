package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getRequestURI().endsWith("loginInvalido"))
			response.sendRedirect("siga/sigalibs/loginInvalido.jsp");

		String uri = (String) request
				.getAttribute("javax.servlet.forward.request_uri");
		String query = (String) request
				.getAttribute("javax.servlet.forward.query_string");
		String redirect = uri + (query != null ? "?" + query : "");
		response.sendRedirect("/siga/redirect.action?uri="
				+ URLEncoder.encode(redirect, "UTF-8"));
	}

}
