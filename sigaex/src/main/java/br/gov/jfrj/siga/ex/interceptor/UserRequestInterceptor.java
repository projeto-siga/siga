package br.gov.jfrj.siga.ex.interceptor;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.dp.DpPessoa;
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

    private static Log log = LogFactory.getLog(UserRequestInterceptor.class);

    public UserRequestInterceptor() {
        this.request = null;
        this.cadastrante = null;
    }

    @Inject
    public UserRequestInterceptor(HttpServletRequest request, SigaObjects sigaObjects) {
        this.request = request;
        this.cadastrante = sigaObjects.getCadastrante();
    }

    @AroundCall
    public void around(SimpleInterceptorStack stack) {
        log.info(
                request.getRequestURL() + ";"
                        + this.cadastrante.getMatricula() + ";"
                        + this.cadastrante.getLotacao() + ";"
                        + this.cadastrante.getNomePessoa() + ";"
                        + ExDao.getInstance().consultarDataEHoraDoServidor() + ";"
                        + HttpRequestUtils.getIpAudit(request)
        );

        stack.next();
    }

}
