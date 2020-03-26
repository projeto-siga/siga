package br.gov.jfrj.siga.ex.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.crivano.swaggerservlet.SwaggerServlet;

public class ExServlet extends SwaggerServlet {
	private static final long serialVersionUID = 1756711359239182178L;

	@Override
	public void initialize(ServletConfig config) throws ServletException {
		super.setActionPackage("br.gov.jfrj.siga.servlet");
	}
}
