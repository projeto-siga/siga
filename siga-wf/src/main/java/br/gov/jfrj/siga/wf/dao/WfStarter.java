package br.gov.jfrj.siga.wf.dao;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.SigaVersion;
import br.gov.jfrj.siga.base.UsuarioDeSistemaEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.util.SigaFlyway;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeConfiguracao;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class WfStarter {


    private final static org.jboss.logging.Logger log = Logger.getLogger(WfStarter.class);
    public static EntityManagerFactory emf;

    @Inject
    public void setEM(EntityManagerFactory factory) {
        emf = factory;
    }

    @PostConstruct
    public void init() {
        log.info("INICIANDO SIGAWF.WAR");
        
		SigaVersion.loadSigaVersion();
		log.info("SIGAWF Versão: v" + SigaVersion.SIGA_VERSION);
		log.info("Data da Versão: v" + SigaVersion.SIGA_VERSION_DATE);
		
        CpTipoDeConfiguracao.mapear(CpTipoDeConfiguracao.values());
        CpTipoDeConfiguracao.mapear(WfTipoDeConfiguracao.values());

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