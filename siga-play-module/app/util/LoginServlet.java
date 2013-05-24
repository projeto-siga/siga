package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import play.Logger;

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

		String out = "Headers: ";
		Enumeration enames = request.getHeaderNames();
		while (enames.hasMoreElements()) {
			String name = (String) enames.nextElement();
			String value = request.getHeader(name);
			out += name + "=" + value + "; ";
		}
		out += "Attributes: ";
		if (request.getSession() != null) {
			enames = request.getSession().getAttributeNames();
			while (enames.hasMoreElements()) {
				String name = (String) enames.nextElement();
				String value = "" + request.getSession().getAttribute(name);
				out += name + "=" + value + "; ";
			}
		}

		Logger.error("Siga-SR Servlet: " + out);

	}

}
