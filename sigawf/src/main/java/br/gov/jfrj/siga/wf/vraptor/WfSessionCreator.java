package br.gov.jfrj.siga.wf.vraptor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;
import br.com.caelum.vraptor.ioc.RequestScoped;

//@Component
//@RequestScoped
public class WfSessionCreator implements ComponentFactory<Session> {

	private final SessionFactory factory;
	private Session session;

	public WfSessionCreator(SessionFactory factory) {
		this.factory = factory;
	}

	@PostConstruct
	public void create() {
		//this.session = factory.openSession();
	}

	public Session getInstance() {
		return session;
	}

	@PreDestroy
	public void destroy() {
		//this.session.close();
	}

}