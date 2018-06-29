package br.gov.jfrj.siga.tp.interceptor;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.InstantiateInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.auth.Autorizacoes;

/**
 * Inteceptor responsavel por preencher as permissoes disponiveis para o usuario.
 * 
 * @author db1
 *
 */
@RequestScoped
@Intercepts(after = { ContextInterceptor.class }, before = InstantiateInterceptor.class)
public class PreencherAutorizacaoGIInterceptor implements Interceptor {

	private AutorizacaoGI autorizacaoGI;
	private Result result;

	public PreencherAutorizacaoGIInterceptor(Result result, AutorizacaoGI autorizacaoGI) {
		this.result = result;
		this.autorizacaoGI = autorizacaoGI;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		autorizacaoGI.incluir(Autorizacoes.ADM_ADMINISTRAR)
			.incluir(Autorizacoes.ADMFROTA_ADMINISTRAR_FROTA)
			.incluir(Autorizacoes.ADMMISSAO_ADMINISTRAR_MISSAO)
			.incluir(Autorizacoes.APR_APROVADOR)
			.incluir(Autorizacoes.GAB_GABINETE)
			.incluir(Autorizacoes.ADMGAB_ADMIN_GABINETE)
			.incluir(Autorizacoes.AGN_AGENTE)
			.incluirAdministrarMissaoComplexo(result)
			.preencherDadosAutorizacoes(result);

		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return Boolean.TRUE;
	}
}