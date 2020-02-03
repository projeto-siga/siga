package br.gov.sp.prodesp.siga.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nimbusds.openid.connect.sdk.AuthenticationRequest;

import br.gov.sp.prodesp.siga.client.OIDCClient;

@WebServlet("/openIdServlet")
public class OpenIdServlet extends javax.servlet.http.HttpServlet {

	private static final long serialVersionUID = 1L;

	public void loginSP(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("SERVLET LOGIN SP");

		response.setContentType("text/html");

		System.out.println("Publicando ...");

		response.getWriter().println("<HTML>LOGIN SP</HTML>");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head>");
            out.println("<title>Meu Servlet Exemplo</title>");
            out.println("</head><body>");
            out.println("<h1>Minha Resposta Usando Servlet</h1>");
            out.println("</body></html>");
            
            try{
    			OIDCClient clientOIDC = new OIDCClient();
    			AuthenticationRequest authRequest1 = clientOIDC.buildAuthenticationRequest();
    			
    			if (authRequest1 != null) {
    				// Initial set
    				String uriString = authRequest1.toURI().toString();
    				// popupOpener.setUrl(uriString);
    				System.out.println("LOGIN SP: "  + uriString);
    		        redirectToLogin(req, resp, uriString);
    			}
    			
    		}catch(Exception e){
    			
    		}
	  } finally {
	            //. . .
	  }
	}
	
	private void redirectToLogin(HttpServletRequest request, HttpServletResponse response, String redirectTo) throws IOException {
		System.out.println("Redirect: "  + redirectTo);
        response.sendRedirect(redirectTo);
    }
}
