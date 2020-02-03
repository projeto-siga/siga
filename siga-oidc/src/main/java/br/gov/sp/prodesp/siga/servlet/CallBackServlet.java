package br.gov.sp.prodesp.siga.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.sp.prodesp.siga.client.HTTPRequestParametersInterceptorServlet;

@WebServlet(urlPatterns = "/callBack", name = "CallbackServlet", asyncSupported = true)
public class CallBackServlet extends HTTPRequestParametersInterceptorServlet {

	private static final long serialVersionUID = 1L;
	
    public static final String STATE_ATTR = "oidc-state";

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head>");
            out.println("<title>Meu Servlet Exemplo</title>");
            out.println("</head><body>");
            out.println("<h1>Minha Resposta Usando Servlet</h1>");
            out.println("</body></html>");
		} catch(Exception ex){
			
		}
            
           
	}
	
	public String handleCallback(HttpServletRequest request) {

        AuthenticatedUser authenticatedUser = new OpenIdConnectCallBackInterceptor(request)
                .extractCode(String.valueOf(request.getSession().getAttribute(STATE_ATTR)))
                .exchangeCodeForTokens()
                .extractAuthenticatedUserInfo(null);

        SecurityContextHolder.getContext().setAuthentication(new OidcUserToken(authenticatedUser));

        return "redirect:/home";

    }
	
}
