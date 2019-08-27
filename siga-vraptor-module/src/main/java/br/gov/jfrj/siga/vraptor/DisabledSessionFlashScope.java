package br.gov.jfrj.siga.vraptor;

import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.SessionFlashScope;

@Component
public class DisabledSessionFlashScope extends SessionFlashScope {

	public DisabledSessionFlashScope(HttpSession session) {
		super(session);
	}

	@Override
	public Object[] consumeParameters(ResourceMethod method) {
		return null;
	}

	@Override
	public void includeParameters(ResourceMethod method, Object[] args) {
		return;
	}

}
