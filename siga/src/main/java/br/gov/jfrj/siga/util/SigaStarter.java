package br.gov.jfrj.siga.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.gov.jfrj.siga.cp.util.SigaFlyway;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class SigaStarter {

	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() throws NamingException {
		SigaFlyway.migrate("java:/jboss/datasources/SigaCpDS", "classpath:db/mysql/sigacp");
		emf = Persistence.createEntityManagerFactory("default");
	}
}