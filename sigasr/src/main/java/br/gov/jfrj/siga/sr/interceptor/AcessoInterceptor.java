package br.gov.jfrj.siga.sr.interceptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EDTCONH_CRIAR_CONHECIMENTOS;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EXIBIR_MENU_ADMINISTRAR;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EXIBIR_MENU_CONHECIMENTOS;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EXIBIR_MENU_RELATORIOS;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EXIBIR_CAMPO_PRIORIDADE;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.REL_RELATORIOS;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.PRIORIZAR_AO_ABRIR;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;

import br.gov.jfrj.siga.vraptor.SigaObjects;

@RequestScoped
@Intercepts(after = { ContextInterceptor.class })
public class AcessoInterceptor  {
    
    private Result result;
    private SigaObjects so;

	/**
	 * @deprecated CDI eyes only
	 */
	public AcessoInterceptor() {
		super();
		result = null;
		so = null;
	}
    
    @Inject
	public AcessoInterceptor(Result result, SigaObjects so) {
        this.result = result;
        this.so = so;
    }

    @AroundCall
    public void intercept(InterceptorStack stack, ControllerMethod method, Object resourceInstance) throws InterceptionException {
        addPermissaoMenu(result);

        stack.next(method, resourceInstance);
    }

    private void addPermissaoMenu(Result result) {

        try {
            so.assertAcesso(ADM_ADMINISTRAR);
            result.include(EXIBIR_MENU_ADMINISTRAR, true);
        } catch (Exception e) {
            result.include(EXIBIR_MENU_ADMINISTRAR, false);
        }

        try {
            so.assertAcesso(EDTCONH_CRIAR_CONHECIMENTOS);
             result.include(EXIBIR_MENU_CONHECIMENTOS, true);
        } catch (Exception e) {
            result.include(EXIBIR_MENU_CONHECIMENTOS, false);
        }

        try {
            so.assertAcesso(REL_RELATORIOS);
             result.include(EXIBIR_MENU_RELATORIOS, true);
        } catch (Exception e) {
            result.include(EXIBIR_MENU_RELATORIOS, false);
        }
        
        try {
            so.assertAcesso(PRIORIZAR_AO_ABRIR);
             result.include(EXIBIR_CAMPO_PRIORIDADE, true);
        } catch (Exception e) {
            result.include(EXIBIR_CAMPO_PRIORIDADE, false);
        }
    }

    @Accepts
    public boolean accepts(ControllerMethod method) {
        return Boolean.TRUE;
    }

}
