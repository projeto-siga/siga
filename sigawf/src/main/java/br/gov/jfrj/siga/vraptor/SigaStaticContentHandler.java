package br.gov.jfrj.siga.vraptor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.core.DefaultStaticContentHandler;
import br.com.caelum.vraptor.core.StaticContentHandler;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class SigaStaticContentHandler implements StaticContentHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultStaticContentHandler.class);

	private final ServletContext context;

	public SigaStaticContentHandler(ServletContext context) {
		this.context = context;
	}

	public boolean requestingStaticFile(HttpServletRequest request)
			throws MalformedURLException {
		String relativeUri = uriRelativeToContextRoot(request);
		URL resourceUrl = context.getResource(relativeUri);
		if (resourceUrl != null && isAFile(resourceUrl))
			return true;
		if (relativeUri.startsWith("/servicos"))
			return true;
		return false;
	}

	private String uriRelativeToContextRoot(HttpServletRequest request) {
		String uri = request.getRequestURI().substring(
				request.getContextPath().length());
		return removeQueryStringAndJSessionId(uri);
	}

	private String removeQueryStringAndJSessionId(String uri) {
		return uri.replaceAll("[\\?;].+", "");
	}

	private boolean isAFile(URL resourceUrl) {
		return !resourceUrl.toString().endsWith("/");
	}

	public void deferProcessingToContainer(FilterChain filterChain,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		logger.debug("Deferring request to container: {} ",
				request.getRequestURI());
		filterChain.doFilter(request, response);
	}

}
