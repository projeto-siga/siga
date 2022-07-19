package br.gov.jfrj.siga.ex.interceptor;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.ex.interceptor.payload.UserRequestPayload;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.TrackRequest;
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
    private UserRequestPayload userRequestPayload;

    private final Level BLAME = Level.forName("BLAME", 450);
    private final Logger logger = LogManager.getLogger(UserRequestInterceptor.class);

    public UserRequestInterceptor() {
        this.userRequestPayload = null;
    }

    @Inject
    public UserRequestInterceptor(HttpServletRequest request, SigaObjects sigaObjects) {
        this.userRequestPayload = new UserRequestPayload(request, sigaObjects.getCadastrante());
    }

    @AroundCall
    public void around(SimpleInterceptorStack stack) {
        logger.log(BLAME, userRequestPayload);

        stack.next();
    }

}
