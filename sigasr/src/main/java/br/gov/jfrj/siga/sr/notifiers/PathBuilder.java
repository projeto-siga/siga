package br.gov.jfrj.siga.sr.notifiers;

import java.lang.reflect.Method;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.controller.DefaultControllerMethod;
import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.SuperMethod;
import br.com.caelum.vraptor.view.PathResolver;


@RequestScoped
public class PathBuilder {

	private final Proxifier proxifier;
	private final MutableRequest request;
	private final Router router;
	private PathResolver resolver;
	private String path;

	/**
	 * @deprecated CDI eyes only
	 */
	public PathBuilder() {
		super();
		this.proxifier = null;
		this.router = null;
		this.request = null;
		this.resolver = null;
	}
	
	@Inject
	public PathBuilder(Proxifier proxifier, Router router, MutableRequest request, PathResolver resolver) {
		this.proxifier = proxifier;
		this.router = router;
		this.request = request;
		this.resolver = resolver;
	}

	public <T> T pathToForwardTo(final Class<T> type) {
		return proxifier.proxify(type, new MethodInvocation<T>() {
			public Object intercept(T proxy, Method method, Object[] args, SuperMethod superMethod) {
				path = resolver.pathFor(DefaultControllerMethod.instanceFor(type, method));
				return null;
			}
		});
	}

	public <T> T pathToRedirectTo(final Class<T> type) {
		return proxifier.proxify(type, new MethodInvocation<T>() {
			public Object intercept(T proxy, Method method, Object[] args, SuperMethod superMethod) {
				if (!acceptsHttpGet(method)) {
					throw new IllegalArgumentException("Your logic method must accept HTTP GET method if you want to redirect to it");
				}
				String contextPath = request.getContextPath();
				String url = router.urlFor(type, method, args);
				contextPath = contextPath + url;
				path = contextPath;
				return null;
			}
		});
	}

	private boolean acceptsHttpGet(Method method) {
		if (method.isAnnotationPresent(Get.class)) {
			return true;
		}
		for (HttpMethod httpMethod : HttpMethod.values()) {
			if (method.isAnnotationPresent(httpMethod.getAnnotation())) {
				return false;
			}
		}
		return true;
	}

	public String getPath() {
		return path;
	}
	
	public String getFullPath() {
		String server = request
				.getRequestURL()
				.toString()
				.split("sigasr")[0];
		
		return server.substring(0, server.length() - 1) + getPath();
	}
}