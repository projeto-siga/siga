package br.gov.jfrj.siga.vraptor;

import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.view.SessionFlashScope;

@Intercepts
public class DisabledSessionFlashScope extends SessionFlashScope {

	public DisabledSessionFlashScope(HttpSession session) {
		super(session);
	}

	@Override
	public Object[] consumeParameters(ControllerMethod method) {
		return null;
	}

	@Override
	public void includeParameters(ControllerMethod method, Object[] args) {
		return;
	}

}
