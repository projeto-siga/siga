package br.gov.jfrj.siga.sr.interceptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EDTCONH_CRIAR_CONHECIMENTOS;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EXIBIR_MENU_ADMINISTRAR;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EXIBIR_MENU_CONHECIMENTOS;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.EXIBIR_MENU_RELATORIOS;
import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.REL_RELATORIOS;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.InstantiateInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@RequestScoped
@Intercepts(after = { ContextInterceptor.class }, before = InstantiateInterceptor.class)
public class MenuAcessoInterceptor implements Interceptor {
    
    private Result result;
    private SigaObjects so;

    public MenuAcessoInterceptor(Result result, SigaObjects so) {
        this.result = result;
        this.so = so;
    }

    @Override
    public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
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
    }

    @Override
    public boolean accepts(ResourceMethod method) {
        return Boolean.TRUE;
    }

}
