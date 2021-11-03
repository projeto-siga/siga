package br.gov.jfrj.siga.gc.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.util.SigaFlyway;

@Startup
@Singleton
@ApplicationScoped
public class GcStarter {

	private final static org.jboss.logging.Logger log = Logger.getLogger(GcStarter.class);
	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		log.info("INICIANDO SIGAGC.WAR");
		CpTipoDeConfiguracao.mapear(CpTipoDeConfiguracao.values());
		emf = CDI.current().select(EntityManagerFactory.class).get();
		assert emf != null;
	}

	public static class MigrationThread extends Thread {
		public void run() {
			try {
				SigaFlyway.migrate("java:/jboss/datasources/SigaGcDS", "classpath:db/mysql/sigagc", true);
			} catch (NamingException e) {
				log.error("Erro na migração do banco", e);
				SigaFlyway.stopJBoss();
			}
		}
	}
}
