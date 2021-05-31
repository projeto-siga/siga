package br.gov.jfrj.siga.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.gov.jfrj.siga.cp.util.SigaFlyway;

/**
 * 
 * @author Rodrigo Ramalho hodrigohamalho@gmail.com
 *
 */
@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class SigaStarter {

	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		SigaFlyway.migrate("java:/jboss/datasources/SigaCpDS", "sigacp", "corporativo");
		emf = Persistence.createEntityManagerFactory("default");
	}

}