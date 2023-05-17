package br.gov.jfrj.siga.api.v1;

import javax.servlet.http.HttpServletRequest;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ISairPost;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.cp.auth.AutenticadorFabrica;
import br.gov.sp.prodesp.siga.servlet.CallBackServlet;

@AcessoPublico
public class SairPost implements ISairPost {
    @Override
    public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
        HttpServletRequest request = ctx.getCtx().getRequest();

        /*
         * Interrompe a sessão local com SSO
         */
        request.getSession().setAttribute(CallBackServlet.PUBLIC_CPF_USER_SSO, null);

        request.getSession(false);
        
        AutenticadorFabrica.getInstance().removerCookie(ctx.getCtx().getRequest(), ctx.getCtx().getResponse());

        resp.status = "OK";
    }

    @Override
    public String getContext() {
        return "logoff usuário";
    }

}
