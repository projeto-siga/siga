package br.gov.jfrj.siga.tp.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.InterceptorExecutor;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.jpa.JPATransactionInterceptor;
import br.com.caelum.vraptor.jpa.JPATransactionInterceptor;
import br.gov.jfrj.siga.tp.auth.annotation.DadosAuditoria;
import br.gov.jfrj.siga.tp.auth.annotation.LogMotivo;
import br.gov.jfrj.siga.tp.util.ContextoRequest;
import br.gov.jfrj.siga.vraptor.SigaObjects;

/**
 * Interceptor que processa a anotacao {@link LogMotivo}.
 * 
 * Funciona junto a tag MotivoLog nos formularios Abastecimentos/listar e  
 * ControlesGabinete/listar, metodo de exclusao.
 * Necessario incluir uma tag <form> com id="formulario".
 * Incluir tambem a tag #{motivoLog /} antes de </form>
 * 
 *  @author db1
 *
 */
@RequestScoped
@Intercepts(after = {PreencherAutorizacaoGIInterceptor.class }, before = JPATransactionInterceptor.class)
public class MotivoLogInterceptor  {

	private Result result;
	private HttpServletRequest request;
	private DadosAuditoria da;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public MotivoLogInterceptor() {
		super();
		this.result = null;
		this.request = null;
		this.da = null;
	}
	
	@Inject
	public MotivoLogInterceptor(Result result, HttpServletRequest request, SigaObjects so, DadosAuditoria da) {
		this.result = result;
		this.request = request;
		this.da = da;
		this.da.setMatricula(so.getCadastrante().getSigla());
		this.da.setMotivoLog(null);
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack)   {
		try {
			ContextoRequest.setDadosAuditoria(da);
			stack.next();
		} catch (Exception e) {
				throw new InterceptionException(e);
		} finally {
			ContextoRequest.removeDadosAuditoria();
		}

	}

	private Boolean isPreencheuMotivo(ControllerMethod method ) {
		LogMotivo motivoLogAnnotation = method.getMethod().getAnnotation(LogMotivo.class);
		if (motivoLogAnnotation != null) {
			String motivoLog = request.getParameter("motivoLog");
			if (motivoLog == null || motivoLog.isEmpty()) {
				throw new InterceptionException("views.erro.campoObrigatorio");
			}
			this.da.setMotivoLog(motivoLog);
			result.include("motivoLog", motivoLog);
			return true;
		}
		return false;
	}

	@Accepts
	public boolean accepts(ControllerMethod method) {
		return (method.containsAnnotation(LogMotivo.class) && isPreencheuMotivo(method));
	}
}

