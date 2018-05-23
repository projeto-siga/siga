package br.gov.jfrj.siga.idp.jwt.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.jfrj.siga.idp.jwt.AuthJwtFormFilter;

/**
 * Servlet para FORM de autenticação usando JWT.
 * 
 * @author tah
 *
 */
public class SigaJwtFormLogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(req, resp);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Headers",
				"Authorization,Content-Type,Jwt-Options");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.addCookie(AuthJwtFormFilter.buildEraseCookie());
		resp.sendRedirect("/siga");
		// req.getRequestDispatcher("/paginas/jwt-login.jsp").forward(req, resp);
	}
}
