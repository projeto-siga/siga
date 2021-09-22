package br.gov.jfrj.siga.hibernate;

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
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.util.SigaFlyway;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class ExStarter {

	private final static org.jboss.logging.Logger log = Logger.getLogger(ExStarter.class);
	public static EntityManagerFactory emf;

	@PostConstruct
	public void init() {
		log.info("INICIANDO SIGAEX.WAR");
		CpTipoDeConfiguracao.mapear(CpTipoDeConfiguracao.values());
		CpTipoDeConfiguracao.mapear(ExTipoDeConfiguracao.values());

		emf = Persistence.createEntityManagerFactory("default");
		Service.setUsuarioDeSistema(UsuarioDeSistemaEnum.SIGA_EX);
		new MigrationThread().start();
	}

	public static class MigrationThread extends Thread {
		public void run() {
			try {
				SigaFlyway.migrate("java:/jboss/datasources/SigaExDS", "classpath:db/mysql/sigaex", true);
			} catch (NamingException e) {
				log.error("Erro na migração do banco", e);
				SigaFlyway.stopJBoss();
			}
		}
	}

}