package br.gov.jfrj.siga.jee;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SigaErrorHandlerServlet extends HttpServlet {

	// Method to handle GET method request.
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
		String newWindow = (String) request.getParameter("newWindow");
		if ("1".equals(newWindow))
			request.setAttribute("newWindow", newWindow);

		if (servletName == null) {
			servletName = "Unknown";
		}
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

		if (requestUri == null) {
			requestUri = "Unknown";
		}

		if (request.getAttribute("jsonError") != null) {
			response.setContentType("application/json");
			response.getWriter().print(request.getAttribute("jsonError"));
			return;
		}

		if (response.isCommitted())
			return;

		request.setAttribute("exception", throwable);

		request.getRequestDispatcher("/WEB-INF/page/erroGeral.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}