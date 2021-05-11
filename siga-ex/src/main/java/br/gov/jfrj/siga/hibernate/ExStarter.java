package br.gov.jfrj.siga.hibernate;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.UsuarioDeSistemaEnum;
import br.gov.jfrj.siga.cp.util.SigaFlyway;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class ExStarter {

	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		try {
			SigaFlyway.migrate("java:/jboss/datasources/SigaExDS", "classpath:db/mysql/sigaex");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		emf = Persistence.createEntityManagerFactory("default");
		Service.setUsuarioDeSistema(UsuarioDeSistemaEnum.SIGA_EX);
	}
}