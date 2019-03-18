package br.gov.jfrj.siga.vraptor;

import br.com.caelum.vraptor.http.route.PathAnnotationRoutesParser;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class SigaRoutesParser extends PathAnnotationRoutesParser {

	public SigaRoutesParser(Router router) {
		super(router);
	}

	protected String extractControllerNameFrom(Class<?> type) {
		String prefix = extractPrefix(type);
		if ("".equals(prefix)) {
			String baseName = lowerFirstCharacter(type.getSimpleName());
			if ("appController".equals(baseName))
				return "/app";
			if (baseName.endsWith("Controller")) {
				return "/app/"
						+ baseName.substring(0,
								baseName.lastIndexOf("Controller"));
			}
			return "/app/" + baseName;
		} else {
			return prefix;
		}
	}

	protected String defaultUriFor(String controllerName, String methodName) {
        return super.defaultUriFor(controllerName, methodName);
    }
}