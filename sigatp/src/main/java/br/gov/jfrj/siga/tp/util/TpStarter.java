package br.gov.jfrj.siga.tp.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.util.SigaFlyway;


/**
 * 
 * @author Rodrigo Ramalho hodrigohamalho@gmail.com
 *
 */
@Startup
@Singleton
@ApplicationScoped
public class TpStarter {
	
	private final static org.jboss.logging.Logger log = Logger.getLogger(TpStarter.class);

	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		log.info("INICIANDO sigatp.WAR");
		CpTipoDeConfiguracao.mapear(CpTipoDeConfiguracao.values());
		emf = Persistence.createEntityManagerFactory("default");
		new MigrationThread().start();
	}

	public static class MigrationThread extends Thread {
		public void run() {
			try {
				SigaFlyway.migrate("java:/jboss/datasources/SigaTpDS", "classpath:db/mysql/sigatp", true);
			} catch (NamingException e) {
				log.error("Erro na migração do banco", e);
				SigaFlyway.stopJBoss();
			}
		}
	}
}