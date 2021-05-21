package br.gov.jfrj.siga.wf.dao;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.UsuarioDeSistemaEnum;
import br.gov.jfrj.siga.cp.util.SigaFlyway;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class WfStarter {

	private final static org.jboss.logging.Logger log = Logger.getLogger(WfStarter.class);
	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		log.info("INICIANDO SIGAWF.WAR");

		emf = Persistence.createEntityManagerFactory("default");
		Service.setUsuarioDeSistema(UsuarioDeSistemaEnum.SIGA_WF);
		new MigrationThread().start();
	}

	public static class MigrationThread extends Thread {
		public void run() {
			try {
				SigaFlyway.migrate("java:/jboss/datasources/SigaWfDS", "classpath:db/mysql/sigawf", true);
			} catch (NamingException e) {
				log.error("Erro na migração do banco", e);
				SigaFlyway.stopJBoss();
			}
		}
	}
}