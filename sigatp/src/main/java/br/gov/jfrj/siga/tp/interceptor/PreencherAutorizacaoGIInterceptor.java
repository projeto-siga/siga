package br.gov.jfrj.siga.tp.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.auth.Autorizacoes;

/**
 * Inteceptor responsavel por preencher as permissoes disponiveis para o usuario.
 * 
 * @author db1
 *
 */
@RequestScoped
@Intercepts(after = { ContextInterceptor.class })
public class PreencherAutorizacaoGIInterceptor  {


	private AutorizacaoGI autorizacaoGI;
	

	private Result result;


	/**
	 * @deprecated CDI eyes only
	 */
	public PreencherAutorizacaoGIInterceptor() {
		super();
		autorizacaoGI = null;
		result = null;

	}
	
	@Inject
	public PreencherAutorizacaoGIInterceptor(AutorizacaoGI autorizacaoGI, Result result) {
		this.autorizacaoGI = autorizacaoGI;
		this.result = result;
	}
	
	@AroundCall
	public void intercept(SimpleInterceptorStack stack)  {
		try {
			autorizacaoGI.incluir(Autorizacoes.ADM_ADMINISTRAR)
			.incluir(Autorizacoes.ADMFROTA_ADMINISTRAR_FROTA)
			.incluir(Autorizacoes.ADMMISSAO_ADMINISTRAR_MISSAO)
			.incluir(Autorizacoes.APR_APROVADOR)
			.incluir(Autorizacoes.GAB_GABINETE)
			.incluir(Autorizacoes.ADMGAB_ADMIN_GABINETE)
			.incluir(Autorizacoes.AGN_AGENTE)
			.incluirAdministrarMissaoComplexo(result)
			.preencherDadosAutorizacoes(result);

		stack.next();
		
		} catch (Exception e) {
			throw new InterceptionException(e);
	} finally {
		
	}
		

	}

	@Accepts
	public boolean accepts(ControllerMethod method) {
		return Boolean.TRUE;
	}
}