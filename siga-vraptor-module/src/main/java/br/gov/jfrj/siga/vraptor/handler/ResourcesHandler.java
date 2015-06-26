package br.gov.jfrj.siga.vraptor.handler;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.StereotypeHandler;

@Component
@ApplicationScoped
public class ResourcesHandler implements StereotypeHandler {

    public ResourcesHandler(Router router) {
        Resources.getInstance().setRouter(router);
    }

	@Override
	public Class<? extends Annotation> stereotype() {
		return Resource.class;
	}

	@Override
	public void handle(Class<?> type) {
	    Resources.getInstance().setClassAndMethods(type, Arrays.asList(type.getDeclaredMethods()));
	}
}
