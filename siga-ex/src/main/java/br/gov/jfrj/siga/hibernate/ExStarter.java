package br.gov.jfrj.siga.hibernate;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * @author Rodrigo Ramalho hodrigohamalho@gmail.com
 *
 */
@Startup
@Singleton
public class ExStarter {

	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		emf = Persistence.createEntityManagerFactory("default");
	}
}