package br.gov.jfrj.siga.vraptor;

import javax.servlet.ServletContext;

import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.view.LinkToHandler;

@Component
@RequestScoped
public class LinkToHandlerCreator implements ComponentFactory<LinkToHandler> {
    private LinkToHandler linkTo;

    public LinkToHandlerCreator(Router router, ServletContext context) {
        this.linkTo = new LinkToHandler(context, router);
    }

    public LinkToHandler getInstance() {
        return linkTo;
    }
}