package br.gov.jfrj.siga.vraptor;

import java.io.IOException;
import java.net.URLEncoder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.model.ContextoPersistencia;

/**
 * Um interceptor que impede que o usuário externo acesse métodos que não tenham
 * sido anotados com @UsuarioExterno.
 * 
 * @author Renato Crivano
 */
@RequestScoped
@Intercepts(after = AccessAuthInterceptor.class)
public class SigaExternalUserInterceptor extends br.com.caelum.vraptor.jpa.JPATransactionInterceptor {

	@Inject
	private SigaObjects so;
	@Inject
	private HttpServletRequest request;
	@Inject
	private HttpServletResponse response;

	@Accepts
	public boolean accepts(ControllerMethod method) {
		return true;
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack, ControllerMethod method) throws IOException {
		//Testar acesso de usuário externo apenas para requests que são sejam de AcessoPublico
		if (so.getCadastrante() != null) {
			boolean b = CpBL.isUsuarioExterno(so.getCadastrante(), so.getLotaTitular());
			ContextoPersistencia.setUsuarioExterno(b);

			if (!method.containsAnnotation(UsuarioExterno.class) && b) {
				response.sendRedirect("/sigaex/app/expediente/doc/mesa-usuario-externo");
				return;
			}
		}
		stack.next();
	}
}