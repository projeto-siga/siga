package br.gov.jfrj.siga.tp.interceptor;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.tp.auth.annotation.DadosAuditoria;
import br.gov.jfrj.siga.tp.auth.annotation.LogMotivo;
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
@Intercepts(after = { AutorizacaoAcessoInterceptor.class }, before = ExecuteMethodInterceptor.class)
public class MotivoLogInterceptor implements Interceptor {

	private Result result;
	private HttpServletRequest request;
	private DadosAuditoria da;

	public MotivoLogInterceptor(Result result, HttpServletRequest request, SigaObjects so, DadosAuditoria da) {
		this.result = result;
		this.request = request;
		this.da = da;
		this.da.setMatricula(so.getCadastrante().getSigla());
		this.da.setMotivoLog(null);
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		LogMotivo motivoLogAnnotation = method.getMethod().getAnnotation(LogMotivo.class);

		if (motivoLogAnnotation != null) {
			String motivoLog = request.getParameter("motivoLog");
			if (motivoLog == null || motivoLog.isEmpty()) {
				throw new InterceptionException("views.erro.campoObrigatorio");
			}
			this.da.setMotivoLog(motivoLog);
			result.include("motivoLog", motivoLog);
		}
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(LogMotivo.class);
	}
}