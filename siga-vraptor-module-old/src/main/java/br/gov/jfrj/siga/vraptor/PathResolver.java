package br.gov.jfrj.siga.vraptor;

import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.view.DefaultPathResolver;

@Component
public class PathResolver extends DefaultPathResolver {
    public PathResolver(FormatResolver resolver) {
		super(resolver);
	}

	protected String getPrefix() {
        return "/WEB-INF/page/";
    }

    protected String getExtension() {
        return "jsp";
    }
}
