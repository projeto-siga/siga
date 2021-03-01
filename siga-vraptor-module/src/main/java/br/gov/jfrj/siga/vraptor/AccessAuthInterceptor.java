package br.gov.jfrj.siga.vraptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.bl.Cp;

@RequestScoped
@Intercepts(after=ParameterOptionalLoaderInterceptor.class)
public class AccessAuthInterceptor {

	@Inject
	private SigaObjects so;
    @Inject
    private HttpServletRequest request;
    @Inject
    private HttpServletResponse response;
    
    /**
	 * @deprecated CDI eyes only
	 */
	public AccessAuthInterceptor() {
	}
	@AroundCall
	public void intercept(SimpleInterceptorStack stack) throws InterceptionException, UnsupportedEncodingException, IOException {
		if (so.getCadastrante() != null && !Cp.getInstance()
				.getConf()
				.podeUtilizarServicoPorConfiguracao(so.getCadastrante(), so.getLotaTitular(), 
						"SIGA:Sistema Integrado de Gestão Administrativa;WEB:Acesso via Web Browser;")) {
			HttpSession session = request.getSession(false);
			if (session != null)
				session.setAttribute("loginMensagem", "Acesso não permitido via Web Browser para o usuário " + so.getCadastrante());
	
			String cont = request.getRequestURL() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
			String base = Prop.get("/siga.base.url");
			if (base != null && base.startsWith("https:") && cont.startsWith("http:"))
				cont = "https" + cont.substring(4);
	
			response.sendRedirect("/siga/public/app/login?cont=" + URLEncoder.encode(cont, "UTF-8"));
			return;
		}
		stack.next();
	}
}
