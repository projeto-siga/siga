package br.gov.jfrj.siga.gc.util;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * @author Rodrigo Ramalho hodrigohamalho@gmail.com
 *
 */
@ApplicationScoped
public class GcStarter {

	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		emf = CDI.current().select(EntityManagerFactory.class).get();
		assert emf != null;
	}
}
