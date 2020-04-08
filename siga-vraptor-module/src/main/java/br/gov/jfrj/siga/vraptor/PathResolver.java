package br.gov.jfrj.siga.vraptor;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.view.DefaultPathResolver;

@Specializes
@RequestScoped
public class PathResolver extends DefaultPathResolver {
	/**
	 * @deprecated CDI eyes only
	 */
	protected PathResolver() {
		this(null);
	}

	@Inject
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
