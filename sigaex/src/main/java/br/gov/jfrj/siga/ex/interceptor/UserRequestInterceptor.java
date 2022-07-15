package br.gov.jfrj.siga.ex.interceptor;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.interceptor.payload.UserRequestPayload;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.TrackRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Intercepts
@RequestScoped
@AcceptsWithAnnotations(TrackRequest.class)
public class UserRequestInterceptor {
    private HttpServletRequest request;
    private DpPessoa cadastrante;
    private UserRequestPayload userRequestPayload;

    private static Log log = LogFactory.getLog(UserRequestInterceptor.class);

    public UserRequestInterceptor() {
        this.request = null;
        this.cadastrante = null;
    }

    @Inject
    public UserRequestInterceptor(HttpServletRequest request, SigaObjects sigaObjects) {
        this.request = request;
        this.cadastrante = sigaObjects.getCadastrante();
        this.userRequestPayload = new UserRequestPayload(
                this.request.getRequestURL().toString(),
                this.request.getQueryString(),
                this.cadastrante.getMatricula().toString(),
                this.cadastrante.getLotacao().toString(),
                this.cadastrante.getNomePessoa(),
                ExDao.getInstance().consultarDataEHoraDoServidor().toString(),
                HttpRequestUtils.getIpAudit(request)
        );
    }

    @AroundCall
    public void around(SimpleInterceptorStack stack) {
        log.info(userRequestPayload);

        stack.next();
    }

}
