package br.gov.jfrj.siga.ex.interceptor;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerInstance;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.interceptor.payload.UserRequestPayload;
import br.gov.jfrj.siga.ex.logic.ExPodeRegistrarRequisicaoUsuario;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.TrackRequest;
import br.gov.jfrj.siga.vraptor.builder.BuscaDocumentoBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Intercepts
@RequestScoped
@AcceptsWithAnnotations(TrackRequest.class)
public class UserRequestInterceptor {
    private HttpServletRequest request;
    private SigaObjects sigaObjects;
    private ExMobil mob;
    private ExPodeRegistrarRequisicaoUsuario exPodeRegistrarRequisicaoUsuario;
    private UserRequestPayload userRequestPayload;

    private final Level BLAME = Level.forName("BLAME", 450);
    private final Logger logger = LogManager.getLogger(UserRequestInterceptor.class);

    public UserRequestInterceptor() {
        this.userRequestPayload = null;
        this.sigaObjects = null;
        this.mob = null;
        this.exPodeRegistrarRequisicaoUsuario = null;
    }

    @Inject
    public UserRequestInterceptor(HttpServletRequest request, SigaObjects sigaObjects) {
        this.request = request;
        this.sigaObjects = sigaObjects;

        String sigla = request.getParameter("sigla");
        final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
        filter.setSigla(sigla);

        this.mob = ExDao.getInstance().consultarPorSigla(filter);

        this.exPodeRegistrarRequisicaoUsuario = new ExPodeRegistrarRequisicaoUsuario(mob,
                sigaObjects.getTitular(), sigaObjects.getLotaTitular());
    }

    @AroundCall
    public void around(SimpleInterceptorStack stack) {
        if(this.exPodeRegistrarRequisicaoUsuario.eval()){
            this.userRequestPayload = new UserRequestPayload(this.request, this.sigaObjects.getCadastrante());
            logger.log(BLAME, userRequestPayload);    
        }

        stack.next();

        UserRequestPayload.clear();
    }

}
