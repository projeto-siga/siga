package br.gov.jfrj.autenticidade;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.ex.service.ExService;

public class Servlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String num = req.getParameter("numDoc");
		
		try {
			System.out.println(req.getRemoteHost() + " buscando " + num);
			// ExService service = Service.getExService("localhost", "8080");
			ExService service = Service.getExService();

			if (num == null)
				throw new Exception("Número não informado");

			byte[] b = service.obterPdfPorNumeroAssinatura(num);

			resp.setContentType("application/pdf");
			resp.setContentLength((int) b.length);
			resp.getOutputStream().write(b);
			resp.getOutputStream().flush();
			resp.getOutputStream().close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(req.getRemoteHost() + " buscando " + num + " - ERRO");
			req.setAttribute("erro", "Não foi possível encontrar ou exibir o documento.");
			req.setAttribute("numDoc", num);
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/index.jsp");
			dispatcher.forward(req, resp);

		}

	}

}
