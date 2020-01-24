package br.gov.jfrj.siga.wf.dao;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Startup
@Singleton
public class WfStarter {

	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		emf = Persistence.createEntityManagerFactory("default");
	}
}