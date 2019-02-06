package br.gov.jfrj.siga.play;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;

import br.gov.jfrj.siga.base.ConexaoHTTP;

public class Proxy extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String retorno = request.getRequestURL() + "?"
				+ request.getQueryString() + "<br/>";

		HashMap<String, String> header = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			retorno += headerName + " = " + request.getHeader(headerName)
					+ "<br/>";
			/*if (!headerName.equals("host"))*/
			if (!headerName.equals("content-length"))
				header.put(headerName, request.getHeader(headerName));
		}

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		try {
			out.write(new String(ConexaoHTTP.get(
					request.getRequestURL().toString().replace("8080", "9000") + "?" + request.getQueryString(),
					header).getBytes()));
		} catch (Exception e) {
			retorno += e.getMessage() + " --- " + e.getCause();
			out.write(retorno);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
	
	
}