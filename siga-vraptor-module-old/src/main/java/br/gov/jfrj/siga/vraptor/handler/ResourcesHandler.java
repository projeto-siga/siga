package br.gov.jfrj.siga.vraptor.handler;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.http.route.Router;



@ApplicationScoped
public class ResourcesHandler  {

    public ResourcesHandler() {
    	
    }
	
	@Inject
    public ResourcesHandler(Router router) {
        Resources.getInstance().setRouter(router);
    }


	public Class<? extends Annotation> stereotype() {
		return Controller.class;
	}

	public void handle(Class<?> type) {
	    Resources.getInstance().setClassAndMethods(type, Arrays.asList(type.getDeclaredMethods()));
	}
}
