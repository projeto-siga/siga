package br.gov.jfrj.siga.vraptor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import br.com.caelum.vraptor.core.ReflectionProvider;
import br.com.caelum.vraptor.http.route.PathAnnotationRoutesParser;
import br.com.caelum.vraptor.http.route.Router;

@Specializes
@ApplicationScoped
public class SigaRoutesParser extends PathAnnotationRoutesParser {

	/**
	 * @deprecated CDI eyes only
	 */
	protected SigaRoutesParser() {
		this(null, null);
	}

	@Inject
	public SigaRoutesParser(Router router, ReflectionProvider reflectionProvider) {
		super(router, reflectionProvider);
	}

	protected String extractControllerNameFrom(Class<?> type) {
		String prefix = extractPrefix(type);
		if ("".equals(prefix)) {
			String baseName = lowerFirstCharacter(type.getSimpleName());
			if ("appController".equals(baseName))
				return "/app";
			if (baseName.endsWith("Controller")) {
				return "/app/" + baseName.substring(0, baseName.lastIndexOf("Controller"));
			}
			return "/app/" + baseName;
		} else {
			return prefix;
		}
	}

	protected String defaultUriFor(String controllerName, String methodName) {
		return super.defaultUriFor(controllerName, methodName);
	}

	private static String lowerFirstCharacter(String baseName) {
		return baseName.toLowerCase().substring(0, 1) + baseName.substring(1, baseName.length());
	}
}