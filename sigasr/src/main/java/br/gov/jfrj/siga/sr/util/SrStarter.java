package br.gov.jfrj.siga.sr.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.SigaVersion;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.util.SigaFlyway;
import br.gov.jfrj.siga.sr.model.enm.SrTipoDeConfiguracao;

@Startup
@Singleton
@ApplicationScoped
public class SrStarter {

    private final static org.jboss.logging.Logger log = Logger.getLogger(SrStarter.class);

    public static EntityManagerFactory emf;

    @Inject
    public void setEM(EntityManagerFactory factory) {
        emf = factory;
    }

    @PostConstruct
    public void init() {
        log.info("INICIANDO SIGASR.WAR");
		SigaVersion.loadSigaVersion();
		log.info("SIGASR Versão: v" + SigaVersion.SIGA_VERSION);
		log.info("Data da Versão: v" + SigaVersion.SIGA_VERSION_DATE);
        CpTipoDeConfiguracao.mapear(CpTipoDeConfiguracao.values());
        CpTipoDeConfiguracao.mapear(SrTipoDeConfiguracao.values());

        new MigrationThread().start();
    }

    public static class MigrationThread extends Thread {
        public void run() {
            try {
                SigaFlyway.migrate("java:/jboss/datasources/SigaServicosDS", "classpath:db/mysql/sigasr", true);
            } catch (NamingException e) {
                log.error("Erro na migração do banco", e);
                SigaFlyway.stopJBoss();
            }
        }
    }
}
